package com.example.medimingle.presentation.br

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.example.medimingle.data.repository.TaskCategoryRepositoryImpl
import com.example.medimingle.data.model.TaskInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OnCompletedBroadcastReceiver : BroadcastReceiver() {
    @Inject lateinit var repository: TaskCategoryRepositoryImpl

    override fun onReceive(p0: Context?, p1: Intent?) {
        val taskInfo = p1?.getSerializableExtra("task_info") as? TaskInfo
        if (taskInfo != null) {
            taskInfo.status = true
        }
        CoroutineScope(IO).launch {
            taskInfo?.let {
                repository.updateTaskStatus(it)
            }
        }
        if (p0 != null && taskInfo != null) {
            NotificationManagerCompat.from(p0).cancel(null, taskInfo.id)
        }
    }
}