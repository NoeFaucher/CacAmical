package cacamical.user;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class CurrentUserController {
    
    @Autowired
    UserRepository userRepository;

    @GetMapping("/curUser")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);

        return ResponseEntity.ok(userOptional.get());
    }

}
