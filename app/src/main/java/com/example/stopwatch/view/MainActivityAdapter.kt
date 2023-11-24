package com.example.stopwatch.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.stopwatch.R
import com.example.stopwatch.viewmodel.TimerModel

class MainActivityAdapter(
    private var position: (Int) -> Unit
) : RecyclerView.Adapter<MainActivityAdapter.RecyclerItemViewHolder>() {
    private lateinit var context: Context
    private var timers: List<TimerModel> = arrayListOf()

    fun setData(timers: List<TimerModel>) {
        this.timers = timers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        context = parent.context
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_main_recyclerview_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return timers.size
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(timers[position])
        holder.itemView.setOnCreateContextMenuListener() { contextMenu, _, _ ->
            setPosition(position)
        }
    }


    inner class RecyclerItemViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        fun bind(timer: TimerModel) {
            val buttonStart = itemView.findViewById<ImageButton>(R.id.button_start)
            val buttonStop = itemView.findViewById<ImageButton>(R.id.button_stop)
            val timerValueTextView = itemView.findViewById<TextView>(R.id.text_time)
            val timerNameTextView = itemView.findViewById<TextView>(R.id.timer_name)
            updateImageOnImageButton(buttonStart, timer.started)

            timer.subscribeToValue().removeObservers(context as LifecycleOwner)

            timer.startCollect()

            timer.subscribeToValue().observe(context as LifecycleOwner) {
                timerValueTextView.text = it
                //timer.notification.updateNotification(it)
            }
            timerNameTextView.text = timer.name

            buttonStart.setOnClickListener {
                if (!timer.started) {
                    for (timer in timers) {
                        pauseTimer(timer)
                        notifyDataSetChanged()
                    }
                    timer.startClicked()
                } else {
                    timer.pauseClicked()
                }

                updateImageOnImageButton(buttonStart, timer.started)

            }
            buttonStop.setOnClickListener {
                timer.stopClicked()
                updateImageOnImageButton(buttonStart, timer.started)
            }
        }
    }

    private fun pauseTimer(timer: TimerModel) {
        timer.pauseClicked()
    }

    private fun startTimer(timer: TimerModel) {
        timer.startClicked()
    }

    private fun stopTimer(timer: TimerModel) {
        timer.stopClicked()
    }

    private fun updateImageOnImageButton(button: ImageButton, started: Boolean) {
        if (started) {
            button.setImageResource(R.drawable.baseline_pause_circle_outline_24)

        } else {
            button.setImageResource(R.drawable.baseline_play_circle_outline_24)
        }
    }

    private fun setPosition(pos: Int) {
        position(pos)
    }
}
