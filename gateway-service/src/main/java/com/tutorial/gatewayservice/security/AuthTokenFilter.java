package com.tutorial.gatewayservice.security;


import com.tutorial.gatewayservice.config.JwtConfig;
import com.tutorial.gatewayservice.payload.request.JwtReqpGateway;
import com.tutorial.gatewayservice.payload.request.RequestDto;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Component
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

             /*routerValidator.isSecured.test(exchange.getRequest()) &&
            if (!jwtConfig.isAuthDisabled()) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxx 1");
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                    return onError(exchange, HttpStatus.BAD_REQUEST);

                String tokenHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                System.out.println(tokenHeader);
                String[] chunks = tokenHeader.split(" ");

                if (chunks.length != 2 || !chunks[0].equals("Bearer"))
                    return onError(exchange, HttpStatus.BAD_REQUEST);


                String tokenHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                Object requestBody = webClient.build()
                        .post()
                        .uri("http://auth-service/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",tokenHeader)
                        //   .header("Content-Type", "application/json")
                        .bodyValue(new RequestDto(exchange.getRequest().getPath().toString(), exchange.getRequest().getMethod().toString()))
                        .retrieve()
                        .onStatus(HttpStatus::isError, clientResponse -> {
                            return Mono.error(new Exception("error"));
                        })
                        .bodyToMono(String.class);

               // return onError(exchange, HttpStatus.UNAUTHORIZED);
        return chain.filter(exchange);

              */
            TcpClient tcpClient = TcpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .doOnConnected(connection ->
                            connection.addHandlerLast(new ReadTimeoutHandler(3))
                                    .addHandlerLast(new WriteTimeoutHandler(3)));

            WebClient webClient = WebClient.builder()
                    .baseUrl("http://localhost:8080")
                    .defaultHeaders(headers -> headers.putAll(exchange.getRequest().getHeaders()))
                   // .defaultHeader("Authorization",tokenHeader)
                    //.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    //.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))  // timeout
                    .build();
    try {
           /*.method(exchange.getRequest().getMethod())
              JwtReqpGateway jwtReqpGateway = webClient.post()
                    .uri("/api/auth/authorize")
                    .body(Mono.just(new RequestDto(exchange.getRequest().getPath().toString(), exchange.getRequest().getMethod().toString())), RequestDto.class)
                    .retrieve()
                    // handle status
                    .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                     //   logger.error("Error endpoint with status code {}",  clientResponse.statusCode());
                        return Mono.error(new Exception("HTTP Status 500 error"));
                    })
                    .bodyToMono(JwtReqpGateway.class)
                     .toFuture()
                     .get();

              if (jwtReqpGateway.getIsauthorized()) {
                  return chain.filter(exchange);
                    }else {
                  return this.onError(exchange, HttpStatus.UNAUTHORIZED);

              }

                String path = exchange.getRequest().getPath().toString();
                Mono<Void> jwtReqpGateway= webClient.method(exchange.getRequest().getMethod())
                .uri("/api/authorize"+path)
                .retrieve()
                .bodyToMono(Void.class);*/

             // Mono<HttpStatus> httpStatusMono = webClient.get()
               // .uri("/api/authorize/user")
                 //       .accept(MediaType.APPLICATION_JSON)
                   //     .exchange()
                     //   .map(response ->  response.statusCode());

               Mono<JwtReqpGateway> jwtReqpGateway = webClient.post()
                .uri("/api/authorize"+exchange.getRequest().getPath())
                .retrieve()
                .bodyToMono(JwtReqpGateway.class);

        return chain.filter(exchange);
             /*
             .uri("http://auth-service/api/authorize/user")
                .headers(headers -> headers.putAll(exchange.getRequest().getHeaders()))
              JwtReqpGateway jwtReqpGateway1 = jwtReqpGateway.share().block();
                    if (jwtReqpGateway.getIsauthorized()) {
                        return chain.filter(exchange);
                    }else {
                        return this.onError(exchange, HttpStatus.UNAUTHORIZED);

                    }*/

                        //.onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                            //   logger.error("Error endpoint with status code {}",  clientResponse.statusCode());

                           //  return Mono.error(new Exception("HTTP Status 500 error"));
                        // })
                        //.onStatus(status -> status.value() == HttpStatus.METHOD_NOT_ALLOWED.value(),
                        // response -> Mono.error(new Exception("Method not allowed. Please check the URL.", response.statusCode().value())));
                        //.bodyToMono(HttpStatus.class)
                        //.block();

                } catch  (Exception e) {
                    //HttpStatus status = e.getStatusCode();
                    return this.onError(exchange, HttpStatus.UNAUTHORIZED);}
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