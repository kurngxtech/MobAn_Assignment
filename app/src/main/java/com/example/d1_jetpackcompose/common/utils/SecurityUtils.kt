package com.example.d1_jetpackcompose.utils

import java.security.MessageDigest

object SecurityUtils {
    /**
     * Mengubah plain text password menjadi SHA-256 Hash String.
     * Ini mencegah password asli terbaca jika database bocor.
     */
    fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}