package cat.institutmarianao.proyectows.repository;

import cat.institutmarianao.proyectows.entity.Comanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComandaRepository extends JpaRepository<Comanda, Integer> {
    List<Comanda> findByEstat(Comanda.EstatComanda estat);
    
    @Query("SELECT COUNT(c) > 0 FROM Comanda c WHERE c.taula.id = :idTaula")
    boolean existsByTaulaId(@Param("idTaula") Integer idTaula);
}