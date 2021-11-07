package com.dx.star.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dx.star.model.Repository
import com.dx.star.base.BaseViewModel

class HomeViewModel(private val repository: Repository) : BaseViewModel(){

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}