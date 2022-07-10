package com.carro.service.controller;

import com.carro.service.entity.Carro;
import com.carro.service.service.CarroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/carro")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @GetMapping
    public ResponseEntity<List<Carro>> listarCarro() {

        List<Carro> carros = carroService.getAll();

        if (carros.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(carros,HttpStatus.OK);
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<Carro> obtenerCarro(@PathVariable("id") int id){

        Optional<Carro> carro = carroService.getCarroById(id);
        if (carro.isPresent()) {
            return new ResponseEntity<>(carro.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Carro>> listarCarroUserId(@PathVariable("userId") int userId){
        List<Carro> carros = carroService.byUserCarrosId(userId);

        if (carros.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(carros,HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<Carro> crearCarro(@RequestBody Carro carro) {
        try {
            Carro carro1 = carroService.save(carro);
            return new ResponseEntity<>(carro1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
