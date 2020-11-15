package com.example.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public final class JdbcBookDao implements BookDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Book> findAll()  {

        List<Book> list = new ArrayList<>();

        jdbcTemplate.queryForList("select * from book")
                .forEach(book -> {
                    list.add(new Book((Long) book.get("id"), (String) book.get("author"),
                            (String) book.get("title"), (String) book.get("description")));
                });

        return list;
    }

    public Book save(Book book) {

        if (book.getId() == null) {

            Long id = jdbcTemplate.queryForObject("select max(id) from book", Long.class);

            id = id == null ? 0 : id + 1;

            book.setId(id);

            jdbcTemplate.update("insert into book(id,author,title,description) values(?,?,?,?)",
                    book.getId(), book.getAuthor(), book.getTitle(), book.getDescription());

        } else {

            jdbcTemplate.update("update  book set author=? , title=? , description=? where id= ?",
                    book.getAuthor(), book.getTitle(), book.getDescription(), book.getId());
        }

        return book;
    }
}
