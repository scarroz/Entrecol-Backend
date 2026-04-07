package co.edu.unbosque.entrecolbackend.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.entrecolbackend.model.Novedad;
import co.edu.unbosque.entrecolbackend.repository.NovedadRepository;
import jakarta.mail.internet.ParseException;

@Service
public class NovedadService {

    @Autowired
    private NovedadRepository novedadRepository;

    public List<Novedad> findAll() {
        return novedadRepository.findAll();
    }

    public Optional<Novedad> findById(Long id) {
        return novedadRepository.findById(id);
    }

    public Novedad save(Novedad novedad) {
        return novedadRepository.save(novedad);
    }

    public void deleteById(Long id) {
        novedadRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return novedadRepository.existsById(id);
    }

    public List<Novedad> obtenerNovedadesPorFecha(String fechaInicio, String fechaFin) {
        try {
            // Convierte las fechas en formato String a Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Formato esperado de la fecha
            Date startDate = dateFormat.parse(fechaInicio);
            Date endDate = dateFormat.parse(fechaFin);

            // Obtén todas las novedades de la base de datos
            List<Novedad> todasNovedades = novedadRepository.findAll();

            // Filtra las novedades por las fechas
            List<Novedad> novedadesFiltradas = todasNovedades.stream()
                .filter(novedad -> esFechaDentroDelRango(novedad, startDate, endDate))
                .collect(Collectors.toList());

            return novedadesFiltradas;

        } catch (java.text.ParseException e) {
            // Manejo de errores si las fechas no son válidas
            e.printStackTrace();
            return new ArrayList<>(); // Retorna una lista vacía si ocurre un error en el parsing
        }
    }

    // Método auxiliar para verificar si la fecha está dentro del rango
    private boolean esFechaDentroDelRango(Novedad novedad, Date startDate, Date endDate) {
        return (esFechaEnRango(novedad.getFecha_inicio_incapacidad(), startDate, endDate) ||
                esFechaEnRango(novedad.getFecha_fin_incapacidad(), startDate, endDate) ||
                esFechaEnRango(novedad.getFecha_inicio_vacaciones(), startDate, endDate) ||
                esFechaEnRango(novedad.getFecha_fin_vacaciones(), startDate, endDate));
    }

    // Método auxiliar para verificar si una fecha está en el rango
    private boolean esFechaEnRango(Date fecha, Date startDate, Date endDate) {
        return fecha != null && !fecha.before(startDate) && !fecha.after(endDate);
    }
    
    
	public NovedadRepository getNovedadRepository() {
		return novedadRepository;
	}

	public void setNovedadRepository(NovedadRepository novedadRepository) {
		this.novedadRepository = novedadRepository;
	}
    
    
}
