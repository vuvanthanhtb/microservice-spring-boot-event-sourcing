package com.thanhvv.borrowing.command.saga;

import com.thanhvv.borrowing.command.commands.DeleteBorrowingCommand;
import com.thanhvv.borrowing.command.event.BorrowingCreatedEvent;
import com.thanhvv.borrowing.command.event.BorrowingDeletedEvent;
import com.thanhvv.common.command.RollbackStatusBookCommand;
import com.thanhvv.common.command.UpdateStatusBookCommand;
import com.thanhvv.common.event.BookUpdateStatusEvent;
import com.thanhvv.common.event.RollbackStatusBookEvent;
import com.thanhvv.common.model.BookResponseCommonModel;
import com.thanhvv.common.model.EmployeeResponseCommonModel;
import com.thanhvv.common.queries.GetBookDetailQuery;
import com.thanhvv.common.queries.GetDetailEmployeeQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Saga
public class BorrowingSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    private void handle(BorrowingCreatedEvent event) {
        log.info("BorrowingCreatedEvent in saga for bookId::{} & employeeId::{}", event.getBookId(), event.getEmployeeId());
        try {
            GetBookDetailQuery getBookDetailQuery = new GetBookDetailQuery(event.getBookId());
            BookResponseCommonModel book = this.queryGateway.query(getBookDetailQuery, ResponseTypes.instanceOf(BookResponseCommonModel.class)).join();

            if (!book.getIsReady()) {
                throw new Exception("Sách đã có ngưởi mượn");
            }

            SagaLifecycle.associateWith("bookId", event.getBookId());

            UpdateStatusBookCommand command = new UpdateStatusBookCommand(event.getBookId(), false, event.getEmployeeId(), event.getId());
            commandGateway.sendAndWait(command);

        } catch (Exception e) {
            log.error(e.getMessage());
            rollbackBorrowingRecord(event.getId());
        }
    }

    private void rollbackBorrowingRecord(String id) {
        log.info("rollbackBorrowingRecord in saga for borrowingId::{}", id);

        DeleteBorrowingCommand command = new DeleteBorrowingCommand(id);
        this.commandGateway.sendAndWait(command);
    }

    @SagaEventHandler(associationProperty = "bookId")
    private void handle(BookUpdateStatusEvent event) {
        log.info("BookUpdateStatusEvent in saga for bookId::{}", event.getBookId());
        try {
            GetDetailEmployeeQuery query = new GetDetailEmployeeQuery(event.getEmployeeId());
            EmployeeResponseCommonModel model = this.queryGateway.query(query, ResponseTypes.instanceOf(EmployeeResponseCommonModel.class)).join();
            if (model.getIsDisciplined()) {
                throw new Error("Nhân viên bị kỷ luật");
            } else {
                log.info("Đã mượn sách thành công");
                SagaLifecycle.end();
            }

        } catch (Exception e) {
            rollbackBookStatus(event.getBookId(), event.getEmployeeId(), event.getBorrowingId());
            log.error(e.getMessage());
        }
    }

    private void rollbackBookStatus(String bookId, String employeeId, String borrowingId) {
        SagaLifecycle.associateWith("bookId", bookId);
        RollbackStatusBookCommand command = new RollbackStatusBookCommand(bookId, true, employeeId, borrowingId);
        this.commandGateway.sendAndWait(command);
    }

    @SagaEventHandler(associationProperty = "bookId")
    private void handle(RollbackStatusBookEvent event) {
        log.info("RollbackStatusBookEvent in saga for bookId::{}", event.getBookId());
        rollbackBorrowingRecord(event.getBookId());
    }

    @SagaEventHandler(associationProperty = "bookId")
    @EndSaga
    private void handle(BorrowingDeletedEvent event) {
        log.info("BorrowingDeletedEvent in saga for Borrowing id::{}", event.getId());
        SagaLifecycle.end();
    }
}
