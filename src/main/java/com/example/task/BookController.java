package com.example.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/books")
public class BookController {

    @Autowired
    private BookDao bookDao;

    @GetMapping("")
    public ResponseEntity getBookList() {

        List<Book> list;

        try {

            list = bookDao.findAll().stream().sorted((book1, book2) ->
                    book2.getTitle().compareTo(book1.getTitle())).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(list);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("List of books was not received");

        }
    }

    @PostMapping("")
    public ResponseEntity addBook(@RequestBody Book book) {

        try {

            return ResponseEntity.status(HttpStatus.OK).body(bookDao.save(book));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Book is not attached");

        }
    }

    @GetMapping("/authors")
    public ResponseEntity getBookListByAuthorGroup() {

        Map<String, List<Book>> map = new HashMap<>();

        try {
            for(Book book : bookDao.findAll())
                if (map.get(book.getAuthor()) == null)
                    map.put(book.getAuthor(), new ArrayList<Book>(Arrays.asList(book)));
                else
                    map.get(book.getAuthor()).add(book);

/*
            bookDao.findAll().forEach(book -> {

                if (map.get(book.getAuthor()) == null)
                    map.put(book.getAuthor(), new ArrayList<Book>(Arrays.asList(book)));
                else
                    map.get(book.getAuthor()).add(book);
            });
*/
            return ResponseEntity.status(HttpStatus.OK).body(map);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("List of books was not received");
        }
    }
}
