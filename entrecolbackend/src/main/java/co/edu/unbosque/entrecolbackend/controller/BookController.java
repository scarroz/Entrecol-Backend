package co.edu.unbosque.entrecolbackend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

import co.edu.unbosque.entrecolbackend.model.Book;
import co.edu.unbosque.entrecolbackend.model.Novedad;
import co.edu.unbosque.entrecolbackend.repository.BookRepository;
import co.edu.unbosque.entrecolbackend.service.BookService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/books")
@Transactional
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepo;

    private final Gson gson = new Gson();

    @PostMapping(path = "createBook")
    @ApiOperation(value = "Crear un nuevo libro", response = Book.class)
    public ResponseEntity<String> createBook(@RequestBody String bookJson) {
        Book book = gson.fromJson(bookJson, Book.class);
        int status = bookService.create(book);
        if (status == 0) {
            return new ResponseEntity<>(gson.toJson(book), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping(path = "getBooks")
    @ApiOperation(value = "Obtener todos los libros", response = Book.class, responseContainer = "List")
    public ResponseEntity<String> getAllBooks() {
        List<Book> books = bookService.findAll();
        return new ResponseEntity<>(gson.toJson(books), HttpStatus.OK);
    }

    @Operation(summary = "Actualiza un libro por ID", description = "Actualiza el libro correspondiente al ID proporcionado.")
    @PutMapping("/{id}")
    @ApiOperation(value = "Actualizar un libro existente", response = Book.class)
    public ResponseEntity<String> updateBook(
            @PathVariable("id") int id,
            @RequestBody String bookJson) {
        Optional<Book> bookData = bookService.findByBookID(id);
        if (bookService.existsById(bookData.get().getId())) {
			bookService.deleteById(bookData.get().getId());
		}
        if (bookData.isPresent()) {
            Book book = gson.fromJson(bookJson, Book.class);
            
            book.setBookID(id);
            Book updatedBook = bookService.save(book);
            return new ResponseEntity<>(gson.toJson(updatedBook), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
    }
    }  

    @Operation(summary = "Elimina un libro por ID", description = "Elimina el libro correspondiente al ID proporcionado.")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Eliminar un libro existente")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") int id) {
        try {
            if (bookService.findByBookID(id).isPresent()) {
                bookService.deleteByBookID(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Trae una libro por el id del libro", description = "Trae el libro correspondiente al ID proporcionado.")
    @GetMapping("/{id}")
    public ResponseEntity<String> getNovedadById(@PathVariable("id") int id) {
        Optional<Book> b = bookService.findByBookID(id);
        if (b.isPresent()) {
            return new ResponseEntity<>(gson.toJson(b.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/frecuency-by-year")
    public List<Map<String, Object>> getBookFrequencyByYear(
        @RequestParam String startDate,
        @RequestParam String endDate
    ) {
        return bookService.getBookFrequencyByYear(startDate, endDate);
    }

}
