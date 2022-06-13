package com.kozbook.server.repository;

import com.kozbook.server.entity.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
