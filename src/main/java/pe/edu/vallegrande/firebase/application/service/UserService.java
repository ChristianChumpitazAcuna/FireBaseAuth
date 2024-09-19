package pe.edu.vallegrande.firebase.application.service;

import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.firebase.domain.model.User;
import pe.edu.vallegrande.firebase.domain.repositoy.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FireBaseService fireBaseService;

    public UserService(UserRepository userRepository, FireBaseService fireBaseService) {
        this.userRepository = userRepository;
        this.fireBaseService = fireBaseService;
    }

    public Mono<User> create(User user) {
        return Mono.just(user)
                .flatMap(u ->
                        Mono.fromCallable(() -> {
                                    fireBaseService.createUser(
                                            u.getEmail(),
                                            u.getPassword(),
                                            u.getRole());
                                    return u;
                                })
                                .flatMap(userRepository::save)
                                .onErrorResume(FirebaseAuthException.class,
                                        e -> Mono.error(
                                                new RuntimeException("Failed to create user in Firebase", e)
                                        )
                                )
                );
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }


}
