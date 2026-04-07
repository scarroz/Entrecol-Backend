package co.edu.unbosque.entrecolbackend.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Optional;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.unbosque.entrecolbackend.model.Cargo;
import co.edu.unbosque.entrecolbackend.model.Dependencia;
import co.edu.unbosque.entrecolbackend.model.EPS;
import co.edu.unbosque.entrecolbackend.model.Empleado;
import co.edu.unbosque.entrecolbackend.model.Novedad;
import co.edu.unbosque.entrecolbackend.model.Pension;
import co.edu.unbosque.entrecolbackend.repository.CargoRepository;
import co.edu.unbosque.entrecolbackend.repository.DependenciaRepository;
import co.edu.unbosque.entrecolbackend.repository.EmpleadoRepository;
import co.edu.unbosque.entrecolbackend.repository.EpsRepository;
import co.edu.unbosque.entrecolbackend.repository.PensionRepository;

@Component
public class ExcelReader {

	private final DependenciaRepository dependenciaRepo;
	private final CargoRepository cargoRepo;
	private final PensionRepository pensionRepo;
	private final EpsRepository epsRepo;

	@Autowired
	public ExcelReader(DependenciaRepository dependenciaRepo, CargoRepository cargoRepo, PensionRepository pensionRepo,
			EpsRepository epsRepo) {
		this.dependenciaRepo = dependenciaRepo;
		this.cargoRepo = cargoRepo;
		this.pensionRepo = pensionRepo;
		this.epsRepo = epsRepo;
	}

	// Método modificado en ExcelReader para leer el cargo con la dependencia correcta

