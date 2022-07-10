package com.usuario.service.Controller;

import com.usuario.service.entity.User;
import com.usuario.service.models.Carro;
import com.usuario.service.models.Moto;
import com.usuario.service.service.UserService;
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
    @GetMapping("/carros/{userId}")
    public ResponseEntity<List<Carro>> getCarrosUserId(@PathVariable("userId") int userId){

        List<Carro> carros = userService.getCarros(userId);

        if (carros.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(carros,HttpStatus.OK);
        }
    }
    @GetMapping("/motos/{userId}")
    public ResponseEntity<List<Moto>> getMotosUserId(@PathVariable("userId") int userId){

        List<Moto> motos = userService.getMotos(userId);

        if (motos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(motos,HttpStatus.OK);
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
    @GetMapping("/getall/{userId}")
    public  ResponseEntity<Map<String,Object>> getUserAll(@PathVariable("userId") int userId, @RequestBody Moto moto){

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
}
