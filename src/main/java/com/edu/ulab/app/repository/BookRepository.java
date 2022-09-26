package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.Book;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select b from Book b where b.id = :id")
    Optional<Book> findByIdForUpdate(long id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query(name = "select b from Book b where b.USER_ID = :id", nativeQuery = true)
    List<Book> findAllByUserId(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(name = "DELETE from Book b where b.USER_ID = :id", nativeQuery = true)
    void deleteAllBooksByUserId(Long id);

    @Modifying
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "UPDATE Book b SET " +
            "b.TITLE = :title AND b.AUTHOR = :author AND b.PAGE_COUNT = :pageCount " +
            "WHERE (ID = :id)", nativeQuery = true)
    void updateBookById(Long id, String title, String author, long pageCount);
}
