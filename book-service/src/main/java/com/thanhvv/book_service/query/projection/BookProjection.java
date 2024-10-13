package com.thanhvv.book_service.query.projection;

import com.thanhvv.book_service.command.data.Book;
import com.thanhvv.book_service.command.data.BookRepository;
import com.thanhvv.common.model.BookResponseCommonModel;
import com.thanhvv.book_service.query.queries.GetAllBookQuery;
import com.thanhvv.common.queries.GetBookDetailQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookProjection {
    @Autowired
    private BookRepository bookRepository;

    @QueryHandler
    public List<BookResponseCommonModel> handle(GetAllBookQuery query) {
        List<Book> books = bookRepository.findAll();

        return books.stream().map(book -> {
            BookResponseCommonModel bookResponseCommonModel = new BookResponseCommonModel();
            BeanUtils.copyProperties(book, bookResponseCommonModel);
            return bookResponseCommonModel;
        }).toList();
    }

    @QueryHandler
    public BookResponseCommonModel handle(GetBookDetailQuery query) {
        BookResponseCommonModel bookResponseCommonModel = new BookResponseCommonModel();
        bookRepository.findById(query.getId()).ifPresent(book -> {
            bookResponseCommonModel.setId(book.getId());
            bookResponseCommonModel.setName(book.getName());
            bookResponseCommonModel.setAuthor(book.getAuthor());
           bookResponseCommonModel.setIsReady(book.getIsReady());
        });

        return bookResponseCommonModel;

    }
}
