package network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import renesme.pierre.td2.Task


class TasksRepository {
    private val tasksService = Api.tasksService
    private val coroutineScope = MainScope()

    fun getTasks(): LiveData<List<Task>?> {
        val tasks = MutableLiveData<List<Task>?>()
        coroutineScope.launch { tasks.postValue(loadTasks()) }
        return tasks
    }

    private suspend fun loadTasks(): List<Task>? {
        val tasksResponse = tasksService.getTasks()
        return if (tasksResponse.isSuccessful) tasksResponse.body() else null
    }

    fun deleteTask(id : String): LiveData<Boolean> {
        val sucess = MutableLiveData<Boolean>()
        coroutineScope.launch { sucess.postValue(deleteRemoteTasks(id)) }
        return sucess
    }

    private suspend fun deleteRemoteTasks(id : String):Boolean {
        val response = tasksService.deleteTask(id)
        return response.isSuccessful
    }
}