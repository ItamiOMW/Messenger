package com.example.itami_chat.core.data.remote

enum class ExceptionCode(val code: String) {

    BadRequest("400"),
    MissingArguments("400.1"),
    InvalidVerificationCode("400.3"),
    InvalidPasswordResetCode("400.4"),
    InvalidEmail("400.5"),
    InvalidPassword("400.6"),

    Unauthorized("401"),

    Forbidden("403"),
    UserNotActive("403.1"),
    PasswordResetNotAllowed("403.2"),

    NotFound("404"),
    UserDoesNotExist("404.1"),

    Conflict("409"),
    UserAlreadyExists("409.1"),

    ServerError("500");



}