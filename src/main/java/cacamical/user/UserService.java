package cacamical.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
 
    @Autowired
    public UserRepository userRepository;
    
    public User registerUser(User u) {
        return userRepository.save(u);
    }

}
