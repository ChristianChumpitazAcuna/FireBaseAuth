package pe.edu.vallegrande.firebase.presentation.controller;

import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.firebase.application.service.UserService;
import pe.edu.vallegrande.firebase.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/public/list")
    public Flux<User> findAll() {
        return userService.findAll();
    }

    @PostMapping("/public/create")
    public Mono<User> create(@RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping("/admin")
    public String privateResource() {
        return "This is a private resource admin";
    }

    @GetMapping("/user")
    public String userResource() {
        return "This is a private resource user";
    }
}
