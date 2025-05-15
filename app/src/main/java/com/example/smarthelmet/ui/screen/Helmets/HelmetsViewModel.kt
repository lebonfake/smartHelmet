package com.example.smarthelmet.ui.screen.Helmets

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthelmet.data.repository.HelmetRepository
import com.example.smarthelmet.model.Helmet
import com.example.smarthelmet.model.Incident
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HelmetsViewModel(private val helmetRepository : HelmetRepository) : ViewModel()  {

     var helmets = MutableStateFlow<List<Helmet>>(emptyList())

   init {
       getHelmets()
   }

    private fun getHelmets() {
        viewModelScope.launch{

            helmetRepository.getHelmets(
            onResult = {
                list-> helmets.value = list
                Log.d("helmets", " i am in on results here are helmets $list ")

            },
            onError =
            {
                e-> Log.d("helmets", "getHelmets: $e")
            }
        )
        }

    }
}