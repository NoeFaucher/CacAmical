package cacamical.caca;

import cacamical.user.User;
import cacamical.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class CacaController {

    @Autowired
    private CacaRepository cacaRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/addPoint")
    public ResponseEntity<String> addPoint(@RequestBody Caca caca, Principal principal) {
        // Vérifiez si l'utilisateur est connecté
        if (principal != null) {
            String username = principal.getName();
            // Recherchez l'utilisateur par son nom d'utilisateur (username)
            Optional<User> userOptional = userRepository.findByUsername(username);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                // Associez l'utilisateur au point Caca
                caca.setUser(user);
                cacaRepository.save(caca);
                System.out.println("Caca ajouté avec succès!");
                return ResponseEntity.ok("Caca ajouté avec succès!");
            } else {
                return ResponseEntity.badRequest().body("Utilisateur introuvable.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Utilisateur non connecté.");
        }
    }


    @GetMapping("/getPoints")
    public ResponseEntity<List<Caca>> getPoints() {
        List<Caca> cacas = cacaRepository.findAll();
        System.out.println("Cacas récupéré avec succès !");
        return ResponseEntity.ok(cacas);
    }

    @DeleteMapping("/deletePoint/{cacaId}")
    public ResponseEntity<String> deletePoint(@PathVariable Long cacaId, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Optional<Caca> cacaOptional = cacaRepository.findById(cacaId);

            if (cacaOptional.isPresent()) {
                Caca caca = cacaOptional.get();

                // Vérifiez si l'utilisateur actuel est le propriétaire du point
                if (caca.getUser() != null && caca.getUser().getUsername().equals(username)) {
                    cacaRepository.delete(caca);
                    return ResponseEntity.ok("Caca supprimé avec succès.");
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'êtes pas autorisé à supprimer ce point.");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Utilisateur non connecté.");
        }
    }


    @PutMapping("/editPoint/{cacaId}")
    public ResponseEntity<String> editPoint(@PathVariable Long cacaId, @RequestBody Caca updatedCaca, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Optional<Caca> cacaOptional = cacaRepository.findById(cacaId);

            if (cacaOptional.isPresent()) {
                Caca caca = cacaOptional.get();

                // Vérifiez si l'utilisateur actuel est le propriétaire du point
                if (caca.getUser() != null && caca.getUser().getUsername().equals(username)) {
                    caca.setDescription(updatedCaca.getDescription());
                    cacaRepository.save(caca);
                    return ResponseEntity.ok("Point modifié avec succès.");
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'êtes pas autorisé à modifier ce point.");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Utilisateur non connecté.");
        }
    }


}


