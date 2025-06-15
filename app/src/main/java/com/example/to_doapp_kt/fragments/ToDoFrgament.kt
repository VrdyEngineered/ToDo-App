package com.example.to_doapp_kt.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.to_doapp_kt.databinding.FragmentToDoBinding
import com.example.to_doapp_kt.utils.adap.ToDoData


class ToDoFrgament : DialogFragment() {


 private lateinit var binding:FragmentToDoBinding
 private lateinit var listener:DialogaddtodoClickListener
 private var toDoData: ToDoData?=null

 // constructor

   public fun setListener(listener: HomeFragment)
 {
      this.listener=listener
  }

    companion object {
        const val TAG = "ToDoFragement"

        @JvmStatic
        fun newInstance(taskId: String, task: String) = ToDoFrgament().apply {
            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("task", task)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentToDoBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            val taskId = arguments?.getString("taskId")
            val task = arguments?.getString("task")

            if (taskId != null && task != null) {
                toDoData = ToDoData(taskId, task)
                binding.todo.setText(task) // If you want to pre-fill the EditText for editing
            }
            binding.todo.setText(toDoData?.task)
        }
        registerEvents()
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }


    private fun registerEvents() {
       binding.addtodo.setOnClickListener()
           {
               val todotask=binding.todo.text.toString()
               if(todotask.isNotEmpty())
               {
                   if(toDoData==null)
                   {
                       // new task being added
                       listener.onSaveTask(todotask,binding.todo)
                   }
                   else
                   {
                       // updating a tassk
                       toDoData?.task=todotask
                       listener.onUpdateTask(toDoData!!,binding.todo )
                   }

               }
               else
               {
                   Toast.makeText(context,"please type some task",Toast.LENGTH_SHORT).show()
               }
           }
        binding.todoclose.setOnClickListener(
            {
                dismiss()
            }
        )

    }


    interface DialogaddtodoClickListener{
        fun onSaveTask(todoo:String, todo: EditText)
        fun onUpdateTask(toDoData: ToDoData, todo: EditText)
    }


}