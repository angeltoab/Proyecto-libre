package cat.institutmarianao.proyectows.service;

import cat.institutmarianao.proyectows.entity.Comanda;
import cat.institutmarianao.proyectows.repository.ComandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComandaService {

    private final ComandaRepository comandaRepository;

    @Autowired
    public ComandaService(ComandaRepository comandaRepository) {
        this.comandaRepository = comandaRepository;
    }

    public List<Comanda> findAll() {
        return comandaRepository.findAll();
    }

    public Optional<Comanda> findById(Integer id) {
        return comandaRepository.findById(id);
    }

    public Comanda save(Comanda comanda) {
        return comandaRepository.save(comanda);
    }

    public void deleteById(Integer id) {
        comandaRepository.deleteById(id);
    }
    public void marcarComandaFinalitzada(int idComanda) {
        Optional<Comanda> optional = comandaRepository.findById(idComanda);
        if (optional.isPresent()) {
            Comanda comanda = optional.get();
            comanda.setEstat(Comanda.EstatComanda.FINALITZADA);
            comandaRepository.save(comanda);
        }
    }
}
