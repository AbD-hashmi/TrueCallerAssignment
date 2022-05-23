package com.truecaller.syed_true_caller_assignment.view

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.syed_true_caller_assignment.R
import com.truecaller.syed_true_caller_assignment.data.DataModel
import com.example.syed_true_caller_assignment.databinding.ActivityMainBinding
import com.truecaller.syed_true_caller_assignment.viewmodel.AssignmentViewModel
import kotlinx.coroutines.*

var dataModel: DataModel = DataModel()
private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val assignmentViewModel: AssignmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.dataModel = dataModel
        binding.fetch.setOnClickListener {
            lifecycleScope.launch {
                assignmentViewModel.fetchBlogData(this@MainActivity as Context, dataModel)
                binding.dataModel = dataModel
            }
        }
    }
}