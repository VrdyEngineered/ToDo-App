package com.example.to_doapp_kt.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.to_doapp_kt.R
import com.example.to_doapp_kt.databinding.FragmentSignInBinding
import com.example.to_doapp_kt.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth


class signInFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding1: FragmentSignInBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding1= FragmentSignInBinding.inflate(inflater,container,false)
        return binding1.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }
    private fun init(view: View) {
        navControl= Navigation.findNavController(view)
        auth= FirebaseAuth.getInstance()
    }

    private fun registerEvents() {
        binding1.authtxtviewSignup.setOnClickListener{
            navControl.navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding1.signinNxtbtnn.setOnClickListener{
            val email=binding1.signinEmail.text.toString().trim()
            val pswd=binding1.signinPswd.text.toString().trim()

            if(email.isNotEmpty()&& pswd.isNotEmpty() )
            {

                    auth.signInWithEmailAndPassword(email,pswd).addOnCompleteListener({
                        if(it.isSuccessful)
                        {
                            Toast.makeText(context,"Login Successfully", Toast.LENGTH_SHORT).show()
                            navControl.navigate(R.id.action_signInFragment_to_homeFragment)
                            {
                                popUpTo(R.id.splashFragment) { inclusive = true } // Clears splash from back stack

                            }


                        }
                        else
                        {
                            Toast.makeText(context,it.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    })

            }
        }
    }
}