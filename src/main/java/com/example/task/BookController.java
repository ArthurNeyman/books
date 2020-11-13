package com.example.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "books")
public class BookController {

    @Autowired
    private BookDao bookDao;

    @GetMapping("/getBookList")
    public ResponseEntity getBookList() {
        try {

            List<Book> list = bookDao.getAll().stream().sorted((o1, o2) ->
                    o2.getTitle().compareTo(o1.getTitle())).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(list);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("List of books was not received");

        }
    }

    @PostMapping("/addBook")
    public ResponseEntity addBook(@RequestBody Book book) {

        try {

            book = bookDao.save(book);

            return ResponseEntity.status(HttpStatus.OK).body(book);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Book is not attached");

        }
    }

    @GetMapping("/getBookListByAuthorGroup")
    public ResponseEntity getBookListByAuthorGroup() {

        Map<String, List<Book>> map = new HashMap<>();

        try {
            bookDao.getAll().forEach(el -> {

                if (map.get(el.getAuthor()) == null)
                    map.put(el.getAuthor(), Stream.of(el).collect(Collectors.toList()));
                else
                    map.get(el.getAuthor()).add(el);

            });

            return ResponseEntity.status(HttpStatus.OK).body(map);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("List of books was not received");
        }
    }
}
