package com.bellogate.deeporganic
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bellogate.deeporganic.util.SIGN_UP_LOGIN_REQUEST_CODE
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

val name = MainActivity::class.java.simpleName

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        //turn off night mode:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //remove action bar:
        actionBar?.hide()

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_UP_LOGIN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                Log.d(name, "Sign-in/Login successful ")

            } else {
                // Sign in failed
                if (response == null) {
                    //User pressed back button
                    Log.d(name, "Sign-in/Login cancelled by user ")
                    return
                }
                if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                    //Network error
                    Log.d(name, "Sign-in/Login failed due to network error")
                    return
                }
                //Unknown error
                Log.d(name, "Sign-in error: ", response.error)
            }
        }
    }
}