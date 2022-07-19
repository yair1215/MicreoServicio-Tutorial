package com.tutorial.gatewayservice.security;


import com.tutorial.gatewayservice.config.JwtConfig;
import com.tutorial.gatewayservice.payload.request.JwtReqpGateway;
import com.tutorial.gatewayservice.payload.request.RequestDto;
import com.tutorial.gatewayservice.payload.response.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class AuthTokenFilter extends AbstractGatewayFilterFactory<AuthTokenFilter.Config> {

    private WebClient.Builder webClient;

    @Autowired
    private final RouterValidator routerValidator;
    private final JwtConfig jwtConfig;

    public  AuthTokenFilter(WebClient.Builder webClient, RouterValidator routerValidator, JwtConfig jwtConfig){
        super(Config.class);
        this.webClient=webClient;
        this.routerValidator = routerValidator;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
             //routerValidator.isSecured.test(exchange.getRequest()) &&
            if (!jwtConfig.isAuthDisabled()) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxx");
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                    return onError(exchange, HttpStatus.BAD_REQUEST);

                String tokenHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                String[] chunks = tokenHeader.split(" ");

                if (chunks.length != 2 || !chunks[0].equals("Bearer"))
                    return onError(exchange, HttpStatus.BAD_REQUEST);


            return webClient.build()
                    .post()
                    .uri("http://auth-service/authorize")
                    .header("Authorization", "Bearer ",tokenHeader)
                    .header("Content-Type", "application/json")
                    .bodyValue(new RequestDto(exchange.getRequest().getPath().toString(), exchange.getRequest().getMethod().toString()))
                    .retrieve()
                    .bodyToMono(JwtReqpGateway.class)
                    .map(t -> {
                        System.out.println(t.getIsauthorized());
                        t.getIsauthorized();
                        return exchange;
                    }).flatMap(chain::filter);
            }
            System.out.println("xxxxxxxxxxxxxxxxxxxxx");
        return chain.filter(exchange);
        }));

    }

    public Mono<Void> onError(ServerWebExchange exchange, HttpStatus status){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    public static class Config{


    }
    /*
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("No se puede configurar la autenticación de usuario: {}", e);
        }
        filterChain.doFilter(request, response);
    }
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }*/
}