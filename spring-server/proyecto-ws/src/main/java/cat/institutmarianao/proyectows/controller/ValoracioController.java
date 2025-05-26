package cat.institutmarianao.proyectows.controller;

import cat.institutmarianao.proyectows.entity.Valoracio;
import cat.institutmarianao.proyectows.service.ValoracioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Valoracios")
public class ValoracioController {

    private final ValoracioService ValoracioService;

    @Autowired
    public ValoracioController(ValoracioService ValoracioService) {
        this.ValoracioService = ValoracioService;
    }

    @GetMapping
    public List<Valoracio> getAllValoracios() {
        return ValoracioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Valoracio> getValoracioById(@PathVariable Integer id) {
        Optional<Valoracio> Valoracio = ValoracioService.findById(id);
        return Valoracio.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Valoracio> createValoracio(@RequestBody Valoracio Valoracio) {
        Valoracio createdValoracio = ValoracioService.save(Valoracio);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdValoracio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteValoracio(@PathVariable Integer id) {
        ValoracioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
