package com.example.stopwatch.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

    fun setData(viewModels: List<TimerModel>) {
        this.timers = viewModels
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
        RecyclerView.ViewHolder(view)//, View.OnCreateContextMenuListener
    {
        fun bind(timer: TimerModel) {
            timer.startCollect()
            timer.subscribe().observe(context as LifecycleOwner) {
                itemView.findViewById<TextView>(R.id.text_time).text = it
            }
            itemView.findViewById<TextView>(R.id.text_time).text = timer.subscribe().value

            itemView.findViewById<TextView>(R.id.timer_name).text = timer.name

            itemView.findViewById<ImageButton>(R.id.button_start).setOnClickListener {
                timer.startClicked()
            }

            itemView.findViewById<ImageButton>(R.id.button_stop).setOnClickListener {
                timer.stopClicked()
            }

            itemView.findViewById<ImageButton>(R.id.button_pause).setOnClickListener {
                timer.pauseClicked()
            }

        }

    }

    private fun setPosition(pos: Int) {
        position(pos)
    }

}
