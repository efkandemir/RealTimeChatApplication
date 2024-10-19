package com.efkan.chatapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.efkan.chatapplication.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference  //realtime database ile bağlantı kurduğumuz yer burasıdır
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {
    private lateinit var binding:ActivitySignupBinding
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDbRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    private fun addUserToDatabase(name:String,email: String,uid:String){
    mDbRef=FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name,email,uid))
    }
}