package cat.institutmarianao.proyectows.controller;

import cat.institutmarianao.proyectows.dto.EmailRequest;
import cat.institutmarianao.proyectows.dto.LoginRequest;
import cat.institutmarianao.proyectows.entity.Usuari;
import cat.institutmarianao.proyectows.service.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@RestController
@RequestMapping("/Usuaris")
public class UsuariController {

	private final UsuariService UsuariService;

	@Autowired
	public UsuariController(UsuariService UsuariService) {
		this.UsuariService = UsuariService;
	}

	@GetMapping
	public List<Usuari> getAllUsuaris() {
		return UsuariService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Usuari> getUsuariById(@PathVariable Integer id) {
		Optional<Usuari> Usuari = UsuariService.findById(id);
		return Usuari.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<?> createUsuari(@RequestBody Usuari usuari) {
		if (UsuariService.findByCorreu(usuari.getCorreu()).isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("El correu electrònic ja està registrat");
		}

		if (usuari.getPunts() == null) {
			usuari.setPunts(0);
		}

		// Cifrado de la contraseña
		String hashedPassword = BCrypt.hashpw(usuari.getContrasenyaHash(), BCrypt.gensalt());
		usuari.setContrasenyaHash(hashedPassword);

		Usuari created = UsuariService.save(usuari);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUsuari(@PathVariable Integer id) {
		UsuariService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    Optional<Usuari> optionalUsuari = UsuariService.findByCorreu(request.getCorreu());

    Map<String, Object> responseJson = new HashMap<>();

    if (optionalUsuari.isPresent()) {
        Usuari usuari = optionalUsuari.get();
        boolean passwordMatches = BCrypt.checkpw(request.getContrasenya(), usuari.getContrasenyaHash());

        if (passwordMatches) {
            responseJson.put("status", "success");
            responseJson.put("message", "Inicio de sesión exitoso");
            return ResponseEntity.ok(responseJson);
        } else {
            responseJson.put("status", "error");
            responseJson.put("message", "Contrasenya incorrecta");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseJson);
        }
    } else {
        responseJson.put("status", "error");
        responseJson.put("message", "Correu o contrasenya incorrectos");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseJson);
    }
}
@PostMapping("/get_user_id")
public ResponseEntity<?> getUserId(@RequestBody EmailRequest request) {
    Optional<Usuari> userOptional = UsuariService.findByCorreu(request.getCorreu());

    if (userOptional.isPresent()) {
        Usuari user = userOptional.get();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("id_usuari", user.getIdUsuari());
        return ResponseEntity.ok(response);
    } else {
        Map<String, String> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", "No s'ha trobat l'usuari");
        return ResponseEntity.status(404).body(error);
    }
}

}
