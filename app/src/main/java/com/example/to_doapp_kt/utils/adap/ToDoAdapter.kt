package com.example.to_doapp_kt.utils.adap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.to_doapp_kt.databinding.EachTodoItemBinding
import com.example.to_doapp_kt.fragments.HomeFragment

class ToDoAdapter(private val list:MutableList<ToDoData>): RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> (){

    private var listener:ToDoAdapterClicksInterface?=null
    fun setListener(listener: HomeFragment)
    {
        this.listener=listener

    }
    inner class ToDoViewHolder( val binding: EachTodoItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = EachTodoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.givenTodoTask.text=this.task
                binding.deleteBtn.setOnClickListener{
                    listener?.onDeleteTaskBtnClicked(this)

                }

                binding.editBtn.setOnClickListener{
                         listener?.onEditTaskBtnClicked(this)
                    }


            }
        }

    }
    interface ToDoAdapterClicksInterface{
        fun onDeleteTaskBtnClicked(tododata: ToDoData)
        fun onEditTaskBtnClicked(tododata: ToDoData)
    }

}