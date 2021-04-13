package com.bellogate.deeporganic

import com.bellogate.deeporganic.data.user.UserRepository
import com.bellogate.deeporganic.fakes.FakeFirebaseUiException
import com.bellogate.deeporganic.model.User
import com.bellogate.deeporganic.util.SIGN_UP_LOGIN_REQUEST_CODE
import com.bellogate.deeporganic.util.common.signup.SignUpResponseHandler
import com.firebase.ui.auth.FirebaseUiException
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SignUpResponseHandlerTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun saveUserTest() = runBlocking(){

        //set up:
        val userRepository: UserRepository = mock()
        val user = User("Jeff", "jeffemuveyan@gmail.com")
        Mockito.`when`(userRepository.saveUserFromAuthToDatabase(any())).thenReturn(user)
        val firebaseUser: FirebaseUser = mock()
        Mockito.`when`(firebaseUser.displayName).thenReturn("Jeff")
        Mockito.`when`(firebaseUser.email).thenReturn("Email")

        val signUpResponseHandler = SignUpResponseHandler(userRepository, firebaseUser)
        val response: IdpResponse = mock()
        Mockito.`when`(response.isNewUser).thenReturn(true)

        var test: Int? = null

        //when resultCode = -1, the authentication was successful, therefore onSuccess() should be
        // called (this should, in turn set 'test' to 100.
        signUpResponseHandler.processResponse(requestCode = SIGN_UP_LOGIN_REQUEST_CODE, -1, response,
            success = {
                test = 100
            },cancelled = {
                test = 200
            },networkError = {
                test = 300
            }, unknownError ={
                test = 400
            })

        assertEquals(100, test)

        //when the result code is anything expect a -1 and response = null, it means the authentication
        // failed because the user cancelled, therefore onCancelled() should be called (this should, in turn set 'test' to 200.
        signUpResponseHandler.processResponse(requestCode = SIGN_UP_LOGIN_REQUEST_CODE, -99, null,
            success = {
                test = 100
            },cancelled = {
                test = 200
            },networkError = {
                test = 300
            }, unknownError ={
                test = 400
            })

        assertEquals(200, test)


        //when the result code is anything expect a -1 and response is not null, it means the authentication
        //failed because network (the response error code will tell us this, therefore networkError()
        //should be called (this should, in turn set 'test' to 300.
        Mockito.`when`(response.error).thenReturn(FakeFirebaseUiException())
        signUpResponseHandler.processResponse(requestCode = SIGN_UP_LOGIN_REQUEST_CODE, -99, response,
            success = {
                test = 100
            },cancelled = {
                test = 200
            },networkError = {
                test = 300
            }, unknownError ={
                test = 400
            })

        assertEquals(300, test)
    }
}