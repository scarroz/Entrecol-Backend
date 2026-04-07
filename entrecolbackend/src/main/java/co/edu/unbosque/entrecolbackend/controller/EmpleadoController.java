package co.edu.unbosque.entrecolbackend.controller;

import java.util.Date;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

import co.edu.unbosque.entrecolbackend.model.Cargo;
import co.edu.unbosque.entrecolbackend.model.EPS;
import co.edu.unbosque.entrecolbackend.model.Empleado;
import co.edu.unbosque.entrecolbackend.model.Pension;
import co.edu.unbosque.entrecolbackend.repository.EmpleadoRepository;
import co.edu.unbosque.entrecolbackend.service.EmpleadoService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/empleados")
@Transactional
public class EmpleadoController {

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private EmpleadoRepository empleadoRepo;

	private final Gson gson = new Gson();

	@PostMapping(path = "createEmpleado")
	@ApiOperation(value = "Crear un nuevo empleado", response = Empleado.class)
	public ResponseEntity<String> createEmpleado(@RequestBody String empleadoJson) {
		Empleado empleado = gson.fromJson(empleadoJson, Empleado.class);
		int status = empleadoService.create(empleado);
		if (status == 0) {
			return new ResponseEntity<>(gson.toJson(empleado), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}

	@Operation(summary = "Trae un empleado po id", description = "Trae un empleado por id")
	@GetMapping("/{id}")
	public ResponseEntity<String> getEmpleadoById(@PathVariable("id") Long id) {

		Optional<Empleado> e = empleadoService.findById(id);
		if (e.isPresent()) {
			return new ResponseEntity<>(gson.toJson(e.get()), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(path = "getEmpleados")
	@ApiOperation(value = "Obtener todos los empleados", response = Empleado.class, responseContainer = "List")
	public ResponseEntity<String> getAllEmpleados() {
		List<Empleado> empleados = empleadoService.findAll();
		return new ResponseEntity<>(gson.toJson(empleados), HttpStatus.OK);
	}

	@Operation(summary = "Actualiza un empleado por ID", description = "Actualiza el empleado correspondiente al ID proporcionado.")
	@PutMapping("/{id}")
	@ApiOperation(value = "Actualizar un empleado existente", response = Empleado.class)
	public ResponseEntity<String> updateEmpleado(@PathVariable("id") Long id, @RequestBody String empleadoJson) {
		Optional<Empleado> empleadoData = empleadoService.findById(id);

		if (empleadoData.isPresent()) {
			Empleado empleado = gson.fromJson(empleadoJson, Empleado.class);
			empleado.setCodigo(id);
			empleado.setCargo(empleadoData.get().getCargo());
			empleado.setPension(empleadoData.get().getPension());
			empleado.setEps(empleadoData.get().getEps());
			Empleado updatedEmpleado = empleadoService.save(empleado);
			return new ResponseEntity<>(gson.toJson(updatedEmpleado), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Elimina un empleado por ID", description = "Elimina el empleado correspondiente al ID proporcionado.")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Eliminar un empleado existente")
	public ResponseEntity<Void> deleteEmpleado(@PathVariable("id") Long id) {
		try {
			if (empleadoService.existsById(id)) {
				empleadoService.deleteById(id);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "/getlogincode")
	ResponseEntity<String> getCodeLogin(@RequestParam long codigo) {
		Optional<Empleado> aux = empleadoService.getEmpleadoRepository().findByCodigo(codigo);
		String email = aux.get().getEmail();
		return new ResponseEntity<>(empleadoService.getRecoverCode(email, codigo), HttpStatus.ACCEPTED);
	}

	@PostMapping(path = "/checklogin")
	ResponseEntity<String> checkLogin(@RequestParam long codigo, @RequestParam String codeTemp) {
		int status = empleadoService.validateCredentials(codigo, codeTemp);
		if (status == 0) {
			return new ResponseEntity<>("Correct credentials", HttpStatus.ACCEPTED);

		} else {
			return new ResponseEntity<>("Invalid code", HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("/dependence-count")
	public ResponseEntity<Map<String, Integer>> getEmployeeCountByDependence() {
		Map<String, Integer> countByDependence = empleadoService.getEmployeeCountByDependence();
		return new ResponseEntity<>(countByDependence, HttpStatus.OK);
	}

	@GetMapping("/dependence-cargo-count")
	public ResponseEntity<Map<String, Map<String, Integer>>> getEmployeeCountByDependenceAndCargo() {
		Map<String, Map<String, Integer>> countByDependenceAndCargo = empleadoService
				.getEmployeeCountByDependenceAndCargo();
		return new ResponseEntity<>(countByDependenceAndCargo, HttpStatus.OK);
	}
	
	@GetMapping("/frecuencia-eps")
	public ResponseEntity<Map<String, Long>> obtenerFrecuenciaPorEPS() {
	    Map<String, Long> frecuenciaEPS = empleadoService.obtenerFrecuenciaPorEPS();
	    return ResponseEntity.ok(frecuenciaEPS);
	}
	
	@GetMapping("/frecuencia-pension")
	public ResponseEntity<Map<String, Long>> obtenerFrecuenciaPorPension() {
	    Map<String, Long> frecuenciaPension = empleadoService.obtenerFrecuenciaPorPension();
	    return ResponseEntity.ok(frecuenciaPension);
	}
	
	@GetMapping("/frecuencia-eps-dependencia")
	public ResponseEntity<Map<String, Map<String, Long>>> obtenerFrecuenciaPorEpsPorDependencia() {
	    Map<String, Map<String, Long>> frecuenciaEpsDependencia = empleadoService.obtenerFrecuenciaPorEpsPorDependencia();
	    return ResponseEntity.ok(frecuenciaEpsDependencia);
	}

	@GetMapping("/frecuencia-pension-dependencia")
	public ResponseEntity<Map<String, Map<String, Long>>> obtenerFrecuenciaPorPensionPorDependencia() {
	    Map<String, Map<String, Long>> frecuenciaPensionDependencia = empleadoService.obtenerFrecuenciaPorPensionPorDependencia();
	    return ResponseEntity.ok(frecuenciaPensionDependencia);
	}


}