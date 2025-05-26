package cat.institutmarianao.proyectows.service;

import cat.institutmarianao.proyectows.entity.Plat;
import cat.institutmarianao.proyectows.repository.PlatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlatService {

    private final PlatRepository platRepository;

    @Autowired
    public PlatService(PlatRepository platRepository) {
        this.platRepository = platRepository;
    }

    public List<Plat> findAll() {
        return platRepository.findAll();
    }

    public Optional<Plat> findById(Integer id) {
        return platRepository.findById(id);
    }

    public Plat save(Plat plat) {
        return platRepository.save(plat);
    }

    public void deleteById(Integer id) {
        platRepository.deleteById(id);
    }
}
