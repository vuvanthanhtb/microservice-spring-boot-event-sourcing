package com.thanhvv.book_service.command.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequestModel {
    private String id;
    @NotBlank(message = "Name is mandatory")
    @Size(min=2, max=30, message = "Name must be between 2 and 30 characters")
    private String name;
    @NotBlank(message = "Authors is mandatory")
    private String author;
    private Boolean isReady;
}
