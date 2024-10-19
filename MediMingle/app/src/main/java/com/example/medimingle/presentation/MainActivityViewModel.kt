package com.example.medimingle.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medimingle.data.model.CategoryInfo
import com.example.medimingle.data.model.NoOfTaskForEachCategory
import com.example.medimingle.data.model.TaskCategoryInfo
import com.example.medimingle.data.model.TaskInfo
import com.example.medimingle.domain.TaskCategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor (private val repository: TaskCategoryRepository) : ViewModel() {

    fun updateTaskStatus(task: TaskInfo) {
        viewModelScope.launch(IO) {
            repository.updateTaskStatus(task)
        }
    }

    fun deleteTask(task: TaskInfo) {
        viewModelScope.launch(IO) {
            repository.deleteTask(task)
        }
    }

    fun insertTaskAndCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo) {
        viewModelScope.launch(IO) {
            repository.insertTaskAndCategory(taskInfo, categoryInfo)
        }
    }

    fun updateTaskAndAddCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo){
        viewModelScope.launch(IO) {
            repository.updateTaskAndAddCategory(taskInfo, categoryInfo)
        }
    }

    fun updateTaskAndAddDeleteCategory(
        taskInfo: TaskInfo,
        categoryInfoAdd: CategoryInfo,
        categoryInfoDelete: CategoryInfo
    ) {
        viewModelScope.launch(IO) {
            repository.updateTaskAndAddDeleteCategory(taskInfo, categoryInfoAdd, categoryInfoDelete)
        }
    }

    fun deleteTaskAndCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo) {
        viewModelScope.launch(IO) {
            repository.deleteTaskAndCategory(taskInfo, categoryInfo)
        }
    }

    fun getUncompletedTask(): LiveData<List<TaskCategoryInfo>> {
        return repository.getUncompletedTask()
    }

    fun getCompletedTask(): LiveData<List<TaskCategoryInfo>> {
        return repository.getCompletedTask()
    }

    fun getUncompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>> {
        return repository.getUncompletedTaskOfCategory(category)
    }

    fun getCompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>> {
        return repository.getCompletedTaskOfCategory(category)
    }

    fun getNoOfTaskForEachCategory(): LiveData<List<NoOfTaskForEachCategory>>{
        return repository.getNoOfTaskForEachCategory()
    }

    fun getCategories(): LiveData<List<CategoryInfo>>  {
        return repository.getCategories()
    }

    suspend fun getCountOfCategory(category: String): Int{
        var count: Int
        coroutineScope() {
            count = withContext(IO) { repository.getCountOfCategory(category) }
        }
        return count
    }

    fun getAlarms(currentTime : Date){
        CoroutineScope(Dispatchers.Main).launch {
            val list = repository.getActiveAlarms(currentTime)
            Log.d("DATA", list.toString())
        }
    }
}