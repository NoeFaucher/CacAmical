package cacamical.caca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CacaController {

    @Autowired
    private CacaRepository cacaRepository;

    @PostMapping("/addPoint")
    public ResponseEntity<String> addPoint(@RequestBody Caca caca) {
        cacaRepository.save(caca);
        System.out.println("Caca ajouté avec succès!");
        return ResponseEntity.ok("Caca ajouté avec succès!");
    }

    @GetMapping("/getPoints")
    public ResponseEntity<List<Caca>> getPoints() {
        List<Caca> cacas = cacaRepository.findAll();
        System.out.println("Cacas récupéré avec succès !");
        return ResponseEntity.ok(cacas);
    }

}


