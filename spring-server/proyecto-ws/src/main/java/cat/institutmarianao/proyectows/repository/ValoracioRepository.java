package cat.institutmarianao.proyectows.repository;

import cat.institutmarianao.proyectows.entity.Valoracio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValoracioRepository extends JpaRepository<Valoracio, Integer> {
    // Agrega métodos personalizados si es necesario
}