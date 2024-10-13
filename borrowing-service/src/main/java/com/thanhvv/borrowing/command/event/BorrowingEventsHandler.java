package com.thanhvv.borrowing.command.event;

import com.thanhvv.borrowing.command.data.Borrowing;
import com.thanhvv.borrowing.command.data.BorrowingRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BorrowingEventsHandler {
    @Autowired
    private BorrowingRepository borrowingRepository;

    @EventHandler
    public void on(BorrowingCreatedEvent event) {
        Borrowing borrowing = new Borrowing();
        borrowing.setId(event.getId());
        borrowing.setBookId(event.getBookId());
        borrowing.setEmployeeId(event.getEmployeeId());
        borrowing.setBorrowDate(event.getBorrowDate());
        borrowingRepository.save(borrowing);
    }

    @EventHandler
    public void on(BorrowingDeletedEvent event) {
        Optional<Borrowing> oldBorrowing = this.borrowingRepository.findById(event.getId());
        oldBorrowing.ifPresent(borrowing -> this.borrowingRepository.delete(borrowing));
    }
}
