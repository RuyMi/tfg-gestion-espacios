package es.dam.validator

import es.dam.dto.UserRegisterDTO

fun UserRegisterDTO.validateCampos(): Boolean {
    return this.username.isNotEmpty() && this.password.isNotEmpty() && this.email.isNotEmpty() && this.name.isNotEmpty()
            && this.username.isNotBlank() && this.password.isNotBlank() && this.email.isNotBlank() && this.name.isNotBlank()
}

fun UserRegisterDTO.validatePassword(): Boolean {
    return this.password.length >= 8
}

fun UserRegisterDTO.validateEmail(): Boolean {
    val regex = Regex("^[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\$")
    return regex.matches(this.email)
}
