package cat.institutmarianao.proyectows.repository;

import cat.institutmarianao.proyectows.entity.Plat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface PlatRepository extends JpaRepository<Plat, Integer> {
}