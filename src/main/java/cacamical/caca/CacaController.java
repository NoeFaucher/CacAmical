package cacamical.caca;

import cacamical.user.User;
import cacamical.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    public ResponseEntity<String> addPoint(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("caca") String cacaJson, Principal principal) throws JsonProcessingException {
        // Vous pouvez extraire les informations du point (caca) du JSON
        // Par exemple, en utilisant ObjectMappercd  de Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        Caca caca = objectMapper.readValue(cacaJson, Caca.class);

        // Vérifiez si l'utilisateur est connecté
        if (principal != null) {
            String username = principal.getName();
            Optional<User> user = userRepository.findByUsername(username);

            if (user.isPresent()) {
                // Vérifiez si un fichier d'image a été fourni
                if (file != null && !file.isEmpty()) {
                    try {
                        // Stockez le fichier de l'image dans un répertoire sur le serveur
                        String fileName = "uploadImg/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
                        String uploadDir = "src/main/resources/static/img/";
                        File uploadPath = new File(uploadDir);
                        if (!uploadPath.exists()) {
                            uploadPath.mkdirs();
                        }
                        String filePath = uploadPath.getAbsolutePath() + File.separator + fileName;
                        file.transferTo(new File(filePath));

                        // Enregistrez le chemin du fichier de l'image dans la base de données
                        caca.setPhotoPath(fileName);
                    } catch (IOException e) {
                        return ResponseEntity.badRequest().body("Erreur lors du téléchargement de l'image.");
                    }
                }

                // Associez le point à l'utilisateur
                caca.setUser(user.get());

                // Enregistrez le point dans la base de données
                cacaRepository.save(caca);

                return ResponseEntity.ok("Point ajouté avec succès!");
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vous devez être connecté pour ajouter un point.");
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


