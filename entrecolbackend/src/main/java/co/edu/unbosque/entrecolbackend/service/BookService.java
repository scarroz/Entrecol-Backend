package co.edu.unbosque.entrecolbackend.service;

import java.io.FileReader;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;

import co.edu.unbosque.entrecolbackend.dto.BookDTO;
import co.edu.unbosque.entrecolbackend.model.Book;
import co.edu.unbosque.entrecolbackend.repository.BookRepository;

@Service
public class BookService {
	@Autowired
	private BookRepository bookRepository;

	public List<Book> findAll() {
		return bookRepository.findAll();
	}

	public Optional<Book> findById(Long id) {
		return bookRepository.findById(id);
	}

	public Book save(Book book) {
		return bookRepository.save(book);
	}

	public int create(Book book) {
		if (findCodigoAlreadyTaken(book)) {
			return 1;
		} else {

			bookRepository.save(book);
			return 0;
		}
	}

	public Optional<Book> findByBookID(int ID) {
		return bookRepository.findByBookID(ID);
	}

	public void deleteByBookID(int id) {
		Optional<Book> b = bookRepository.findByBookID(id);
		if (b.isPresent()) {
			bookRepository.delete(b.get());
		}

	}

	public boolean existsById(Long id) {
		return bookRepository.existsById(id);
	}

	public void deleteById(Long id) {
		bookRepository.deleteById(id);
	}

	public boolean findCodigoAlreadyTaken(Book b) {
		Optional<Book> found = bookRepository.findByBookID(b.getBookID());
		if (found.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

	public List<Book> readJson(String file) {
		List<Book> libros = Collections.synchronizedList(new ArrayList<Book>());
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			List<Book> books = objectMapper.readValue(new File(file), new TypeReference<List<Book>>() {
			});

			for (Book book : books) {
				try {
					String autores = book.getAuthors().length() <= 40 ? book.getAuthors()
							: book.getAuthors().substring(0, 39);
					Book nuevoLibro = new Book((Integer) book.getBookID(), book.getTitle(), autores,
							book.getAverage_rating(), book.getIsbn(), book.getIsbn13(), book.getLanguage_code(),
							book.getNum_pages(), book.getRatings_count(), book.getText_reviews_count(),
							book.getPublication_Date(), book.getPublisher());

					libros.add(nuevoLibro);

					Thread saverThread = new Thread(() -> {
						try {
							bookRepository.save(nuevoLibro);
							System.out.println("Libro guardado: " + nuevoLibro.getTitle());
						} catch (Exception e) {
							System.out.println("Error al guardar el libro: " + nuevoLibro.getTitle());
							e.printStackTrace();
						}
					});
					saverThread.start();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			System.out.println("Falla en la lectura del Json");
			e.printStackTrace();
		}
		return libros;
	}

	private static final int NUM_THREADS = 4; // Número de hilos a utilizar

	public BookRepository getBookRepository() {
		return bookRepository;
	}

	public void setBookRepository(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public static int getNumThreads() {
		return NUM_THREADS;
	}

	public List<Map<String, Object>> getBookFrequencyByYear(String startDate, String endDate) {
	    // Validar que las fechas no estén vacías
	    if (startDate == null || startDate.trim().isEmpty() || endDate == null || endDate.trim().isEmpty()) {
	        throw new IllegalArgumentException("Ambas fechas (inicio y fin) son obligatorias.");
	    }

	    SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
	    Date start;
	    Date end;

	    try {
	        // Convertir fechas de entrada
	        start = inputFormat.parse(startDate);
	        end = inputFormat.parse(endDate);
	    } catch (ParseException e) {
	        throw new IllegalArgumentException("Fechas inválidas: " + e.getMessage());
	    }

	    // Filtrar y agrupar libros por año
	    return bookRepository.findAll().stream()
	        .filter(book -> {
	            try {
	                if (book.getPublication_Date() == null) return false;

	                Date publicationDate = inputFormat.parse(book.getPublication_Date());
	                return !publicationDate.before(start) && !publicationDate.after(end);
	            } catch (ParseException e) {
	                System.err.println("Fecha malformada para el libro: " + book.getTitle());
	                return false;
	            }
	        })
	        .collect(Collectors.groupingBy(
	            book -> {
	                try {
	                    Date publicationDate = inputFormat.parse(book.getPublication_Date());
	                    Calendar cal = Calendar.getInstance();
	                    cal.setTime(publicationDate);
	                    return String.valueOf(cal.get(Calendar.YEAR));
	                } catch (ParseException e) {
	                    return "Unknown";
	                }
	            },
	            Collectors.counting()
	        ))
	        .entrySet().stream()
	        .map(entry -> {
	            Map<String, Object> result = new HashMap<>();
	            result.put("year", entry.getKey());
	            result.put("count", entry.getValue());
	            return result;
	        })
	        .collect(Collectors.toList());
	}

}
