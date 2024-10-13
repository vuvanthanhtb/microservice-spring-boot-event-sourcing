package com.thanhvv.book_service.query.controller;

import com.thanhvv.common.model.BookResponseCommonModel;
import com.thanhvv.book_service.query.queries.GetAllBookQuery;
import com.thanhvv.common.queries.GetBookDetailQuery;
import com.thanhvv.common.services.KafkaService;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookQueryController {
    @Autowired
    private QueryGateway queryGateway;

    @Autowired
    private KafkaService kafkaService;

    @GetMapping
    public List<BookResponseCommonModel> getAllBooks() {
        kafkaService.sendMessage("test", "GetAllBookQuery");

        GetAllBookQuery getAllBookQuery = new GetAllBookQuery();
        return queryGateway.query(getAllBookQuery, ResponseTypes.multipleInstancesOf(BookResponseCommonModel.class)).join();
    }

    @GetMapping("{bookId}")
    public BookResponseCommonModel getBookById(@PathVariable("bookId") String bookId) {
        GetBookDetailQuery query = new GetBookDetailQuery(bookId);
        return queryGateway.query(query, ResponseTypes.instanceOf(BookResponseCommonModel.class)).join();
    }
}
