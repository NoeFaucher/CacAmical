package cacamical.caca;

import cacamical.user.User;
import cacamical.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
            return ResponseEntity.badRequest().body("Utilisateur non connecté.");
        }
    }


    @GetMapping("/getPoints")
    public ResponseEntity<List<Caca>> getPoints() {
        List<Caca> cacas = cacaRepository.findAll();
        System.out.println("Cacas récupéré avec succès !");
        return ResponseEntity.ok(cacas);
    }

    @DeleteMapping("/deletePoint/{cacaId}")
    public ResponseEntity<String> deletePoint(@PathVariable Long cacaId) {
        Optional<Caca> cacaOptional = cacaRepository.findById(cacaId);
        if (cacaOptional.isPresent()) {
            cacaRepository.delete(cacaOptional.get());
            return ResponseEntity.ok("Caca supprimé avec succès.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/editPoint/{cacaId}")
    public ResponseEntity<String> editPoint(@PathVariable Long cacaId, @RequestBody Caca updatedCaca) {
        Optional<Caca> cacaOptional = cacaRepository.findById(cacaId);
        if (cacaOptional.isPresent()) {
            Caca caca = cacaOptional.get();
            caca.setDescription(updatedCaca.getDescription());
            cacaRepository.save(caca);
            return ResponseEntity.ok("Point modifié avec succès.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}


