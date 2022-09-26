package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.web.request.UserBookRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BookServiceImplTemplate implements BookService {
    private final BookMapper bookMapper;
    private final JdbcTemplate jdbcTemplate;

    public BookServiceImplTemplate(BookMapper bookMapper, JdbcTemplate jdbcTemplate) {
        this.bookMapper = bookMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    private PreparedStatement getPreparedStatementForBook(Connection connection,
                                                          String SQLExpression, BookDto bookDto) throws SQLException {
        PreparedStatement ps =
                connection.prepareStatement(SQLExpression, new String[]{"id"});
        ps.setString(1, bookDto.getTitle());
        ps.setString(2, bookDto.getAuthor());
        ps.setLong(3, bookDto.getPageCount());
        ps.setLong(4, bookDto.getUserId());
        return ps;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        log.info("createBook begins - bookDto: {}", bookDto);
        final String INSERT_SQL = "INSERT INTO BOOK(TITLE, AUTHOR, PAGE_COUNT, USER_ID) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        return getPreparedStatementForBook(connection, INSERT_SQL, bookDto);
                    }
                },
                keyHolder);

        bookDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.info("createBook finished - bookDto: {}", bookDto);
        return bookDto;
    }

    @Override
    public void updateBookById(BookDto bookDto) {
        // реализовать недстающие методы
    }

    @Override
    public BookDto getBookById(Long id) {
        // реализовать недстающие методы
        return null;
    }

    @Override
    public void deleteBookById(Long id) {
        // реализовать недстающие методы
    }

    @Override
    public void deleteAllBooksByUserId(Long userId) {

    }

    @Override
    public List<Long> getListBooksIdsByUserIdCheckedAndGotFmDB(Long userId) {
        return null;
    }

    @Override
    public List<Book> getAllBooksByUserId(Long userId) {
        return null;
    }

    @Override
    public List<Long> getListBooksIdsForUserAndAddBooksInRepo(UserBookRequest userBookRequest, UserDto createdUser) {
        return userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(createdUser.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(this::createBook) // repo save
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
    }
}
