package com.carro.service.service;

import com.carro.service.entity.Carro;
import com.carro.service.repository.CarroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    public List<Carro> getAll(){
        return carroRepository.findAll();
    }
    public Optional<Carro> getCarroById(int id){
        return carroRepository.findById(id);
    }
    public Carro save(Carro carro){
        return carroRepository.save(carro);
    }

    public List<Carro> byUserCarrosId(int userId){
        return carroRepository.findByUserId(userId);

    }
}
