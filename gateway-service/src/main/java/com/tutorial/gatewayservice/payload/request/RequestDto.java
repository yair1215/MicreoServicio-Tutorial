package com.tutorial.gatewayservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RequestDto {
   private String uri;
   private String method;
}