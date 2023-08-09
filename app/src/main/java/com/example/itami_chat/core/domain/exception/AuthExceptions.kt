package com.example.itami_chat.core.domain.exception

data object EmptyEmailFieldException : Exception()

data object EmptyPasswordFieldException : Exception()

data object EmptyConfirmPasswordFieldException : Exception()

data object ShortPasswordException : Exception()

data object PasswordsDoNotMatchException : Exception()

data object InvalidEmailException : Exception()

data object InvalidPasswordException : Exception()

data object UserAlreadyExistsException : Exception()

data object UserDoesNotExistException : Exception()

data object UserNotActiveException : Exception()