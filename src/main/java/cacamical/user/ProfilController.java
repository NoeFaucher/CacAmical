package cacamical.user;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ProfilController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profil")
    public String profilePage(Model model, Principal principal) {
        String username = principal.getName();
        Optional<User> user = userRepository.findByUsername(username);
        


        if (user.isPresent()) {
            System.out.println(user.get().getDateCreation());
            model.addAttribute("user", user.get());
            return "profil";
        } else {
            return "redirect:/connexion";
        }
    }
}
