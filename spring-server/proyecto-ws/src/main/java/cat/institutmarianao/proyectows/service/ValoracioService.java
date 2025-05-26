package cat.institutmarianao.proyectows.service;

import cat.institutmarianao.proyectows.entity.Valoracio;
import cat.institutmarianao.proyectows.repository.ValoracioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValoracioService {

    private final ValoracioRepository ValoracioRepository;

    @Autowired
    public ValoracioService(ValoracioRepository ValoracioRepository) {
        this.ValoracioRepository = ValoracioRepository;
    }

    public List<Valoracio> findAll() {
        return ValoracioRepository.findAll();
    }

    public Optional<Valoracio> findById(Integer id) {
        return ValoracioRepository.findById(id);
    }

    public Valoracio save(Valoracio Valoracio) {
        return ValoracioRepository.save(Valoracio);
    }

    public void deleteById(Integer id) {
        ValoracioRepository.deleteById(id);
    }
}
