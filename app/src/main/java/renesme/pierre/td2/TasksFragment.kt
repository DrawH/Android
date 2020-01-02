package renesme.pierre.td2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import network.Api
import network.TasksRepository

private val cocoScope = MainScope()
private val tasksRepository = TasksRepository()
private val tasks = mutableListOf<Task>()

class TasksFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }


    private val tasks = mutableListOf<Task>()

    val tasksAdapter = TasksAdapter(tasks)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        tasksAdapter.onDeleteClickListener = { task ->
            tasksRepository.deleteTask(task.id).observe(this, Observer { success ->
                if (success) {
                    tasks.remove(task)
                    tasksAdapter.notifyDataSetChanged()
                }
            })
        }

        add_button.setOnClickListener {
            tasks.add(Task(id = "${tasks.count()}", title = "task # ${tasks.count() + 1}"))
            tasksAdapter.notifyDataSetChanged()
            recycler_view_td2.scrollToPosition(tasks.count()-1)
        }
        recycler_view_td2.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = tasksAdapter
        }

    }

    override fun onResume() {
        super.onResume()
        cocoScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            TextView.text = "${userInfo.firstName} ${userInfo.lastName}"
        }

        tasksRepository.getTasks().observe(this, Observer {
            if (it != null) {
                tasks.clear()
                tasks.addAll(it)
                tasksAdapter.notifyDataSetChanged()
            }
        })


    }

    override fun onDestroy() {
        super.onDestroy()
        cocoScope.cancel()
    }
}