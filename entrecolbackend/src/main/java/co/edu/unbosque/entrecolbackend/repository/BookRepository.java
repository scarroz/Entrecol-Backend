package co.edu.unbosque.entrecolbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.entrecolbackend.model.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
	public List<Book> findAll();
	public Optional<Book> findByBookID(int bookID);
	public void deleteByBookID(int bookID);
}
