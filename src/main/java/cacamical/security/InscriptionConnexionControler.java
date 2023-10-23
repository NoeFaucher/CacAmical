package cacamical.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cacamical.user.User;
import cacamical.user.UserService;

@Controller
public class InscriptionConnexionControler {

    @Autowired
    private UserService userService;
    
    @GetMapping("/connexion")
    public String showConnexion() {
        return "connexion";
    }

    @GetMapping("/inscription")
    public String showInscription() {
        return "inscription";
    }

    @PostMapping("/inscription")
    public ResponseEntity<String> createUser(User user) {
        // User existingUser = userService.userRepository.findByUsername(user.getUsername()).get();
        // if (existingUser != null) {
        //     return ResponseEntity.badRequest().body("L'utilisateur existe déjà.");
        // }

        user.setPassword(SecurityConfig.passwordEncoder().encode(user.getPassword()));

        userService.registerUser(user);
    
        return ResponseEntity.ok("L'utilisateur a été créé.");
    }

}
