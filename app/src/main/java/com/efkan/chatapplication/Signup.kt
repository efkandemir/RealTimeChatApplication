package com.efkan.chatapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.efkan.chatapplication.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class Signup : AppCompatActivity() {
    private lateinit var binding:ActivitySignupBinding
    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivitySignupBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        mAuth=FirebaseAuth.getInstance()

        binding.btnSignupsign.setOnClickListener{
            val email=binding.edtEmailsign.text.toString()
            val password=binding.edtPasswordsign.text.toString()
            if(!email.equals("") && !password.equals("")) {
                signUp(email,password)
            }
            else{
                Toast.makeText(this@Signup,"Email or password empty",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun signUp(email:String,password:String){
        //logic of creating user
        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent=Intent(this@Signup,Login::class.java)
                startActivity(intent)
        }.addOnFailureListener {authResult->
          Toast.makeText(this@Signup,authResult.localizedMessage,Toast.LENGTH_SHORT).show()
        }
    }
}