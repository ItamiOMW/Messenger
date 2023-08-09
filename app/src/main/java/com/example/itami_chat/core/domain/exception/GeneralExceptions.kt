package com.example.itami_chat.core.domain.exception

data object ConflictException : Exception()

data object ForbiddenException : Exception()

data object InvalidVerificationCodeException : Exception()

data object NotFoundException : Exception()

data object PoorNetworkConnectionException : Exception()

data object ServerErrorException : Exception()

data object UnauthorizedException : Exception()