	public ArrayList<Empleado> readExcelEmpleados(String file) {
	    ArrayList<Empleado> empleados = new ArrayList<>();
	    try (FileInputStream fis = new FileInputStream(file); XSSFWorkbook myFile = new XSSFWorkbook(fis)) {

	        XSSFSheet sheet = myFile.getSheetAt(0);
	        XSSFRow row;
	        XSSFCell cell;

	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	            Empleado aux = new Empleado();
	            row = sheet.getRow(i);
	            if (row == null)
	                continue;

	            String dependenciaNombre = null;
	            String cargoNombre = null;

	            for (int j = 0; j < row.getLastCellNum(); j++) {
	                cell = row.getCell(j);
	                if (cell == null)
	                    continue;
	                
	                switch (j) {
	                    case 0:
	                        aux.setCodigo(Long.parseLong(cell.toString().split("\\.")[0]));
	                        break;
	                    case 1:
	                        aux.setNombreEmpleado(cell.toString());
	                        break;
	                    case 2:
	                    	dependenciaNombre = cell.toString();
	                         break;
	                    case 3:
	                    	cargoNombre = cell.toString();
	                        Cargo cargo = obtenerCargo(dependenciaNombre, cargoNombre);
	                        aux.setCargo(cargo);
	                      
	                    case 4:
	                        Date fechaIngreso = convertirStringADate(cell);
	                        if (fechaIngreso != null && isFechaValida(fechaIngreso)) {
	                            aux.setFecha_ingreso(fechaIngreso);
	                        } else {
	                            System.err.println("Fecha no válida: " + cell);
	                            continue;
	                        }
	                        break;
	                    case 5:
	                        aux.setEps(obtenerEPS(cell.toString()));
	                        break;
	                    case 6:
	                        aux.setArl(cell.toString());
	                        break;
	                    case 7:
	                        aux.setPension(obtenerPension(cell.toString()));
	                        break;
	                    case 8:
	                        aux.setSueldo(Double.parseDouble(cell.toString()));
	                        break;
	                    case 9:
	                        aux.setEmail(cell.toString());
	                        break;
	                    default:
	                        break;
	                }
	            }
	            empleados.add(aux);
	        }

	    } catch (IOException ex) {
	        System.err.println("Error leyendo el archivo de Excel: " + ex.getMessage());
	    } catch (NumberFormatException ex) {
	        System.err.println("Error de formato en uno de los datos numéricos: " + ex.getMessage());
	    }
	    return empleados;
	}

	private Cargo obtenerCargo(String nombreDependencia, String nombreCargo) {
	    Dependencia dependencia = obtenerDependencia(nombreDependencia);
	    Optional<Cargo> cargoOpt = cargoRepo.findByCargoNombreAndDependencia(nombreCargo, dependencia);

	    return cargoOpt.orElseGet(() -> {
	        Cargo nuevoCargo = new Cargo();
	        nuevoCargo.setCargoNombre(nombreCargo);
	        nuevoCargo.setDependencia(dependencia);
	        return cargoRepo.save(nuevoCargo);
	    });
	}

	public ArrayList<Novedad> readExcelNovedades(String file) {
		ArrayList<Novedad> novedades = new ArrayList<>();
		try (FileInputStream fis = new FileInputStream(file); XSSFWorkbook myFile = new XSSFWorkbook(fis)) {

			XSSFSheet sheet = myFile.getSheetAt(1);
			XSSFRow row;
			XSSFCell cell;

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Novedad novedad = new Novedad();
				row = sheet.getRow(i);
				if (row == null)
					continue;

				for (int j = 0; j < row.getLastCellNum(); j++) {
					cell = row.getCell(j);
					if (cell == null)
						continue;

					switch (j) {
					case 0:
						novedad.setCodigo_novedad((long) i);
						break;
					case 1:
						novedad.setNovedad_incapacidad("X".equals(cell.toString()) ? 1 : 0);
						break;
					case 2:
						novedad.setNovedad_vacaciones("X".equals(cell.toString()) ? 1 : 0);
						break;
					case 3:
						novedad.setDias_trabajados((int) Double.parseDouble(cell.toString()));
						break;
					case 4:
						novedad.setDias_incapacidad((int) Double.parseDouble(cell.toString()));
						break;
					case 5:
						novedad.setDias_vacaciones((int) Double.parseDouble(cell.toString()));
						break;
					case 6:
						novedad.setFecha_inicio_vacaciones(
								cell.toString().isBlank() ? null : convertirStringADate(cell));
						break;
					case 7:
						novedad.setFecha_fin_vacaciones(cell.toString().isBlank() ? null : convertirStringADate(cell));
						break;
					case 8:
						novedad.setFecha_inicio_incapacidad(
								cell.toString().isBlank() ? null : convertirStringADate(cell));
						break;
					case 9:
						novedad.setFecha_fin_incapacidad(cell.toString().isBlank() ? null : convertirStringADate(cell));
						break;
					case 10:
						novedad.setBonificacion(Float.parseFloat(cell.toString()));
						break;
					case 11:
						novedad.setTransporte(Float.parseFloat(cell.toString()));
						break;
					default:
						break;
					}
				}
				novedades.add(novedad);
			}

		} catch (IOException ex) {
			System.err.println("Error leyendo el archivo de Excel: " + ex.getMessage());
		} catch (NumberFormatException ex) {
			System.err.println("Error de formato en uno de los datos numéricos: " + ex.getMessage());
		}
		return novedades;
	}

	public static Date convertirStringADate(XSSFCell cell) {
		Date fecha = null;
		try {
			if (cell.getCellType() == CellType.NUMERIC) {
				if (DateUtil.isCellDateFormatted(cell)) {
					fecha = cell.getDateCellValue();
				} else {
					String valorCadena = String.valueOf((long) cell.getNumericCellValue());
					fecha = convertirCadenaADate(valorCadena);
				}
			} else if (cell.getCellType() == CellType.STRING) {
				String valorCadena = cell.getStringCellValue().trim();
				fecha = convertirCadenaADate(valorCadena);
			}
		} catch (ParseException | IllegalArgumentException e) {
			System.err.println("Error al convertir la fecha: " + e.getMessage());
		}

		return fecha;
	}

	private static Date convertirCadenaADate(String valorCadena) throws ParseException {
		SimpleDateFormat formatoFecha;

		if (valorCadena.length() == 8) {
			String año = valorCadena.substring(0, 4);
			String mes = valorCadena.substring(4, 6);
			String dia = valorCadena.substring(6, 8);
			String fechaFormateada = año + "/" + mes + "/" + dia;
			formatoFecha = new SimpleDateFormat("yyyy/MM/dd");
			return formatoFecha.parse(fechaFormateada);
		} else if (valorCadena.length() == 10 && valorCadena.contains("/")) {
			formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
			return formatoFecha.parse(valorCadena);
		} else {
			throw new IllegalArgumentException("Formato de fecha no reconocido: " + valorCadena);
		}
	}

	private boolean isFechaValida(Date fecha) {
		try {
			Date fechaMinima = new SimpleDateFormat("yyyy-MM-dd").parse("1000-01-01");
			Date fechaMaxima = new SimpleDateFormat("yyyy-MM-dd").parse("9999-12-31");
			return !fecha.before(fechaMinima) && !fecha.after(fechaMaxima);
		} catch (ParseException e) {
			return false;
		}
	}

	private Dependencia obtenerDependencia(String nombreDependencia) {
		Optional<Dependencia> dependenciaOpt = dependenciaRepo.findByNombreDependencia(nombreDependencia);
		return dependenciaOpt.orElseGet(() -> {
			Dependencia nuevaDependencia = new Dependencia();
			nuevaDependencia.setNombreDependencia(nombreDependencia);
			return dependenciaRepo.save(nuevaDependencia);
		});
	}

//	private Cargo obtenerCargo(String nombreCargo) {
//		Optional<Cargo> cargoOpt = cargoRepo.findByCargoNombre(nombreCargo);
//		return cargoOpt.orElseGet(() -> {
//			Cargo nuevoCargo = new Cargo();
//			nuevoCargo.setCargoNombre(nombreCargo);
//			return cargoRepo.save(nuevoCargo);
//		});
//	}

	private Pension obtenerPension(String nombrePension) {
		Optional<Pension> pensionOpt = pensionRepo.findByNombrePension(nombrePension);
		return pensionOpt.orElseGet(() -> {
			Pension nuevaPension = new Pension();
			nuevaPension.setNombrePension(nombrePension);
			return pensionRepo.save(nuevaPension);
		});
	}

	private EPS obtenerEPS(String nombreEps) {
		Optional<EPS> epsOpt = epsRepo.findByNombreEps(nombreEps);
		return epsOpt.orElseGet(() -> {
			EPS nuevaEPS = new EPS();
			nuevaEPS.setNombreEps(nombreEps);
			return epsRepo.save(nuevaEPS);
		});
	}
}
