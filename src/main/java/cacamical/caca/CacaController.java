package cacamical.caca;

import cacamical.user.User;
import cacamical.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class CacaController {

    @Autowired
    private CacaRepository cacaRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/addPoint")
    public ResponseEntity<String> addPoint(@RequestParam("file") MultipartFile file, @RequestParam("caca") String cacaJson, Principal principal) {
        // Vérifiez si l'utilisateur est connecté
        if (principal != null) {
            try {
                // Convertissez le JSON de caca en objet Caca
                ObjectMapper objectMapper = new ObjectMapper();
                Caca caca = objectMapper.readValue(cacaJson, Caca.class);

                // Vérifiez si un fichier a été téléchargé
                if (file != null && !file.isEmpty()) {
                    // Obtenez le chemin du répertoire où vous souhaitez stocker les fichiers
                    String uploadDirectory = "/resources/static/img/uploadImg/";

                    // Générez un nom de fichier unique pour éviter les conflits
                    String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

                    // Créez le chemin complet du fichier
                    String filePath = uploadDirectory + fileName;

                    // Enregistrez le fichier sur le serveur
                    Files.write(Paths.get(filePath), file.getBytes());

                    // Stockez le chemin du fichier dans l'objet Caca
                    caca.setPhotoPath(filePath);
                }
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
                }
                else {
                    return ResponseEntity.badRequest().body("Utilisateur introuvable.");
                }

            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Erreur lors de l'ajout du point.");
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


