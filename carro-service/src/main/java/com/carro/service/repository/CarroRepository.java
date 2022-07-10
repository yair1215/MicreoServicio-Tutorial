package com.carro.service.repository;

import com.carro.service.entity.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarroRepository extends JpaRepository<Carro,Integer> {

    public List<Carro> findByUserId(int userId);
}
