package com.sparkmind.demo.repository;

import com.sparkmind.demo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
    // Tìm sách dựa trên mã ISBN
    Optional<Book> findByIsbn(String isbn);

    // Kiểm tra ISBN tồn tại hay không
    boolean existsByIsbn(String isbn);

    // Tìm danh sách dựa trên tên sách
    List<Book> findByTitleContainingIgnoreCase(String titleKeyword);

    // Tìm danh sách dựa trên tên tác giả
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // Tìm danh sách dựa trên thể loại
    List<Book> findByGenreContainingIgnoreCase(String genre);

    // Tìm được thêm bởi admin (admin_Id?)
    List<Book> findByAddedByAdmin_UserId(Integer adminId);

    // Tìm các sách có sẵn
    @Query("SELECT b FROM Book b WHERE b.availableQuantity > 0")
    List<Book> findAvailableBooks();
}