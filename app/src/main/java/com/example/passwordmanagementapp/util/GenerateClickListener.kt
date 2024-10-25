package com.example.passwordmanagementapp.util

import android.view.View

interface GenerateClickListener {

    fun generatePassword(v: View)
    fun copyGeneratePassword(v: View)
    fun goToList(v: View)
    fun goToHashing(v: View)
    fun goToAdd(v: View)
}