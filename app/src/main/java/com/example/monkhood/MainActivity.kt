package com.example.monkhood
import android.content.Intent
import android.net.Uri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Toast
import com.example.model.UserModel
import com.example.monkhood.databinding.ActivityMainBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage :FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private var selectImg: Uri? = null


    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       database = FirebaseDatabase.getInstance()
        storage= FirebaseStorage.getInstance()
        auth= FirebaseAuth.getInstance()

        binding.btnCancel.setOnClickListener {
           finish()
        }

        binding.btnSave.setOnClickListener {
            if (binding.nameEt.text.isEmpty()){
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            } else if (binding.emailEt.text.isEmpty()){
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }else if (binding.phoneEt.text.isEmpty()){
                Toast.makeText(this, "Please enter your number", Toast.LENGTH_SHORT).show()
            }
            else uploadData()
            startActivity(Intent(this,SecondActivity::class.java))
            finish()

        }

        binding.ButtonDOB.setOnClickListener {
            val datePicker= MaterialDatePicker.Builder.datePicker().setTitleText("Select Date").build()
            datePicker.show(supportFragmentManager, "Date")

            datePicker.addOnPositiveButtonClickListener(MaterialPickerOnPositiveButtonClickListener { selection : Long? ->
                 val dateString : String = DateFormat.format("dd/MM/yyyy", Date(selection!!)).toString()
                  binding.txtDatePicker.text= dateString
            })
        }

        binding.upload.setOnClickListener {
            val intent = Intent()
            intent.action= Intent.ACTION_GET_CONTENT
            intent.type= "image/*"
            startActivityForResult(intent, 1)
        }
    }

    private fun uploadData() {
     val reference = storage.reference.child("Profile").child(Date().time.toString())
        selectImg?.let {
            reference.putFile(it).addOnCompleteListener{
                if (it.isSuccessful){
                    reference.downloadUrl.addOnSuccessListener { task ->
                        uploadInfo(task.toString())
                    }
                }
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
         val user = UserModel(auth.uid.toString(), binding.nameEt.text.toString(), binding.phoneEt.text
             .toString(), imgUrl, binding.emailEt.text.toString())

        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                finish()
            }
    }


    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data!=null){
            if (data.data !=null){
                selectImg= data.data!!
                binding.profile.setImageURI(selectImg)
            }
        }
    }
}