package com.kozbook.server.repository;

import com.kozbook.server.entity.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Integer> {
}
