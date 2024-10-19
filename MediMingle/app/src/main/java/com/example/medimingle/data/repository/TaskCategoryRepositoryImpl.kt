package com.example.medimingle.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medimingle.data.model.CategoryInfo
import com.example.medimingle.data.model.NoOfTaskForEachCategory
import com.example.medimingle.data.model.TaskCategoryInfo
import com.example.medimingle.data.model.TaskInfo
import com.example.medimingle.data.util.SharedPreferencesManager
import com.example.medimingle.domain.TaskCategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class TaskCategoryRepositoryImpl @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager
) : TaskCategoryRepository {

    override suspend fun updateTaskStatus(task: TaskInfo): Int {
        val tasks = sharedPreferencesManager.getTasks().toMutableList()
        val taskIndex = tasks.indexOfFirst { it.id == task.id }
        return if (taskIndex != -1) {
            tasks[taskIndex] = task
            sharedPreferencesManager.saveTasks(tasks)
            1  // Indicating successful update
        } else {
            0  // Task not found
        }
    }

    override suspend fun deleteTask(task: TaskInfo) {
        sharedPreferencesManager.deleteTask(task)
    }

    override suspend fun insertTaskAndCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo) {
        val tasks = sharedPreferencesManager.getTasks().toMutableList()
        val categories = sharedPreferencesManager.getCategories().toMutableList()

        tasks.add(taskInfo)
        if (!categories.contains(categoryInfo)) {
            categories.add(categoryInfo)
        }

        sharedPreferencesManager.saveTasks(tasks)
        sharedPreferencesManager.saveCategories(categories)
    }

    override suspend fun deleteTaskAndCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo) {
        deleteTask(taskInfo)
        sharedPreferencesManager.deleteCategory(categoryInfo)
    }

    override suspend fun updateTaskAndAddDeleteCategory(
        taskInfo: TaskInfo,
        categoryInfoAdd: CategoryInfo,
        categoryInfoDelete: CategoryInfo
    ) {
        updateTaskStatus(taskInfo)
        val categories = sharedPreferencesManager.getCategories().toMutableList()

        categories.remove(categoryInfoDelete)
        if (!categories.contains(categoryInfoAdd)) {
            categories.add(categoryInfoAdd)
        }

        sharedPreferencesManager.saveCategories(categories)
    }

    override suspend fun updateTaskAndAddCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo) {
        updateTaskStatus(taskInfo)
        val categories = sharedPreferencesManager.getCategories().toMutableList()

        if (!categories.contains(categoryInfo)) {
            categories.add(categoryInfo)
        }

        sharedPreferencesManager.saveCategories(categories)
    }

    override fun getUncompletedTask(): LiveData<List<TaskCategoryInfo>> {
        val tasks = sharedPreferencesManager.getTasks().filter { !it.status }  // Status is false for uncompleted
        val taskCategoryInfoList = tasks.map { taskInfo ->
            TaskCategoryInfo(taskInfo, findCategory(taskInfo.category))
        }
        return MutableLiveData(taskCategoryInfoList)
    }

    override fun getCompletedTask(): LiveData<List<TaskCategoryInfo>> {
        val tasks = sharedPreferencesManager.getTasks().filter { it.status }
        val taskCategoryInfoList = tasks.map { TaskCategoryInfo(it, findCategory(it.category)) }
        return MutableLiveData(taskCategoryInfoList)
    }

    override fun getUncompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>> {
        val tasks = sharedPreferencesManager.getTasks().filter { !it.status && it.category == category }  // Status is false for uncompleted
        val taskCategoryInfoList = tasks.map { TaskCategoryInfo(it, findCategory(category)) }
        return MutableLiveData(taskCategoryInfoList)
    }

    override fun getCompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>> {
        val tasks = sharedPreferencesManager.getTasks().filter { it.status && it.category == category }  // Status is true for completed
        val taskCategoryInfoList = tasks.map { TaskCategoryInfo(it, findCategory(category)) }
        return MutableLiveData(taskCategoryInfoList)
    }

    override fun getNoOfTaskForEachCategory(): LiveData<List<NoOfTaskForEachCategory>> {
        val tasks = sharedPreferencesManager.getTasks()
        val categories = sharedPreferencesManager.getCategories()

        val taskCountList = categories.map { category ->
            val count = tasks.count { it.category == category.categoryInformation }
            NoOfTaskForEachCategory(category.categoryInformation, count, category.color)
        }

        return MutableLiveData(taskCountList)
    }

    override fun getCategories(): LiveData<List<CategoryInfo>> {
        return MutableLiveData(sharedPreferencesManager.getCategories())
    }

    override suspend fun getCountOfCategory(category: String): Int {
        return sharedPreferencesManager.getTasks().count { it.category == category }
    }

    override suspend fun getActiveAlarms(currentTime: Date): List<TaskInfo> {
        return withContext(Dispatchers.IO) {
            sharedPreferencesManager.getTasks().filter { it.date.after(currentTime) && !it.status }  // Status is false for uncompleted
        }
    }

    private fun findCategory(category: String): CategoryInfo {
        return sharedPreferencesManager.getCategories().find { it.categoryInformation == category }
            ?: CategoryInfo(categoryInformation = category, color = "#000000")  // Default category if not found
    }

}
