package cacamical.caca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacaController {

    @Autowired
    private CacaRepository cacaRepository;

    @PostMapping("/addPoint")
    public String addPoint(Double longitude, Double latitude, String description) {
        System.out.println(longitude);
        System.out.println(latitude);
        System.out.println(description);
        Caca caca = new Caca();
        caca.setLongitude(longitude);
        caca.setLatitude(latitude);
        caca.setDescription(description);
        cacaRepository.save(caca);
        return "Point ajouté avec succès!";
    }
}

