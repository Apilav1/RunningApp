package com.runningapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runningapp.db.Run
import com.runningapp.other.SortType
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

    private val runsSortedByDate = mainRepository.getAllRunsSortedByDate()
    private val runsSortedByDistance = mainRepository.getAllRunsSortedByDistance()
    private val runsSortedByAvgSpeed = mainRepository.getAllRunsSortedByAvgSpeed()
    private val runsSortedByCaloriesBurned = mainRepository.getAllRunsSortedByCaloriesBurned()
    private val runsSortedByTimeInMillis = mainRepository.getAllRunsSortedByTimeInMillis()

    //to merge several liveDatas we use MediatorLiveData
    val runs = MediatorLiveData<List<Run>>()

    var sortType = SortType.DATE //default one

    init {
        runs.addSource(runsSortedByDate) { result -> //this is an observer in a form of lambda
            //if there was a change and the type is DATE
            if(sortType == SortType.DATE) {
                 result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByAvgSpeed) { result -> //this is an observer in a form of lambda
            if(sortType == SortType.AVG_SPEED) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByCaloriesBurned) { result -> //this is an observer in a form of lambda
            if(sortType == SortType.CALORIES_BURNED) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByDistance) { result -> //this is an observer in a form of lambda
            if(sortType == SortType.DISTANCE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByTimeInMillis) { result -> //this is an observer in a form of lambda
            if(sortType == SortType.RUNNING_TIME) {
                result?.let { runs.value = it }
            }
        }
    }

    //because logic in the init block doesn't recognize when the sortType is changed
    fun sortRuns(sortType: SortType) = when(sortType) {
        SortType.DATE -> runsSortedByDate.value?.let { runs.value = it }
        SortType.AVG_SPEED -> runsSortedByAvgSpeed.value?.let { runs.value = it }
        SortType.RUNNING_TIME -> runsSortedByTimeInMillis.value?.let { runs.value = it }
        SortType.DISTANCE -> runsSortedByDistance.value?.let { runs.value = it }
        SortType.CALORIES_BURNED -> runsSortedByCaloriesBurned.value?.let { runs.value = it }
    }.also {
        this.sortType = sortType
    }

    fun insertRun(run: Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }
}