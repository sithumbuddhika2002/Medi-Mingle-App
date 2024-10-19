package com.example.medimingle.presentation.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medimingle.presentation.MainActivity
import com.example.medimingle.presentation.MainActivityViewModel
import com.example.medimingle.R
import com.example.medimingle.data.model.CategoryInfo
import com.example.medimingle.presentation.adapter.CategoryAdapter
import com.example.medimingle.presentation.adapter.TasksAdapter
import com.example.medimingle.databinding.FragmentBaseBinding
import com.example.medimingle.data.model.TaskCategoryInfo
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class BaseFragment : ParentFragment() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: FragmentBaseBinding
    @Inject
    @Named("base_fragment")
    lateinit var adapter: TasksAdapter
    @Inject
    lateinit var adapter2: CategoryAdapter

    private val handler = Handler(Looper.getMainLooper())  // Handler for updating the timer
    private var secondsElapsed = 0  // Counter for the timer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        // Start the timer when the fragment is loaded
        startTimer()

        adapter.setOnItemClickListener {
            editTaskInformation(it)
        }
        adapter.setOnTaskStatusChangedListener {
            updateTaskStatus(viewModel, it)
        }
        adapter2.setOnItemClickListener {
            goToTaskCategoryFragment(it)
        }
        initRecyclerView1()
        initRecyclerView2()

        binding.fab.setOnClickListener {
            val action = BaseFragmentDirections.actionBaseFragmentToNewTaskFragment(null)
            it.findNavController().navigate(action)
        }

        viewModel.getUncompletedTask().observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) binding.noResultAnimationView.visibility = View.VISIBLE
            else binding.noResultAnimationView.visibility = View.GONE
            adapter.differ.submitList(it)
        })

        viewModel.getNoOfTaskForEachCategory().observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) binding.categoryAnimationView.visibility = View.VISIBLE
            else binding.categoryAnimationView.visibility = View.GONE
            adapter2.differ.submitList(it)
        })

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val taskInfo = adapter.differ.currentList[position]?.taskInfo
                val categoryInfo = adapter.differ.currentList[position]?.categoryInfo
                if (taskInfo != null && categoryInfo != null) {
                    deleteTask(viewModel, taskInfo, categoryInfo)
                    Snackbar.make(binding.root, "Deleted Successfully", Snackbar.LENGTH_LONG)
                        .apply {
                            setAction("Undo") {
                                viewModel.insertTaskAndCategory(taskInfo, categoryInfo)
                            }
                            show()
                        }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.tasksRecyclerView)

    }

    private fun editTaskInformation(taskCategoryInfo: TaskCategoryInfo) {
        val action = BaseFragmentDirections.actionBaseFragmentToNewTaskFragment(taskCategoryInfo)
        findNavController().navigate(action)
    }

    private fun goToTaskCategoryFragment(category: String) {
        val action = BaseFragmentDirections.actionBaseFragmentToTaskCategoryFragment(category)
        findNavController().navigate(action)
    }

    private fun initRecyclerView2() {
        binding.tasksRecyclerView.adapter = adapter
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initRecyclerView1() {
        binding.categoriesRecyclerView.adapter = adapter2
        binding.categoriesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun startTimer() {
        handler.post(object : Runnable {
            override fun run() {
                // Increment the seconds elapsed
                secondsElapsed++

                // Convert secondsElapsed to hours, minutes, and seconds
                val hours = secondsElapsed / 3600
                val minutes = (secondsElapsed % 3600) / 60
                val seconds = secondsElapsed % 60

                // Update the TextView with the formatted time
                binding.taskTimer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                // Re-run the handler every second
                handler.postDelayed(this, 1000)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Stop the handler when the fragment is destroyed
        handler.removeCallbacksAndMessages(null)
    }
}
