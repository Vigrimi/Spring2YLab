package com.edu.ulab.app.service;


import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.web.request.UserBookRequest;

import java.util.List;

public interface BookService {
    BookDto createBook(BookDto bookDto);

    void updateBookById(BookDto bookDto);

    BookDto getBookById(Long id);

    void deleteBookById(Long id);

    void deleteAllBooksByUserId(Long userId);

    List<Long> getListBooksIdsByUserIdCheckedAndGotFmDB(Long userId);

    List<Book> getAllBooksByUserId(Long userId);

    List<Long> getListBooksIdsForUserAndAddBooksInRepo(UserBookRequest userBookRequest, UserDto createdUser);
}
