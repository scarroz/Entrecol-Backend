package co.edu.unbosque.entrecolbackend.dto;

import java.util.Date;

public record EmpleadoDTO(Long codigo, String nombreEmpleado, Date fecha_ingreso, String arl, Double sueldo,
		String email, Long cargo, Long pension, Long eps) {

}
