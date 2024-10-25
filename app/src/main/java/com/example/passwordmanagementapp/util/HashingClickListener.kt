package com.example.passwordmanagementapp.util

import android.view.View

interface HashingClickListener {
    fun hashingClickListener(v: View)
    fun hashResultCopy(v: View)
    fun goToList(v: View)
    fun gotToGenerate(v: View)
    fun goToAddPassword(v:View)
}