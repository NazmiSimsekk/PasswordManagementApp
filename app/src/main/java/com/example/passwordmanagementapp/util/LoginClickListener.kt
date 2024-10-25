package com.example.passwordmanagementapp.util

import android.view.View

interface LoginClickListener {

    fun loginClick(v: View)

    fun registerClick(v: View)

    fun forgotPasswordClick(v: View)
}