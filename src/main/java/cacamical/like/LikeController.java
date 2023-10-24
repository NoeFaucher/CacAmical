package cacamical.like;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import cacamical.caca.Caca;
import cacamical.caca.CacaRepository;
import cacamical.user.User;
import cacamical.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Controller
public class LikeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CacaRepository cacaRepository;

    @PostMapping("/addLike/{cacaId}")
    public ResponseEntity<String> addLike(@PathVariable Long cacaId, Principal principal) {
        // Vérifiez si l'utilisateur est connecté
        if (principal != null) {
            String username = principal.getName();
            Optional<User> user = userRepository.findByUsername(username);
            Optional<Caca> caca = cacaRepository.findById(cacaId);

            if (user.isPresent() && caca.isPresent()) {
                // Créez un nouveau like et associez-le à l'utilisateur et au point (caca)
                Like newLike = new Like(user.get(), caca.get(), new Date());
                likeRepository.save(newLike);
                return ResponseEntity.ok("Like ajouté avec succès!");
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vous devez être connecté pour aimer un point.");
    }

    @GetMapping("/getLikes/{cacaId}")
    public ResponseEntity<Integer> getLikesCount(@PathVariable Long cacaId) {
        Optional<Caca> caca = cacaRepository.findById(cacaId);
        if (caca.isPresent()) {
            Integer likeCount = likeRepository.countLikesByCaca(caca.get());
            System.out.println("Like Récupérés" + likeCount);
            return ResponseEntity.ok(likeCount);
        }
        return ResponseEntity.notFound().build();
    }

}
