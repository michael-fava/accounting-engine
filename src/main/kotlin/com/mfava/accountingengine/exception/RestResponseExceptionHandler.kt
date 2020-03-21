package com.mfava.accountingengine.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class RestResponseExceptionHandler {

    @ResponseBody
    @ExceptionHandler(BaseException::class)
    fun handleBaseException(ex: BaseException): ResponseEntity<Error> {
        return ResponseEntity(Error(ex.errorCode, ex.message!!), ex.errorCode.httpStatus)
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBaseException(ex: IllegalArgumentException): ResponseEntity<Error> {
        return ResponseEntity(Error(ErrorCode.ILLEGAL_ARGUMENT_IN_REQUEST, ex.message!!), ErrorCode.ILLEGAL_ARGUMENT_IN_REQUEST.httpStatus)
    }
}
