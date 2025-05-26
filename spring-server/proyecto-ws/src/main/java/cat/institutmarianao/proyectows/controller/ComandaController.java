package cat.institutmarianao.proyectows.controller;

import cat.institutmarianao.proyectows.entity.Comanda;
import cat.institutmarianao.proyectows.service.ComandaService;
import cat.institutmarianao.proyectows.repository.ComandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comandes")
public class ComandaController {

    private final ComandaService comandaService;

    @Autowired
    private ComandaRepository comandaRepository;

    @Autowired
    public ComandaController(ComandaService comandaService) {
        this.comandaService = comandaService;
    }

    @GetMapping
    public List<Comanda> getAllComandas() {
        return comandaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comanda> getComandaById(@PathVariable Integer id) {
        Optional<Comanda> comanda = comandaService.findById(id);
        return comanda.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Comanda> createComanda(@RequestBody Comanda comanda) {
        System.out.println("Comanda rebuda: " + comanda);  // <-- LOG para comprobar si llega
        if (comanda == null || comanda.getPlat() == null || comanda.getTaula() == null || comanda.getUsuari() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Comanda createdComanda = comandaService.save(comanda);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComanda);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComanda(@PathVariable Integer id) {
        comandaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/actives")
    public List<Comanda> getActiveComandes() {
        return comandaRepository.findByEstat(Comanda.EstatComanda.ACTIVA);
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateComanda(@RequestParam("id_comanda") int idComanda) {
        comandaService.marcarComandaFinalitzada(idComanda);
        return ResponseEntity.ok().build();
    }

}
