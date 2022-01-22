package com.kozbook.server.service;

import com.kozbook.server.entity.model.Library;
import com.kozbook.server.entity.model.User;
import com.kozbook.server.repository.LibraryRepository;
import com.kozbook.server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationException;
import java.util.List;

@Service
@Transactional
public class LibraryService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private LibraryRepository libraryRepository;

    public List<Library> listAllLibraries() {
        return libraryRepository.findAll();
    }

    public Library saveLibrary(Library library) throws ValidationException {
        return libraryRepository.save(library);
    }

    public Library getLibrary(Integer id) {
        return libraryRepository.findById(id).get();
    }

}
