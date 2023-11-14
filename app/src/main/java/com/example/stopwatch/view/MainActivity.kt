package com.example.stopwatch.view

import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.stopwatch.R
import com.example.stopwatch.databinding.ActivityMainBinding
import com.example.stopwatch.view.dialogInput.RenameDialogInput
import com.example.stopwatch.viewmodel.MainActivityViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ScopeActivity

private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG = "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"

class MainActivity : ScopeActivity() {

    private lateinit var binding: ActivityMainBinding
    //private val adapter = MainActivityAdapter(::setPosition)
    private val adapter: MainActivityAdapter by lazy { MainActivityAdapter(::setPosition) }
    var currentPosition = 0
    lateinit var viewModel: MainActivityViewModel
//    private val viewModel: MainActivityViewModel by lazy {
//        ViewModelProvider(this).get(MainActivityViewModel::class.java)
//    }


    private fun setPosition(position: Int) {
        currentPosition = position
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        registerForContextMenu(binding.mainActivityRecyclerview)
        binding.mainActivityRecyclerview.adapter = adapter

    }

    private fun initViewModel() {
        if(binding.mainActivityRecyclerview.adapter != null) {
            throw IllegalStateException("The ViewModel should be initialised first")
        }

        val model: MainActivityViewModel by inject()
        viewModel = model

        viewModel.subscribeToLiveData().observe(this, Observer {
            Toast.makeText(applicationContext, "Adapter update", Toast.LENGTH_SHORT).show()
            adapter.setData(it)
        })
    }

    override fun onDestroy() {
        viewModel.subscribeToLiveData().removeObservers(this)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_timer -> {
                viewModel.addTimer()
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
                viewModel.addTimer()
            }
            R.id.remove_timer -> {
                viewModel.removeTimer(currentPosition, this)

            }
            R.id.rename_timer -> {
                renameTimer()
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun renameTimer() {
        val renameDialogFragment = RenameDialogInput.newInstance()
        renameDialogFragment.setOnSearchClickListener(onRenameClickListener)
        renameDialogFragment.show(supportFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
    }

    private val onRenameClickListener: RenameDialogInput.OnRenameClickListener =
        object : RenameDialogInput.OnRenameClickListener {
            override fun onClick(newTimerName: String) {
                viewModel.renameTimer(currentPosition, newTimerName)
            }
        }
}