package cat.institutmarianao.proyectows.repository;

import cat.institutmarianao.proyectows.entity.Usuari;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuariRepository extends JpaRepository<Usuari, Integer> {
	Optional<Usuari> findByCorreu(String correu);

}