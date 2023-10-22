package cacamical.caca;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CacaRepository extends JpaRepository<Caca, Long> {

}

