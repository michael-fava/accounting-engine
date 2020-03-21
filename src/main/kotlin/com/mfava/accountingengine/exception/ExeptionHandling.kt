package com.mfava.accountingengine.exception

import java.io.Serializable
import java.lang.Exception

class BaseException(val errorCode: ErrorCode, val msg: String) : Exception(msg)

class Error(val errorCode: ErrorCode, val message: String) : Serializable
