package com.tutorial.authservice.controllers;

import com.tutorial.authservice.payload.request.RequestDto;
import com.tutorial.authservice.payload.response.JwtRespGateway;
import com.tutorial.authservice.security.services.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/authorize")
public class TestController {

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }


    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public void userAccess(){}

    @PostMapping("/user")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public void userAccesspost(){}

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/authorize")
    public ResponseEntity<?> authenticateValidator(@Valid @RequestBody RequestDto RequestDto) {
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
