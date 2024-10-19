package com.example.medimingle.data.util

import android.content.Context
import android.content.SharedPreferences
import com.example.medimingle.data.model.CategoryInfo
import com.example.medimingle.data.model.TaskInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class


SharedPreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("task_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        const val TASKS_KEY = "tasks"
        const val CATEGORIES_KEY = "categories"
    }

    fun saveTasks(tasks: List<TaskInfo>) {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(tasks)
        editor.putString(TASKS_KEY, json)
        editor.apply()
    }

    fun getTasks(): List<TaskInfo> {
        val json = sharedPreferences.getString(TASKS_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<TaskInfo>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveCategories(categories: List<CategoryInfo>) {
        val jsonString = gson.toJson(categories)
        sharedPreferences.edit().putString(CATEGORIES_KEY, jsonString).apply()
    }

    fun getCategories(): List<CategoryInfo> {
        val jsonString = sharedPreferences.getString(CATEGORIES_KEY, null)
        val type = object : TypeToken<List<CategoryInfo>>() {}.type
        return if (jsonString != null) gson.fromJson(jsonString, type) else emptyList()
    }

    fun deleteTask(task: TaskInfo) {
        val tasks = getTasks().toMutableList()
        tasks.remove(task)
        saveTasks(tasks)
    }

    fun deleteCategory(category: CategoryInfo) {
        val categories = getCategories().toMutableList()
        categories.remove(category)
        saveCategories(categories)
    }

}
