package com.kozbook.server.service;

import com.kozbook.server.repository.BookRepository;
import com.kozbook.server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private BookRepository bookRepository;
}
