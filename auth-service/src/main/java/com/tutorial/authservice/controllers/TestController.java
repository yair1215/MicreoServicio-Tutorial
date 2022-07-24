package com.tutorial.authservice.controllers;

import com.tutorial.authservice.payload.request.RequestDto;
import com.tutorial.authservice.payload.response.JwtRespGateway;
import com.tutorial.authservice.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/authorize")
public class TestController {


    UserDetailsImpl userDetails;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }


    @PostMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> userAccess(){

        userDetails  = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        if(AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("ROLE_ADMIN")){

        }
        boolean authorized = false;// = authorities.contains(new SimpleGrantedAuthority("ROLE_USER"));
        return ResponseEntity.ok(new JwtRespGateway(authorized));
    }

    @PostMapping("/user")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> userAccesspost(){
        boolean authorized = false;// = authorities.contains(new SimpleGrantedAuthority("ROLE_USER"));
        return ResponseEntity.ok(new JwtRespGateway(authorized));
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public void moderatorAccess() { }

    @PostMapping("/mod")
    @PreAuthorize("hasRole('ADMIN')")
    public void adminAccesspost() {}

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

}
