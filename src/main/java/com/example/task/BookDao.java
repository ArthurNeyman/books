package com.example.task;

import java.util.List;

public interface BookDao {

    List<Book> findAll();
    Book save(Book book);
}
