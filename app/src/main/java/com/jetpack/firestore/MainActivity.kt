package com.jetpack.firestore

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.ktx.storage
import com.jetpack.firestore.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val listItems = ArrayList<String>()
    val listPrefix = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        var firstName=binding.inputFirstName.text
//        var lastName=binding.inputLastName.text
//        readFireStoreData()
//        binding.submit.setOnClickListener {
//            saveFireStore(firstName.toString(), lastName.toString())
//        }
//
        listAllFiles()
    }
    fun listAllFiles() {
        // [START storage_list_all]
        val storage = Firebase.storage
        val listRef = storage.reference

        listRef.listAll()
            .addOnSuccessListener {
                it.items.forEach { item ->
                    listItems.add(item.path)
                }
                it.prefixes.forEach { prefix ->
                    listPrefix.add(prefix.path)
                }
                binding.resultaa.text=listItems.toString()+"\n"+listPrefix.toString()
                Log.d("listAllFiles", listItems.toString())
                Log.d("listPrefix", listPrefix.toString())
                Log.d("listPrefix+listAllFiles", listPrefix.toString())
            }
            .addOnFailureListener {
                Log.d("Fail", "Fail")
            }


    }


    private fun saveFireStore(firstName: String, lastName: String) {
        Toast.makeText(this@MainActivity, firstName+lastName,Toast.LENGTH_SHORT).show()
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["firstName"] = firstName
        user["lastName"] = lastName
        db.collection("user")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
                Toast.makeText(this@MainActivity, "Record Sucess",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e)
                Toast.makeText(this@MainActivity, "Record Fail",Toast.LENGTH_SHORT).show()}

    }

    fun readFireStoreData(){
        val db = FirebaseFirestore.getInstance()
        db.collection("user")
            .get()
            .addOnCompleteListener { task ->
                val result = StringBuffer()
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Read", document.id + " => " + document.data)
                        result.append(document.data["firstName"]).append("")
                            .append(document.data.getValue("lastName")).append("\n")
                    }
                    binding.result.text = result
                } else {
                    Log.w("Read", "Error getting documents.", task.exception)
                }
            }
    }
}