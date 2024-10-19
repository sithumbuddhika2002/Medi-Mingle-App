package com.example.medimingle.data.model


import java.io.Serializable
import java.util.*

data class TaskInfo(
    var id: Int,
    var description: String,
    var date: Date,
    var priority: Int,
    var status: Boolean,
    var category: String
) : Serializable
