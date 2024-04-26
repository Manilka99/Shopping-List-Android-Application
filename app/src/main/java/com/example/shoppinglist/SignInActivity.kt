package com.example.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignInAccount


import com.google.android.gms.common.api.ApiException

class SignInActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    companion object {
        const val RC_SIGN_IN = 9001
        const val TAG = "SignInActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        // Set up Google Sign-In button
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account?.idToken)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=${e.statusCode}")
            Toast.makeText(this, "Sign-in failed. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    // Proceed to main activity or perform necessary actions
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
