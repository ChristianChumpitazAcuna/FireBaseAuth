package pe.edu.vallegrande.firebase.application.service;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class FireBaseService {

    public void createUser(String email, String password, String role) throws FirebaseAuthException {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            log.info("Successfully created new user: {}", userRecord.getUid());

            setUserRole(userRecord.getUid(), role);
        } catch (FirebaseAuthException e) {
            log.error("Error creating user", e);
            throw e;
        }
    }

    private void setUserRole(String uid, String role) throws FirebaseAuthException {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", "ROLE_" + role);

            FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);
        } catch (FirebaseAuthException e) {
            log.error("Error setting role", e);
            throw e;
        }
    }
}
