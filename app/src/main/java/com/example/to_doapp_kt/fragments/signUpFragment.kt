package com.example.to_doapp_kt.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.collection.emptyLongSet
//import androidx.compose.ui.semantics.text
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import com.example.to_doapp_kt.R
import com.example.to_doapp_kt.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth



class signUpFragment : Fragment()
{

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl:NavController
    private lateinit var binding: FragmentSignUpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSignUpBinding.inflate(inflater,container,false)
        // Force logout to prevent auto-login after signing up
//        if (auth.currentUser != null) {
//            auth.signOut()
//        }
        return binding.root
    }


    private fun init(view: View) {
        navControl= Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()
    }

    private fun registerEvents() {
        binding.authtxtview.bringToFront()

        binding.authtxtview.setOnClickListener {
            Log.d("CLICK", "TextView Clicked")
            // Use Navigation Component for consistent navigation
            navControl.navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        binding.nxtbtnn.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val pswd = binding.pswd.text.toString().trim()
            val repswd = binding.repswd.text.toString().trim()

            if (email.isNotEmpty() && pswd.isNotEmpty() && repswd.isNotEmpty()) {
                if (pswd == repswd) {
                    binding.nxtbtnn.isEnabled = false // Disable button during registration
                    auth.createUserWithEmailAndPassword(email, pswd)
                        .addOnCompleteListener { task ->
                            binding.nxtbtnn.isEnabled = true // Re-enable button after registration attempt
                            if (task.isSuccessful) {
                                // Registration successful
                                Toast.makeText(
                                    context,
                                    "Registered Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d(
                                    "AuthProcess",
                                    "Navigating to Home after successful registration"
                                )

                                navControl.navigate(R.id.action_signUpFragment_to_homeFragment2) {
                                    // Optional: Clear back stack up to splash if needed
                                    // popUpTo(R.id.splashFragment) { inclusive = true }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    task.exception?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Enter Email & Password", Toast.LENGTH_SHORT).show()
            }
        }
    }
    }






