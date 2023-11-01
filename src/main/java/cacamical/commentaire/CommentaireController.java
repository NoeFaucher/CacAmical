package cacamical.commentaire;

import cacamical.caca.Caca;
import cacamical.caca.CacaRepository;
import cacamical.user.User;
import cacamical.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class CommentaireController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentaireRepository commentaireRepository;

    @Autowired
    private CacaRepository cacaRepository;

    // Endpoint POST pour ajouter un commentaire à un point (caca)
    @PostMapping("/addComment/{cacaId}")
    public ResponseEntity<String> addComment(@PathVariable Long cacaId, @RequestBody Commentaire commentaire, Principal principal) {
        // Vérifiez si l'utilisateur est connecté
        if (principal != null) {
            String username = principal.getName();
            Optional<User> user = userRepository.findByUsername(username);
            Optional<Caca> caca = cacaRepository.findById(cacaId);

            if (user.isPresent() && caca.isPresent()) {
                // Créez un nouveau commentaire et associez-le à l'utilisateur, au point (caca) et au contenu
                Commentaire newComment = new Commentaire(user.get(), caca.get(), commentaire.getContenu(), new Date());
                commentaireRepository.save(newComment);
                return ResponseEntity.ok("Commentaire ajouté avec succès!");
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vous devez être connecté pour ajouter un commentaire.");
    }

    // Endpoint GET pour obtenir tous les commentaires d'un point (caca)
    @GetMapping("/getComments/{cacaId}")
    public ResponseEntity<List<Commentaire>> getComments(@PathVariable Long cacaId) {
        Optional<Caca> caca = cacaRepository.findById(cacaId);
        if (caca.isPresent()) {
            List<Commentaire> comments = commentaireRepository.findByCaca(caca.get());
            return ResponseEntity.ok(comments);
        }
        return ResponseEntity.notFound().build();
    }
}
