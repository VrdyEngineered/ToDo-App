package com.example.to_doapp_kt.fragments

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.to_doapp_kt.R
import com.example.to_doapp_kt.databinding.FragmentSignInBinding
import com.example.to_doapp_kt.databinding.ProfileFragBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.ViewModelProvider
import com.example.to_doapp_kt.viewmodel.SharedViewModel


class FragProfile : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
   private lateinit var binding: ProfileFragBinding

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= ProfileFragBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        init(view)
        registerEvents()
    }

    private fun registerEvents() {
          binding.btnLogout.setOnClickListener{
              navControl.navigate(R.id.action_fragProfile_to_signUpFragment)
          }
          // ongoing tasks
        sharedViewModel.taskCount.observe(viewLifecycleOwner) { count ->
            binding.tvOngoingTasks.text = count.toString()
        }

        // Completed tasks
        sharedViewModel.completedTaskCount.observe(viewLifecycleOwner) { completedCount ->
            binding.tvTasksCompleted.text = completedCount.toString()
        }


    }

    private fun init(view: View) {

        navControl= Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()
    }


}

