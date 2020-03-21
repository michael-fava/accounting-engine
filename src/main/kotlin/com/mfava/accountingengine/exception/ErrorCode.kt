package com.mfava.accountingengine.exception

import org.springframework.http.HttpStatus

enum class ErrorCode (val httpStatus: HttpStatus){
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND),
    ACCOUNT_PIN_MISMATCH(HttpStatus.BAD_REQUEST),
    INSUFFICIENT_FUNDS(HttpStatus.BAD_REQUEST),
    ILLEGAL_ARGUMENT_IN_REQUEST(HttpStatus.BAD_REQUEST),
    TRANSACTION_NOT_FOUND(HttpStatus.BAD_REQUEST)
}
