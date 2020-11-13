package com.example.task;

import java.util.List;

public interface BookDao {

    List<Book> getAll();
    Book save(Book book);
}
