package com.example.smarthelmet.ui.screen.Workers

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthelmet.data.repository.WorkerRepository
import com.example.smarthelmet.model.Worker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WorkersViewModel(val  workerRepository: WorkerRepository) : ViewModel() {
    var workers = MutableStateFlow<List<Worker>>(emptyList())

    init {
      /*  viewModelScope.launch {
            var result = workerRepository.addWorker(Worker(1,"fadli mohamed",21,"sick"))

        }*/
         getAllWorker()
    }

    fun getAllWorker(){
        viewModelScope.launch {
            workers.value = workerRepository.getAllWorkers()
        }
        Log.d("worker", "getAllWorker viewmodel: ${workers.value} ")
    }
}