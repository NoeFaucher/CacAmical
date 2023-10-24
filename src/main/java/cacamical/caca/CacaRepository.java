package cacamical.caca;

import cacamical.user.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface CacaRepository extends JpaRepository<Caca, Long> {

}

