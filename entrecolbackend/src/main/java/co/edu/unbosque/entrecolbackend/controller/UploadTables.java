package co.edu.unbosque.entrecolbackend.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.entrecolbackend.model.Book;
import co.edu.unbosque.entrecolbackend.model.Empleado;
import co.edu.unbosque.entrecolbackend.model.Genero;
import co.edu.unbosque.entrecolbackend.model.Movie;
import co.edu.unbosque.entrecolbackend.model.Novedad;
import co.edu.unbosque.entrecolbackend.repository.BookRepository;
import co.edu.unbosque.entrecolbackend.repository.CargoRepository;
import co.edu.unbosque.entrecolbackend.repository.DependenciaRepository;
import co.edu.unbosque.entrecolbackend.repository.EmpleadoRepository;
import co.edu.unbosque.entrecolbackend.repository.NovedadRepository;
import co.edu.unbosque.entrecolbackend.repository.PensionRepository;
import co.edu.unbosque.entrecolbackend.service.BookService;
import co.edu.unbosque.entrecolbackend.service.GeneroService;
import co.edu.unbosque.entrecolbackend.service.MovieService;
import co.edu.unbosque.entrecolbackend.util.ExcelReader;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

//@CrossOrigin(origins = { "http://localhost:8083", "http://localhost:8082", "*" })
@RestController
@Transactional
public class UploadTables {

	@Autowired
	private EmpleadoRepository eRepo;

	@Autowired
	private BookRepository bRepo;
	@Autowired
	private NovedadRepository nRepo;
	private final ExcelReader er;

	@Autowired
	public UploadTables(ExcelReader excelReader) {
		this.er = excelReader;
	}

	@Autowired
	private BookService bookService;
	@Autowired
	private MovieService mServ;

	@Autowired
	private GeneroService gServ;

//	    @GetMapping("/books/load")
//	    public ResponseEntity<String> loadBooksFromFile() {
//	        try {
//	            // Ruta del archivo JSON en el proyecto
//	            String filePath = "src/main/java/co/edu/unbosque/entrecolbackend/files/books.json";
//	            
//	            bookService.loadBooksFromJson(filePath);
//	            
//	            return ResponseEntity.status(HttpStatus.OK).body("Books loaded and saved successfully");
//	            
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load and save books");
//	        }
//	    }
	@PostMapping("/save/libros")
	public void saveAllBooks() {
		bookService.readJson("src/main/java/co/edu/unbosque/entrecolbackend/files/books.json");

	}

	public void saveBook(@Valid Book b, Model model) {
		bRepo.save(b);
	}

	@PostMapping("/save/nomina")
	public void saveEmployeesBonus(Model model) {
		ArrayList<Empleado> em = er
				.readExcelEmpleados("src/main/java/co/edu/unbosque/entrecolbackend/files/Nomina.xlsx");
		ArrayList<Novedad> nov = er
				.readExcelNovedades("src/main/java/co/edu/unbosque/entrecolbackend/files/Nomina.xlsx");

		for (int i = 0; i < em.size(); i++) {
			nov.get(i).setEmpleado(em.get(i));
			saveEmployee(em.get(i), model);
			saveNovedad(nov.get(i), model);
		}
	}

	public void saveEmployee(@Valid Empleado e, Model model) {
		eRepo.save(e);
	}

	public void saveNovedad(@Valid Novedad b, Model model) {
		nRepo.save(b);
	}

	@PostMapping("/save/movies")
	public void saveMoviesNGenders(Model model) {

		mServ.readDat("src/main/java/co/edu/unbosque/entrecolbackend/files/movies.dat");

	}

	public void saveMovie(@Valid Movie p, Model model) {
		mServ.saveMovie(p);
	}

	public void saveGender(@Valid Genero g, Model model) {
		gServ.saveGenero(g);
	}

}
