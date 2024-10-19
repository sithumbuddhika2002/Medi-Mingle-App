package com.example.medimingle.data.model


import java.io.Serializable

data class TaskCategoryInfo(
    val taskInfo: TaskInfo,
    val categoryInfo: CategoryInfo
) : Serializable
