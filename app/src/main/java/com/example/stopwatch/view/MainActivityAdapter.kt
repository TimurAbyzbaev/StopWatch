package com.example.stopwatch.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.stopwatch.R
import com.example.stopwatch.viewmodel.MainActivityViewModel

class MainActivityAdapter(
    private var position: (Int) -> Unit
): RecyclerView.Adapter<MainActivityAdapter.RecyclerItemViewHolder>() {
    private lateinit var context: Context
    private var viewModels: List<MainActivityViewModel> = arrayListOf()

    fun setData(viewModels: List<MainActivityViewModel>) {
        this.viewModels = viewModels
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
        return viewModels.size
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(viewModels[position])
        holder.itemView.setOnCreateContextMenuListener() { contextMenu, _, _ ->
            setPosition(position)
        }
    }


    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view)//, View.OnCreateContextMenuListener
    {
        fun bind(viewModel: MainActivityViewModel) {
            viewModel.startCollect()
            viewModel.subscribe().observe(context as LifecycleOwner) {
                itemView.findViewById<TextView>(R.id.text_time).text = it
            }
            itemView.findViewById<TextView>(R.id.text_time).text = viewModel.subscribe().value

            itemView.findViewById<Button>(R.id.button_start).setOnClickListener {
                viewModel.startClicked()
            }

            itemView.findViewById<Button>(R.id.button_stop).setOnClickListener {
                viewModel.stopClicked()
            }

            itemView.findViewById<Button>(R.id.button_pause).setOnClickListener {
                viewModel.pauseClicked()
            }

        }

    }
    private fun setPosition(pos: Int) {
        position(pos)
    }

}
