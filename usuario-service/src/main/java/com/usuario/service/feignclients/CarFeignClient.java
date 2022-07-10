package com.usuario.service.feignclients;

import com.usuario.service.models.Carro;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "carro-service",url = "http://localhost:8002", path = "/carro")
public interface CarFeignClient {

    @PostMapping()
    Carro save(@RequestBody Carro carro);

    @GetMapping("/user/{userId}")
    List<Carro> getCars(@PathVariable("userId") int userId);
}
