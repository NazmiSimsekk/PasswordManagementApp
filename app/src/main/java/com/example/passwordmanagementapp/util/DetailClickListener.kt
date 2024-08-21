package com.example.passwordmanagementapp.util

import android.view.View

interface DetailClickListener {

    fun detailSaveClickListener(v: View)

    fun detailDeleteClickListener(v: View)

    fun detailCopyClickListener(v: View)
}