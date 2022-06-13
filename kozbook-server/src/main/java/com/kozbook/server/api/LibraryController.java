package com.kozbook.server.api;

import com.kozbook.server.entity.model.Library;
import com.kozbook.server.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/library")
public class LibraryController {
    @Autowired
    LibraryService libraryService;
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("")
    public List<Library> list() {
        return libraryService.listAllLibraries();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Library> get(@PathVariable Integer id) {
        try {
            Library library = libraryService.getLibrary(id);
            return new ResponseEntity<>(library, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public ResponseEntity<Library> add(@RequestBody Library library) {
        try{
            Library saved_library = libraryService.saveLibrary(library);
            return new ResponseEntity<>(saved_library, HttpStatus.OK);
        } catch (ValidationException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Library library, @PathVariable Integer id) {
        try {
            Library existLibrary = libraryService.getLibrary(id);
            library.setId_library(id);
            libraryService.saveLibrary(library);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException | ValidationException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
