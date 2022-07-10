package com.moto.service.service;

import com.moto.service.entity.Moto;
import com.moto.service.repository.MotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MotoService {

    @Autowired
    public MotoRepository motoRepository;

    public List<Moto> getAll(){
        return motoRepository.findAll();
    }
    public Optional<Moto> getMotoById(int id){
        return motoRepository.findById(id);
    }
    public Moto save(Moto carro){
        return motoRepository.save(carro);
    }
    public List<Moto> byUserMotosId(int userId){
        return motoRepository.findByUserId(userId);

    }


}
