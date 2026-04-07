package co.edu.unbosque.entrecolbackend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.entrecolbackend.model.Empleado;
import co.edu.unbosque.entrecolbackend.model.Cargo;
import co.edu.unbosque.entrecolbackend.model.Dependencia;
import co.edu.unbosque.entrecolbackend.repository.DependenciaRepository;
import co.edu.unbosque.entrecolbackend.repository.EmpleadoRepository;

@Service
public class EmpleadoService {
	@Autowired
	private EmailService eService;

	private String codeTemp = "";
	@Autowired
	private EmpleadoRepository empleadoRepository;

	public List<Empleado> findAll() {
		return empleadoRepository.findAll();
	}

	public Optional<Empleado> findById(Long id) {
		return empleadoRepository.findByCodigo(id);
	}

	public Empleado save(Empleado emp) {
		return empleadoRepository.save(emp);
	}

	public int create(Empleado empleado) {
		if (findCodigoAlreadyTaken(empleado)) {
			return 1;
		} else {
			eService.sendWelcomeEmail(empleado.getEmail());
			empleadoRepository.save(empleado);
			return 0;
		}
	}

	public String getRecoverCode(String email, long codigo) {
		Optional<Empleado> found = empleadoRepository.findByCodigo(codigo);
		if (found.isPresent()) {
			codeTemp = eService.recoverPasswordEmail(email);
			System.out.println(codeTemp);
			return codeTemp;
		} else {
			System.out.println(codeTemp);
			return codeTemp;
		}
	}

	public int validateCredentials(long codigo, String codigoT) {
		System.out.println("Received codigo: " + codigo);
		System.out.println("Received codigo temporal: " + codigoT);
		for (Empleado e : findAll()) {
			if (e.getCodigo().equals(codigo) && codigoT.equals(codeTemp)) {
				System.out.println("Valido");
				return 0;
			}
		}
		return 1;
	}

	public boolean existsById(Long id) {
		return empleadoRepository.existsById(id);
	}

	public void deleteById(Long id) {
		empleadoRepository.deleteById(id);
	}

	public boolean findCodigoAlreadyTaken(Empleado newEmp) {
		Optional<Empleado> found = empleadoRepository.findById(newEmp.getCodigo());
		if (found.isPresent()) {
			return true;
		} else {
			return false;
		}
	}
	
	public Map<String, Integer> getEmployeeCountByDependence() {
        List<Empleado> empleados = empleadoRepository.findAll();
        Map<String, Integer> countByDependence = new HashMap<>();

        for (Empleado empleado : empleados) {
            String dependencia = empleado.getCargo().getDependencia().getNombreDependencia();
            countByDependence.put(dependencia, countByDependence.getOrDefault(dependencia, 0) + 1);
        }
        return countByDependence;
    }

    // Método para contar empleados por dependencia y cargo
    public Map<String, Map<String, Integer>> getEmployeeCountByDependenceAndCargo() {
        List<Empleado> empleados = empleadoRepository.findAll();
        Map<String, Map<String, Integer>> countByDependenceAndCargo = new HashMap<>();

        for (Empleado empleado : empleados) {
            String dependencia = empleado.getCargo().getDependencia().getNombreDependencia();
            String cargo = empleado.getCargo().getCargoNombre();

            countByDependenceAndCargo
                .computeIfAbsent(dependencia, k -> new HashMap<>())
                .put(cargo, countByDependenceAndCargo.get(dependencia).getOrDefault(cargo, 0) + 1);
        }
        return countByDependenceAndCargo;
    }

    public Map<String, Long> obtenerFrecuenciaPorEPS() {
        return empleadoRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                    empleado -> empleado.getEps().getNombreEps(),
						Collectors.counting()
                ));
    }

    public Map<String, Long> obtenerFrecuenciaPorPension() {
        return empleadoRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                    empleado -> empleado.getPension().getNombrePension(),
                    Collectors.counting()
                ));
    }

    public Map<String, Map<String, Long>> obtenerFrecuenciaPorEpsPorDependencia() {
        return empleadoRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                    empleado -> empleado.getCargo().getDependencia().getNombreDependencia(),
                    Collectors.groupingBy(
                        empleado -> empleado.getEps().getNombreEps(),
                        Collectors.counting()
                    )
                ));
    }

    public Map<String, Map<String, Long>> obtenerFrecuenciaPorPensionPorDependencia() {
        return empleadoRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                    empleado -> empleado.getCargo().getDependencia().getNombreDependencia(),
                    Collectors.groupingBy(
                        empleado -> empleado.getPension().getNombrePension(),
                        Collectors.counting()
                    )
                ));
    }

    
	public EmailService geteService() {
		return eService;
	}

	public void seteService(EmailService eService) {
		this.eService = eService;
	}

	public EmpleadoRepository getEmpleadoRepository() {
		return empleadoRepository;
	}

	public String getCodeTemp() {
		return codeTemp;
	}

	public void setCodeTemp(String codeTemp) {
		this.codeTemp = codeTemp;
	}

	public void setEmpleadoRepository(EmpleadoRepository empleadoRepository) {
		this.empleadoRepository = empleadoRepository;
	}

}
