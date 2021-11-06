package com.dx.star.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dx.star.model.Repository
import com.dx.star.base.BaseViewModel

class DashboardViewModel(private val repository: Repository) : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}