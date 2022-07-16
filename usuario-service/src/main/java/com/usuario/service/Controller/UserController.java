package com.usuario.service.Controller;

import com.usuario.service.entity.User;
import com.usuario.service.models.Carro;
import com.usuario.service.models.Moto;
import com.usuario.service.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> listarUser() {

        List<User> users = userService.getAll();

        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(users,HttpStatus.OK);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<User> obtenerUser(@PathVariable("id") int id){

        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallbackGetCarrosUserId")
    @GetMapping("/carros/{userId}")
    public ResponseEntity<List<Carro>> getCarrosUserId(@PathVariable("userId") int userId){


            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {
                List<Carro> carros = userService.getCarros(userId);
                return new ResponseEntity<>(carros, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

    }
    @CircuitBreaker(name = "bikesCB", fallbackMethod = "fallbackGetMotosUserId")
    @GetMapping("/motos/{userId}")
    public ResponseEntity<List<Moto>> getMotosUserId(@PathVariable("userId") int userId) {

        try {

            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {
                List<Moto> motos = userService.getMotos(userId);
                    return new ResponseEntity<>(motos, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    public ResponseEntity<User> crearUser(@RequestBody User user) {
        try {
            User user1 = userService.save(user);
            return new ResponseEntity<>(user1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @CircuitBreaker(name = "allCB", fallbackMethod = "fallbackGetUserAll")
    @GetMapping("/getall/{userId}")
    public  ResponseEntity<Map<String,Object>> getUserAll(@PathVariable("userId") int userId){

        try {

            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {

                Map<String,Object> getUserAll = userService.getUserAndVehicles(user.get().getId());
                getUserAll.put("User",user.get());
                return new ResponseEntity<>(getUserAll, HttpStatus.OK);

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallbackSaveCarro")
    @PostMapping("/savecarro/{userId}")
    public  ResponseEntity<Carro> saveCarro(@PathVariable("userId") int userId,@RequestBody Carro carro){

        try {

            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {

                Carro carroNew = userService.save(user.get().getId(),carro);
                return new ResponseEntity<>(carroNew, HttpStatus.CREATED);

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @CircuitBreaker(name = "bikesCB", fallbackMethod = "fallbackSaveMoto")
    @PostMapping("/savemoto/{userId}")
    public  ResponseEntity<Moto> saveMoto(@PathVariable("userId") int userId,@RequestBody Moto moto){

        try {

            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {

                Moto motoNew = userService.save(user.get().getId(),moto);
                return new ResponseEntity<>(motoNew, HttpStatus.CREATED);

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<List<Carro>> fallbackGetCarrosUserId(@PathVariable("userId") int userId, RuntimeException e) {
        return new ResponseEntity("El usuario " + userId + " tiene los coches en el taller", HttpStatus.OK);
    }

    private ResponseEntity<Carro> fallbackSaveCarro(@PathVariable("userId") int userId, @RequestBody Carro car, RuntimeException e) {
        return new ResponseEntity("El usuario " + userId + " no tiene dinero para coches", HttpStatus.OK);
    }

    private ResponseEntity<List<Moto>> fallbackGetMotosUserId(@PathVariable("userId") int userId, RuntimeException e) {
        return new ResponseEntity("El usuario " + userId + " tiene las motos en el taller", HttpStatus.OK);
    }

    private ResponseEntity<Moto> fallbackSaveMoto(@PathVariable("userId") int userId, @RequestBody Moto bike, RuntimeException e) {
        return new ResponseEntity("El usuario " + userId + "  no tiene dinero para motos", HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Object>> fallbackGetUserAll(@PathVariable("userId") int userId, RuntimeException e) {
        return new ResponseEntity("El usuario " + userId + " tiene los vehículos en el taller", HttpStatus.OK);
    }
}
