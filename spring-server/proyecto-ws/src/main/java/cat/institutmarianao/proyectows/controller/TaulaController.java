package cat.institutmarianao.proyectows.controller;

import cat.institutmarianao.proyectows.entity.Taula;
import cat.institutmarianao.proyectows.service.TaulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/Taulas")
public class TaulaController {

    private final TaulaService taulaService;

    @Autowired
    public TaulaController(TaulaService taulaService) {
        this.taulaService = taulaService;
    }

    @GetMapping
    public List<Taula> getAllTaulas() {
        return taulaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taula> getTaulaById(@PathVariable Integer id) {
        Optional<Taula> taula = taulaService.findById(id);
        return taula.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Taula> createTaula(@RequestBody Taula taula) {
        Taula createdTaula = taulaService.save(taula);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaula);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaula(@PathVariable Integer id) {
        if (taulaService.hasOrders(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        taulaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/existeix")
    public ResponseEntity<String> existeixTaula(@RequestParam("id_taula") Integer idTaula) {
        boolean existeix = taulaService.existsById(idTaula);
        return ResponseEntity.ok(existeix ? "true" : "false");
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Taula> updateTaula(@PathVariable Integer id, @RequestBody Taula taulaDetails) {
        Optional<Taula> taulaOptional = taulaService.findById(id);
        
        if (taulaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Taula existingTaula = taulaOptional.get();
        existingTaula.setCapacitat(taulaDetails.getCapacitat());
        
        Taula updatedTaula = taulaService.save(existingTaula);
        return ResponseEntity.ok(updatedTaula);
    }
    @GetMapping("/{id}/hasOrders")
    public ResponseEntity<Map<String, Boolean>> hasPedidos(@PathVariable Integer id) {
        boolean hasOrders = taulaService.hasOrders(id);
        return ResponseEntity.ok(Collections.singletonMap("hasOrders", hasOrders));
    }
}