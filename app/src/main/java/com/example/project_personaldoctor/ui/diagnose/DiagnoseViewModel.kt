package com.example.project_personaldoctor.ui.diagnose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DiagnoseViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Medicines Database"
    }
    val text: LiveData<String> = _text
}