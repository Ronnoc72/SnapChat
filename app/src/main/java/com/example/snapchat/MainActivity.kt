package com.example.snapchat

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        if (mAuth.currentUser != null) {
            logIn()
        }
    }

    fun goClick(view: View) {
        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    logIn()
                } else {
                    mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
                        .addOnCompleteListener(this) { test ->
                            if (test.isSuccessful) {
                                FirebaseDatabase.getInstance().reference.child("users").child(test.result?.user!!.uid)
                                    .child("email").setValue(emailEditText?.text.toString())
                                logIn()
                            } else {
                                Toast.makeText(this, "Login Failed Try Again.", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
    }

    fun logIn() {
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }
}