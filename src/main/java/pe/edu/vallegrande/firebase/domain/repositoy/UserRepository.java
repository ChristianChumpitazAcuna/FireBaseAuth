package pe.edu.vallegrande.firebase.domain.repositoy;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.firebase.domain.model.User;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
