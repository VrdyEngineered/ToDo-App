package com.example.to_doapp_kt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _taskCount = MutableLiveData<Int>(0)         // Ongoing Tasks
    val taskCount: LiveData<Int> = _taskCount

    private val _completedTaskCount = MutableLiveData<Int>(0) // Completed Tasks
    val completedTaskCount: LiveData<Int> = _completedTaskCount

    fun setTaskCount(count: Int) {
        _taskCount.value = count
    }

    fun incrementTaskCount() {
        _taskCount.value = (_taskCount.value ?: 0) + 1
    }

    fun decrementTaskCount() {
        _taskCount.value = (_taskCount.value ?: 0) - 1
    }

    fun incrementCompletedTaskCount() {
        _completedTaskCount.value = (_completedTaskCount.value ?: 0) + 1
    }

    fun setCompletedTaskCount(count: Int) {
        _completedTaskCount.value = count
    }
    fun updateTaskCount(count: Int) {
        _taskCount.value = count
    }


}
