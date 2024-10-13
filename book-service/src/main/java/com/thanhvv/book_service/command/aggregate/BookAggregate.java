package com.thanhvv.book_service.command.aggregate;

import com.thanhvv.book_service.command.commands.CreateBookCommand;
import com.thanhvv.book_service.command.commands.DeleteBookCommand;
import com.thanhvv.book_service.command.commands.UpdateBookCommand;
import com.thanhvv.book_service.command.event.BookCreatedEvent;
import com.thanhvv.book_service.command.event.BookDeletedEvent;
import com.thanhvv.book_service.command.event.BookUpdatedEvent;
import com.thanhvv.common.command.RollbackStatusBookCommand;
import com.thanhvv.common.command.UpdateStatusBookCommand;
import com.thanhvv.common.event.BookUpdateStatusEvent;
import com.thanhvv.common.event.RollbackStatusBookEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@NoArgsConstructor
public class BookAggregate {
    @AggregateIdentifier
    private String id;
    private String name;
    private String author;
    private Boolean isReady;

    @CommandHandler
    public BookAggregate(CreateBookCommand command) {
        BookCreatedEvent event = new BookCreatedEvent();
        BeanUtils.copyProperties(command, event);

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(UpdateBookCommand command) {
        BookUpdatedEvent event = new BookUpdatedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(DeleteBookCommand command) {
        DeleteBookCommand deleteCommand = new DeleteBookCommand();
        BeanUtils.copyProperties(command, deleteCommand);
        AggregateLifecycle.apply(deleteCommand);
    }

    @CommandHandler
    public void handle(UpdateStatusBookCommand command) {
        BookUpdateStatusEvent event = new BookUpdateStatusEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(RollbackStatusBookCommand command) {
        RollbackStatusBookEvent event = new RollbackStatusBookEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(BookCreatedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.author = event.getAuthor();
        this.isReady = event.getIsReady();
    }

    @EventSourcingHandler
    public void on(BookUpdatedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.author = event.getAuthor();
        this.isReady = event.getIsReady();
    }

    @EventSourcingHandler
    public void on(BookDeletedEvent event) {
        this.id = event.getId();
    }

    @EventSourcingHandler
    public void on(BookUpdateStatusEvent event) {
        this.id = event.getBookId();
        this.isReady = event.getIsReady();
    }

    @EventSourcingHandler
    public void on(RollbackStatusBookEvent event) {
        this.id = event.getBookId();
        this.isReady = event.getIsReady();
    }
}
