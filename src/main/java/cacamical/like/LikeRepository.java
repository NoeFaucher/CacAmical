package cacamical.like;

import cacamical.caca.Caca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, Long> {

    //Fonction qui a l'aide de SQL récupère le nombre de likes par "points"
    @Query("SELECT COUNT(l) FROM Like l WHERE l.caca = :caca")
    Integer countLikesByCaca(@Param("caca") Caca caca);
}
