package com.bellogate.deeporganic.util.common.signup
import android.util.Log
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import com.bellogate.deeporganic.data.user.UserRepository
import com.bellogate.deeporganic.util.SIGN_UP_LOGIN_REQUEST_CODE
import com.bellogate.deeporganic.util.common.SessionManager
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@Keep
class SignUpResponseHandler @Inject constructor (val userRepository: UserRepository,
                                                 var authUser: FirebaseUser?) {

    suspend fun processResponse(requestCode: Int,
                                resultCode: Int,
                                response: IdpResponse?,
                                success: ()->Unit,
                                cancelled: (()->Unit)? = null,
                                networkError: ()->Unit,
                                unknownError: ()->Unit){

        if (requestCode == SIGN_UP_LOGIN_REQUEST_CODE) {
            when {
                resultCode == -1 -> {
                    // Successfully signed in
                    saveUser(response, success)
                }
                response == null -> {
                    //User pressed back button to cancel the process
                    cancelled?.invoke()
                }
                response.error!!.errorCode == ErrorCodes.NO_NETWORK -> {
                    //Network error
                    networkError.invoke()
                }
                else ->{
                    //Unknown error
                    unknownError.invoke()
                }
            }
        }
    }


    private suspend fun saveUser(response: IdpResponse?, success: ()->Unit){
        if(response == null) return

        if(response.isNewUser) {
            // Successfully signed up. Save this user (gotten from authentication to the fire store:
            userRepository.saveUserFromAuthToDatabase(authUser)
            success.invoke()
        }else{
            //this was a Sign-in operation because the user already existed, so:
            // fetch the user's details from FireStore so that can have a local copy of this user's
            // data:
            val user = userRepository.getUser(response.email!!)
            SessionManager.currentUser = user
            success.invoke()
        }
    }

}