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

       getAllWorker()
    }

    fun getAllWorker(){
        viewModelScope.launch {
            workers.value = workerRepository.getAllWorkers()
        }
        Log.d("worker", "getAllWorker viewmodel: ${workers.value} ")
    }

    fun onAddWorker(worker : Worker){


        viewModelScope.launch {
            val workerId = workerRepository.addWorker(worker) // Await the result
            if (workerId != null) {
                val newWorker = worker.copy(id = workerId) // Create a new Worker object with the ID
                // Assuming workers is a MutableStateFlow
                val currentWorkers = workers.value // Get the current list
                workers.value = currentWorkers + newWorker // Create a new list with the added worker and update the StateFlow
            } else {


            }

        }
    }

    fun deleteAllWorkers()
    {
        viewModelScope.launch { workerRepository.deleteAllWorkers() }

    }
}