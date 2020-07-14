package com.runningapp.repositories

import com.runningapp.db.Run
import com.runningapp.db.RunDAO
import javax.inject.Inject

//the job of the repository is to collect data from all of our data sources (in this case only source is Room)
class MainRepository @Inject constructor(
    val runDao: RunDAO
){  //provides the functions from our db by DAOs
    suspend fun insertRun(run: Run) = runDao.insertRun(run)

    suspend fun deleteRun(run: Run) = runDao.deleteRun(run)

    //not suspend, liveData is asynchronous so we don't need to execute this function inside of coroutine
    //because our main goal is to asynchronous execution and liveData does it already
    fun getAllRunsSortedByDate() = runDao.getAllRunsSortedByDate()

    fun getAllRunsSortedByDistance() = runDao.getAllRunsSortedByDistance()

    fun getAllRunsSortedByTimeInMillis() = runDao.getAllRunsSortedByTimeInMillis()

    fun getAllRunsSortedByAvgSpeed() = runDao.getAllRunsSortedByAvgSpeed()

    fun getAllRunsSortedByCaloriesBurned() = runDao.getAllRunsSortedByCaloriesBurned()

    //for graph
    fun getTotalAvgSpeed() = runDao.getTotalAvgSpeed()

    fun getTotalDistance() = runDao.getTotalDistance()

    fun getTotalCaloriesBurned() = runDao.getTotalCaloriesBurned()

    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()
}