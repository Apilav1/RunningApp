package com.runningapp.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class Run(
    var img: Bitmap? = null,
    var timestamp: Long = 0L,
    var avgSpeedInKMH: Float = 0F,
    var distanceInMeter: Int = 0,
    var timeInMillis: Long = 0L, //how long was run
    var caloriesBurned: Int = 0
) {
    @PrimaryKey(autoGenerate = true) //room worries about ids
    var id: Int? = null

}