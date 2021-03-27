package com.bellogate.deeporganic.util.common.signup
import android.util.Log
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import com.bellogate.deeporganic.util.SIGN_UP_LOGIN_REQUEST_CODE
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import javax.inject.Inject

@Keep
class SignUpResponseHandler @Inject constructor() {

    companion object {
        private val name = SignUpResponseHandler::class.java.simpleName
    }

    private var response: IdpResponse? = null

    fun processResponse(requestCode: Int,
                        resultCode: Int,
                        response: IdpResponse?,
                        success: ()->Unit,
                        cancelled: (()->Unit)? = null,
                        networkError: ()->Unit,
                        unknownError: ()->Unit){

        if (requestCode == SIGN_UP_LOGIN_REQUEST_CODE) {
            // Successfully signed in
            if (resultCode == AppCompatActivity.RESULT_OK) {
                Log.d(name, "Sign-in/Login successful ")
                success.invoke()
            } else {
                // Sign in failed
                if (response == null) {
                    //User pressed back button
                    Log.d(name, "Sign-in/Login cancelled by user ")
                    cancelled?.invoke()
                    return
                }
                if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                    //Network error
                    Log.d(name, "Sign-in/Login failed due to network error")
                    networkError.invoke()
                    return
                }
                //Unknown error
                Log.d(name, "Sign-in error: ", response.error)
                unknownError.invoke()
            }
        }
    }
}