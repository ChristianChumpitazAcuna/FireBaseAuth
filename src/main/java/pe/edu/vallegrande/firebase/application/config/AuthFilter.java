package pe.edu.vallegrande.firebase.application.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
@Slf4j
public class AuthFilter implements WebFilter {
    private static final List<String> PUBLIC_URLS = List.of(
            "/.*/public/.*"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // Allow public URLs
        if (PUBLIC_URLS.stream().anyMatch(path::matches)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("No Bearer token found in the request headers");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String idToken = authHeader.replace("Bearer ", "");

        return Mono.fromCallable(() -> FirebaseAuth.getInstance().verifyIdToken(idToken))
                .flatMap(decodedToken -> authenticateAndChain(decodedToken, exchange, chain))
                .onErrorResume(e -> handleAuthenticationError(e, exchange));
    }

    private Mono<Void> authenticateAndChain(FirebaseToken decodedToken,
                                            ServerWebExchange exchange, WebFilterChain chain) {
        String uid = decodedToken.getUid();
        String role = (String) decodedToken.getClaims().get("role");
        log.info("User {} has role {}", uid, role);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(uid, null, authorities);

        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }

    private Mono<Void> handleAuthenticationError(Throwable e, ServerWebExchange exchange) {
        log.error("Authentication error: {}", e.getMessage(), e);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
