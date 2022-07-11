package com.usuario.service.service;

import com.usuario.service.entity.User;
import com.usuario.service.feignclients.BikeFeignClient;
import com.usuario.service.feignclients.CarFeignClient;
import com.usuario.service.models.Carro;
import com.usuario.service.models.Moto;

import com.usuario.service.repository.UserRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CarFeignClient carFeignClient;

    @Autowired
    BikeFeignClient bikeFeignClient;

    public List<Carro> getCarros(int userId){
        List<Carro> carros = restTemplate.getForObject("http://carro-service/carro/user/"+userId,List.class);
        return carros;
    }
    public List<Moto> getMotos(int userId){
        List<Moto> motos = restTemplate.getForObject("http://moto-service/moto/user/"+userId,List.class);
        return motos;
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }
    public Optional<User> getUserById(int id){
        return userRepository.findById(id);
    }
    public User save(User user){
        return userRepository.save(user);
    }

    public Carro save(int userId,Carro carro){
        carro.setUserId(userId);
        Carro carroNew = carFeignClient.save(carro);
        return carroNew;
    }
    public Moto save(int userId,Moto moto){
        moto.setUserId(userId);
        Moto motoNew = bikeFeignClient.save(moto);
        return motoNew;
    }

    public Map<String,Object> getUserAndVehicles(int userId){
        Map<String,Object> result = new HashMap<>();
       /* User user = userRepository.findById(userId).orElse(null);
       if(user == null){
           result.put("Mesaje","No existe usuario");
           return result;
       }*/
        try {
            List<Carro> carros = carFeignClient.getCars(userId);
            if(!carros.isEmpty()) {
                result.put("Cars", carros);
            }
        }catch (Exception e) {
            result.put("Cars","El user no tiene carros");
        }

        try {
            List<Moto> motos = bikeFeignClient.getBike(userId);
            if(!motos.isEmpty()) {
                result.put("Bikes", motos);
            }
        }catch (Exception e) {
            result.put("Bikes", "El user no tiene motos");
        }

        return result;
    }
}
