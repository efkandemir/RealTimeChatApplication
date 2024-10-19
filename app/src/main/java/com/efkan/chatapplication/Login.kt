package com.efkan.chatapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.efkan.chatapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()   //login ekranında action barı gizlememizi sağlar
        mAuth=FirebaseAuth.getInstance()
        binding=ActivityLoginBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        binding.btnSignup.setOnClickListener{
            val intent=Intent(this,Signup::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            val email=binding.edtEmail.text.toString()
            val password=binding.edtPassword.text.toString()
            login(email,password)
        }
    }
    private fun login(email: String,password:String){
        mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            val intent=Intent(this@Login,MainActivity::class.java)
            startActivity(intent)
        }.addOnFailureListener {  authResult->
            Toast.makeText(this@Login,authResult.localizedMessage,Toast.LENGTH_SHORT).show()
        }
    }

}