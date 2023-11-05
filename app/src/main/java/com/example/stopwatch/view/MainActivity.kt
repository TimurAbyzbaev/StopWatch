package com.example.stopwatch.view

import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stopwatch.R
import com.example.stopwatch.databinding.ActivityMainBinding
import com.example.stopwatch.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = MainActivityAdapter(::setPosition)
    val viewModels = mutableListOf<MainActivityViewModel>()
    var currentPosition = 0

    private fun setPosition(position: Int){
        currentPosition = position
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initViewModel()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModels.add(MainActivityViewModel())
        adapter.setData(viewModels)
        binding.mainActivityRecyclerview.adapter = adapter

        registerForContextMenu(binding.mainActivityRecyclerview)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.add_timer -> {
                viewModels.add(MainActivityViewModel())
                adapter.setData(viewModels)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if(v?.id == R.id.main_activity_recyclerview){
            menuInflater.inflate(R.menu.timer_menu, menu)
        }

    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.add_timer -> {
                viewModels.add(MainActivityViewModel())
                adapter.setData(viewModels)
            }
            R.id.remove_timer -> {
                deleteItemFromList(currentPosition)
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun deleteItemFromList(position: Int){
        viewModels.removeAt(position)
        adapter.setData(viewModels)
    }
}