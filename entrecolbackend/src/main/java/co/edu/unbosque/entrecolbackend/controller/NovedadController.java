package co.edu.unbosque.entrecolbackend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

import co.edu.unbosque.entrecolbackend.model.Novedad;
import co.edu.unbosque.entrecolbackend.service.NovedadService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
//@CrossOrigin(origins = { "http://localhost:8083", "http://localhost:8082", "*" })
@RestController
@RequestMapping("/novedades")
@Transactional
public class NovedadController {

    @Autowired
    private NovedadService novedadService;

    private final Gson gson = new Gson();

    @PostMapping(path = "crearNovedad")
    public ResponseEntity<String> createNovedad(@RequestBody String novedadJson) {
        Novedad novedad = gson.fromJson(novedadJson, Novedad.class);
        Novedad createdNovedad = novedadService.save(novedad);
        return new ResponseEntity<>(gson.toJson(createdNovedad), HttpStatus.CREATED);
    }

    @GetMapping(path = "getNovedades")
    public ResponseEntity<List<Novedad>> getAllNovedades() {
        List<Novedad> novedades = novedadService.findAll();
        return new ResponseEntity<>(novedades, HttpStatus.OK);
    }

    @Operation(summary = "Trae una novedad por ID", description = "Trae la novedad correspondiente al ID proporcionado.")
    @GetMapping("/{id}")
    public ResponseEntity<String> getNovedadById(@PathVariable("id") Long id) {
        Optional<Novedad> novedad = novedadService.findById(id);
        if (novedad.isPresent()) {
            return new ResponseEntity<>(gson.toJson(novedad.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Actualizar una novedad por ID", description = "Actualiza la novedad correspondiente al ID proporcionado.")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateNovedad(
            @PathVariable("id") Long id,
            @RequestBody String novedadJson) {
        Optional<Novedad> novedadData = novedadService.findById(id);

        if (novedadData.isPresent()) {
            Novedad novedad = gson.fromJson(novedadJson, Novedad.class);
            novedad.setCodigo_novedad(id);
            Novedad updatedNovedad = novedadService.save(novedad);
            return new ResponseEntity<>(gson.toJson(updatedNovedad), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(summary = "Eliminar una novedad por ID", description = "Elimina la novedad correspondiente al ID proporcionado.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNovedad(@PathVariable("id") Long id) {
        if (novedadService.existsById(id)) {
            novedadService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
