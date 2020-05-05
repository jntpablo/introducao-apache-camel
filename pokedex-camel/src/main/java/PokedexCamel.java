import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.impl.DefaultCamelContext;

public class PokedexCamel {

    public static void main(String[] args) throws Exception {
        CamelContext camelContext = new DefaultCamelContext();

        camelContext.addRoutes(new RouteBuilder() {
            public void configure() throws Exception {
                from("file:input-pokemon?noop=true").
                        routeId("rota-principal").
                        split().xpath("/pokemons/name").
                        to("direct:rota-pokeapi").
                        to("direct:rota-pokedex");

                from("direct:rota-pokeapi").
                        routeId("rota-pokeapi").
                        setProperty("pokemonName", xpath("/name/text()")).
                        log("Buscando dados do Pokemon: ${property.pokemonName}").
                        setHeader(Exchange.HTTP_METHOD, HttpMethods.GET).
                        setHeader(Exchange.HTTP_PATH, simple("${property.pokemonName}")).
                        to("http4://pokeapi.co/api/v2/pokemon");

                from("direct:rota-pokedex").
                        routeId("rota-pokedex").
                        log("Salvando dados do Pokemon na Pokedex").
                        setHeader(Exchange.HTTP_METHOD, HttpMethods.POST).
                        setHeader(Exchange.HTTP_PATH, constant("pokedex")).
                        to("http4://localhost:8080");
            }
        });

        camelContext.start();
        Thread.sleep(20000);
        camelContext.stop();
    }
}
