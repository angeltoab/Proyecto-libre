package cat.institutmarianao.proyectows.service;

import cat.institutmarianao.proyectows.entity.Taula;
import cat.institutmarianao.proyectows.repository.TaulaRepository;
import cat.institutmarianao.proyectows.repository.ComandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaulaService {

    private final TaulaRepository taulaRepository;
    private final ComandaRepository comandaRepository;

    @Autowired
    public TaulaService(TaulaRepository taulaRepository, ComandaRepository comandaRepository) {
        this.taulaRepository = taulaRepository;
        this.comandaRepository = comandaRepository;
    }

    public List<Taula> findAll() {
        return taulaRepository.findAll();
    }

    public Optional<Taula> findById(Integer id) {
        return taulaRepository.findById(id);
    }

    public Taula save(Taula taula) {
        return taulaRepository.save(taula);
    }

    public void deleteById(Integer id) {
        taulaRepository.deleteById(id);
    }
    
    public boolean existsById(Integer id) {
        return taulaRepository.existsById(id);
    }
    
    public boolean hasOrders(Integer idTaula) {
        return comandaRepository.existsByTaulaId(idTaula);
    }
}