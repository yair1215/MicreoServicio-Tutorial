package com.moto.service.controller;

import com.moto.service.entity.Moto;
import com.moto.service.service.MotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/moto")
public class MotoController {

    @Autowired
    private MotoService motoService;

    @GetMapping
    public ResponseEntity<List<Moto>> listarMoto() {

        List<Moto> motos = motoService.getAll();

        if (motos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(motos,HttpStatus.OK);
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<Moto> obtenerMoto(@PathVariable("id") int id){

        Optional<Moto> moto = motoService.getMotoById(id);
        if (moto.isPresent()) {
            return new ResponseEntity<>(moto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Moto>> listarMotosUserId(@PathVariable("userId") int userId){
        List<Moto> motos = motoService.byUserMotosId(userId);

        if (motos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(motos,HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<Moto> crearMoto(@RequestBody Moto moto) {
        try {
            Moto moto1 = motoService.save(moto);
            return new ResponseEntity<>(moto1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
