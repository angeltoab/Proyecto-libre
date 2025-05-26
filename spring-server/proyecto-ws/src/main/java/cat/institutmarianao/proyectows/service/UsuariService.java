package cat.institutmarianao.proyectows.service;

import cat.institutmarianao.proyectows.entity.Usuari;
import cat.institutmarianao.proyectows.repository.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuariService {

    private final UsuariRepository UsuariRepository;

    @Autowired
    public UsuariService(UsuariRepository UsuariRepository) {
        this.UsuariRepository = UsuariRepository;
    }

    public List<Usuari> findAll() {
        return UsuariRepository.findAll();
    }

    public Optional<Usuari> findById(Integer id) {
        return UsuariRepository.findById(id);
    }

    public Usuari save(Usuari Usuari) {
        return UsuariRepository.save(Usuari);
    }

    public void deleteById(Integer id) {
        UsuariRepository.deleteById(id);
    }
    public Optional<Usuari> findByCorreu(String correu) {
        return UsuariRepository.findByCorreu(correu);
    }
}
