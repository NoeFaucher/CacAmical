package cacamical.user;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.annotation.JsonValueInstantiator;


@Controller
public class UserController {
    
    @Autowired
    UserRepository userRepository;

    /** Get endpoint to get the curent user connected ()
     * 
     */
    @GetMapping("/curUser")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userOptional.get());
    }

    /* Get endpoint to get all the existing users
     * 
     */
    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getPoints() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }


    /*
     * 
     */
    @PostMapping("/addFriend")
    public ResponseEntity<String> addFriend(Principal principal, @RequestBody Map<String,Long> body) {
        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Utilisateur non connecté.");
        }


        Long friendId = body.get("friendId");

        Optional<User> friendOptional = userRepository.findById(friendId);

        // check if the Id of the firend is an existing user
        if (!friendOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Utilisateur n'existe pas.");
        }


        User user = userOptional.get();
        User friend = friendOptional.get();

        if (user.getUserId() == friend.getUserId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Utilisateur ne peut pas être ami avec lui-même.");
        }


        user.addAmi(friend);

        userRepository.save(user);

        return ResponseEntity.ok("Ami ajouté");
    }

    @DeleteMapping("/removeFriend")
    public ResponseEntity<String> removeFriend(Principal principal, @RequestBody Map<String,Long> body) {
        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Utilisateur non connecté.");
        }


        Long friendId = body.get("friendId");

        Optional<User> friendOptional = userRepository.findById(friendId);

        // check if the Id of the firend is an existing user
        if (!friendOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Utilisateur n'existe pas.");
        }


        User user = userOptional.get();
        User friend = friendOptional.get();

        user.removeAmi(friend);

        userRepository.save(user);
        
        return ResponseEntity.ok("Ami supprimé");
    }
 
}
