package cat.institutmarianao.proyectows.controller;

import cat.institutmarianao.proyectows.entity.Plat;
import cat.institutmarianao.proyectows.entity.Plat.Recomanacio;
import cat.institutmarianao.proyectows.service.PlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/plats")
public class PlatController {

    private final PlatService platService;

    @Autowired
    public PlatController(PlatService platService) {
        this.platService = platService;
    }

    @GetMapping
    public List<Plat> getAllPlats() {
        return platService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plat> getPlatById(@PathVariable Integer id) {
        Optional<Plat> plat = platService.findById(id);
        return plat.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/createPlat")
    public ResponseEntity<Plat> createPlat(@RequestBody Plat plat) {
        Plat createdPlat = platService.save(plat);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlat(@PathVariable Integer id) {
        platService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/updatePlat/{id}")
    public ResponseEntity<Plat> updatePlat(@PathVariable Integer id, @RequestBody Plat platDetails) {
        Optional<Plat> platOptional = platService.findById(id);
        
        if (platOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Plat existingPlat = platOptional.get();
        
        // Actualiza los campos del plato existente con los nuevos valores
        existingPlat.setNom(platDetails.getNom());
        existingPlat.setDescripcio(platDetails.getDescripcio());
        existingPlat.setCategoria(platDetails.getCategoria());
        existingPlat.setRecomanacio(platDetails.getRecomanacio());
        existingPlat.setPreu(platDetails.getPreu());
        existingPlat.setPuntuacio(platDetails.getPuntuacio());
        
        Plat updatedPlat = platService.save(existingPlat);
        return ResponseEntity.ok(updatedPlat);
    }
    @PutMapping("/updateRecommendation/{id}")
    public ResponseEntity<Plat> updateRecommendation(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
        Optional<Plat> platOptional = platService.findById(id);
        
        if (platOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Plat existingPlat = platOptional.get();
        String recomanacio = payload.get("recomanacio");
        
        if (recomanacio != null && (recomanacio.equals("Si") || recomanacio.equals("No"))) {
            existingPlat.setRecomanacio(Recomanacio.valueOf(recomanacio));
            Plat updatedPlat = platService.save(existingPlat);
            return ResponseEntity.ok(updatedPlat);
        }
        
        return ResponseEntity.badRequest().build();
    }

}
