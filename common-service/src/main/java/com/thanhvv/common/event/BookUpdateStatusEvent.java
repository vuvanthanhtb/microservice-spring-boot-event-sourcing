package com.thanhvv.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdateStatusEvent {
    private String bookId;
    private Boolean isReady;
    private String employeeId;
    private String borrowingId;
}
