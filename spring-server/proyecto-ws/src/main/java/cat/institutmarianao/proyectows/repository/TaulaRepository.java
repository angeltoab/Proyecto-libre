package cat.institutmarianao.proyectows.repository;

import cat.institutmarianao.proyectows.entity.Taula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaulaRepository extends JpaRepository<Taula, Integer> {
	  boolean existsById(Integer id);
}