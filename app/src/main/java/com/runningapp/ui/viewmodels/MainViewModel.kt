package com.runningapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runningapp.db.Run
import com.runningapp.repositories.MainRepository
import kotlinx.coroutines.launch

//viewModel collects data from repository and provides it for the views (fragments in this case)
//usually we cannot create an instance of viewModel this easily and dagger cannot do this easily:
//class MainViewModel @Inject constructor(
//val mainRepository: MainRepository) : ViewModel()
// because when we want to pass parameters to viewModel we need factory for that
//and in the old dagger this was very complicated but hilt made it much easier
//and hilt will manage the factory and other things for us behind the scenes
// SO we use @ViewModelInject
//we should make function in AppModule.kt to provide MainRepository but
//since MainRepository uses only RunDAO, and hilt already knows how to provide that (from the function provideDao()
//we don't need to write any more functions in AppModule.kt as manual to hilt on how to inject MainRepository
class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
): ViewModel() {

    val runsSortedByDate = mainRepository.getAllRunsSortedByDate()

    fun insertRun(run: Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }
}