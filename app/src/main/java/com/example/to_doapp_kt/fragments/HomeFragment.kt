package com.example.to_doapp_kt.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.to_doapp_kt.R
import com.example.to_doapp_kt.databinding.FragmentHomeBinding
import com.example.to_doapp_kt.utils.adap.ToDoAdapter
import com.example.to_doapp_kt.utils.adap.ToDoData
import com.example.to_doapp_kt.viewmodel.SharedViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.properties.Delegates


class HomeFragment : Fragment(), ToDoFrgament.DialogaddtodoClickListener,
    ToDoAdapter.ToDoAdapterClicksInterface {
 private  lateinit var  auth:FirebaseAuth
 private lateinit var databaseref: DatabaseReference
 private lateinit var navController: NavController
 private lateinit var binding: FragmentHomeBinding
private var popUpFragment: ToDoFrgament?=null
// related to recycler view
private lateinit var adapter:ToDoAdapter
private lateinit var mlist: MutableList<ToDoData>
private lateinit var nav_drawer: NavigationView
    private lateinit var sharedViewModel: SharedViewModel
    private var progress:Int = 0
    private var task_done:Int = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Set up the Toolbar as the ActionBar (you might prefer to do this in the Activity)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)


        // Initialize ActionBarDrawerToggle after binding is available
        val toggle = ActionBarDrawerToggle(
            requireActivity(), // Use requireActivity() to get the Activity
            binding.homeFragment, // Access the DrawerLayout through binding
            binding.toolbar, // Access the Toolbar through binding
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.homeFragment.addDrawerListener(toggle)
        toggle.syncState()

        // adding user email on the nav drawer
        // Get the header view and update the email TextView
        val headerView = binding.navDrawer.getHeaderView(0)
        val userEmailTextView = headerView.findViewById<TextView>(R.id.userEmailTextView) // Assuming ID is userEmailTextView
        val currentUser = FirebaseAuth.getInstance().currentUser
        userEmailTextView.text = currentUser?.email ?: "Not logged in"

        binding.navDrawer.setNavigationItemSelectedListener { item: MenuItem ->  // Access nav_drawer through binding
            when (item.itemId) {
                R.id.profile -> {
                   navController.navigate(R.id.action_homeFragment_to_fragProfile)
                }
                R.id.ssign_out -> {
                    navController.navigate(R.id.action_homeFragment_to_signUpFragment)
                }
            }
            binding.homeFragment.closeDrawer(GravityCompat.START)
            true
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]


        init(view)
        getDataFromFirebase()
        registerEvents()
    }

    private fun registerEvents() {
       binding.fab.setOnClickListener({
            if(popUpFragment!=null)
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
           popUpFragment=ToDoFrgament()
           popUpFragment!!.setListener(this)
           popUpFragment!!.show(
               childFragmentManager,
               ToDoFrgament.TAG
           )

       })
    }
    private fun getDataFromFirebase() {
        databaseref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mlist.clear()
                for (taskSnapshot in snapshot.children) {
                    val taskId = taskSnapshot.key
                    val task = taskSnapshot.getValue(String::class.java) // Assuming task is stored as String
                    if (taskId != null && task != null) {
                        mlist.add(ToDoData(taskId, task))
                    }
                }
                // Ensure UI update happens on the main thread
                activity?.runOnUiThread {
                    adapter.notifyDataSetChanged()

                    // 🆕 Update the count dynamically based on list size
                    sharedViewModel.updateTaskCount(mlist.size)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching data: ${error.message}")
                Toast.makeText(
                    context,
                    "Error fetching data: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun init(view: View) {

        navController=Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()
        databaseref=FirebaseDatabase.getInstance().reference
            .child("Tasks").child(auth.currentUser?.uid.toString())
        // logic for using recyler view
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager=LinearLayoutManager(context)
        mlist= mutableListOf()
        adapter =ToDoAdapter(mlist)
        adapter.setListener(this)
        binding.recyclerview.adapter=adapter


    }

    override fun onSaveTask(todoo: String, todo: EditText) {
        databaseref.push().setValue(todoo).addOnCompleteListener(
            {
                if(it.isSuccessful)
                {
                    Toast.makeText(context,"Task saved successfully!!",Toast.LENGTH_SHORT).show()
                    todo.text=null
//                    progress++
//                    sharedViewModel.incrementTaskCount()
//                   sharedViewModel.setTaskCount(progress)



                }
                else
                {
                    Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
                }
                todo.text=null
                popUpFragment!!.dismiss()
            }
        )

    }

    override fun onUpdateTask(toDoData: ToDoData, todo: EditText) {
       val map= HashMap<String,Any>()
        map[toDoData.TaskId]=toDoData.task
        databaseref.updateChildren(map).addOnCompleteListener({
            if(it.isSuccessful){
                Toast.makeText(context,"Updated successfully!!",Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
            }
            todo.text=null
            popUpFragment!!.dismiss()
        })
    }

    override fun onDeleteTaskBtnClicked(tododata: ToDoData) {
        databaseref.child(tododata.TaskId).removeValue().addOnCompleteListener{
            if(it.isSuccessful)
            {
                Toast.makeText(context,"Deleted successfully!!",Toast.LENGTH_SHORT).show()
                task_done++
                progress--
                //sharedViewModel.decrementTaskCount()
                sharedViewModel.incrementCompletedTaskCount()
            }
            else
            {
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditTaskBtnClicked(tododata: ToDoData) {
       // Toast.makeText(context,"Edit Functionality not yet implemented!!",Toast.LENGTH_SHORT).show()
        if(popUpFragment!=null)
            childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()


        popUpFragment=ToDoFrgament.newInstance(tododata.TaskId,tododata.task)
        popUpFragment!!.setListener(this)
        popUpFragment!!.show(childFragmentManager,ToDoFrgament.TAG)

    }


}