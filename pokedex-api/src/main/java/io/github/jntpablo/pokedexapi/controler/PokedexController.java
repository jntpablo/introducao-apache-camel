package io.github.jntpablo.pokedexapi.controler;

import io.github.jntpablo.pokedexapi.domain.Pokemon;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pokedex")
public class PokedexController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void adicionar(@RequestBody Pokemon pokemon) {
        System.out.println("[POST] - " + pokemon);
    }
}
