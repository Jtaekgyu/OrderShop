package com.example.myshop.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum
ErrorCode {

    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "Member name is duplicated"),
    DUPLICATED_ITEM_NAME(HttpStatus.CONFLICT, "Item name is duplicated"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"Member not founded"),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND,"Item not founded");

    private HttpStatus status;
    private String message;
}
