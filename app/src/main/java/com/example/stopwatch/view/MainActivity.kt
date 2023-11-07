package com.example.stopwatch.view

import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.stopwatch.R
import com.example.stopwatch.databinding.ActivityMainBinding
import com.example.stopwatch.view.dialogInput.RenameDialogInput
import com.example.stopwatch.viewmodel.MainActivityViewModel
import com.example.stopwatch.viewmodel.TimerModel

private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG = "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = MainActivityAdapter(::setPosition)
    //private var timers = mutableListOf<TimerModel>()
    var currentPosition = 0
    private val viewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this)[MainActivityViewModel::class.java]
    }


    private fun setPosition(position: Int) {
        currentPosition = position
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainActivityRecyclerview.adapter = adapter

        registerForContextMenu(binding.mainActivityRecyclerview)

        //timers.add(TimerModel("Work"))


        viewModel.subscribeToLiveData().observe(this) { adapter.setData(it) }
//        viewModel.subscribeToLiveData().observe(this) {
//            onChange(it)
//        }

        //adapter.setData(timers)
    }

    private fun onChange(timers: MutableList<TimerModel>) {
        adapter.setData(timers)
        adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_timer -> {
                viewModel.addTimer()
                //timers.add(TimerModel())
               // adapter.setData(timers)
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
        if (v?.id == R.id.main_activity_recyclerview) {
            menuInflater.inflate(R.menu.timer_menu, menu)
        }

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_timer -> {
                //timers.add(TimerModel("New Timer"))
                viewModel.addTimer()
                //adapter.setData(timers)
            }

            R.id.remove_timer -> {
                //val currentTimer = timers.get(currentPosition)
                //currentTimer.stopClicked()
                //deleteItemFromList(currentPosition)
            }
            R.id.rename_timer -> {
                renameTimer()
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun deleteItemFromList(index: Int) {
        //timers.removeAt(position)
        viewModel.removeTimer(index)
        //adapter.setData(timers)
    }
    private fun renameTimer() {
        val renameDialogFragment = RenameDialogInput.newInstance()
        renameDialogFragment.setOnSearchClickListener(onRenameClickListener)
        renameDialogFragment.show(supportFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
    }

    private val onRenameClickListener: RenameDialogInput.OnRenameClickListener =
        object : RenameDialogInput.OnRenameClickListener {
            override fun onClick(newTimerName: String) {
                //timers[currentPosition].name = newTimerName
                viewModel.renameTimer(currentPosition, newTimerName)
                adapter.notifyDataSetChanged()
            }
        }
}