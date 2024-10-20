package com.efkan.chatapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.efkan.chatapplication.databinding.ActivitySignupBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference  //realtime database ile bağlantı kurduğumuz yer burasıdır
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {
    private lateinit var binding:ActivitySignupBinding
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDbRef:DatabaseReference
    private lateinit var permissionlauncher:ActivityResultLauncher<String>
    private lateinit var activityResultLauncher:ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()
        binding=ActivitySignupBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        mAuth=FirebaseAuth.getInstance()

        binding.btnSignupsign.setOnClickListener{
            val name=binding.edtNamesign.text.toString()
            val email=binding.edtEmailsign.text.toString()
            val password=binding.edtPasswordsign.text.toString()
            if(!name.equals("") && !email.equals("") && !password.equals("")) {
                signUp(name,email,password)
            }
            else{
                Toast.makeText(this@Signup,"Email or password empty",Toast.LENGTH_SHORT).show()
            }
        }
        binding.selectPhotosign.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this@Signup,Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this@Signup,Manifest.permission.READ_MEDIA_IMAGES)){
                    Snackbar.make(binding.root,"Permission needed gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give permission"){
                        //request permission
                        permissionlauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }.show()
                }
                else{
                    //request permission
                    permissionlauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }
            else{
                //permission granted
                openGallery()
            }
        }
        }

    @SuppressLint("SuspiciousIndentation")
    private fun openGallery(){
    val intentToGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intentToGallery)
    }
    private fun registerLauncher() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val intentFromResult = result.data
                intentFromResult?.data?.let { uri ->
                   binding.selectPhotosign.setImageURI(uri)
                }
            }
        }

        permissionlauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(this@Signup, "Permission Needed!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun signUp(name:String,email:String,password:String){
        //logic of creating user
        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
            addUserToDatabase(name,email,mAuth.uid!!)
                val intent=Intent(this@Signup,Login::class.java)
                startActivity(intent)
        }.addOnFailureListener {authResult->
          Toast.makeText(this@Signup,authResult.localizedMessage,Toast.LENGTH_SHORT).show()
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun addUserToDatabase(name:String, email: String, uid:String){
    mDbRef=FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name,email,uid))
    }
}