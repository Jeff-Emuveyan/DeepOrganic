package com.bellogate.deeporganic.data.user

import android.content.Context
import android.util.Log
import com.bellogate.deeporganic.data.BaseRepository
import com.bellogate.deeporganic.model.User
import com.bellogate.deeporganic.util.USERS
import com.bellogate.deeporganic.util.common.SessionManager
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import dagger.Provides
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


open class UserRepository @Inject constructor (var db: FirebaseFirestore,
                                               var firebaseAuth: FirebaseAuth,
                                               var authUI: AuthUI):  BaseRepository() {

    /*** This will retrieve the user's details from Auth and save them to the FireStoreDatabase ***/
    open suspend fun saveUserFromAuthToDatabase(authUser: FirebaseUser?): User?{
        if(authUser != null){
            val newUser = User(name = authUser.displayName ?: "You", email = authUser.email!!)
            if (save(newUser)) return newUser
        }
        return null
    }

    open suspend fun save(user: User): Boolean = try{
        db.collection(USERS).document(user.timeCreated.toString()).set(user).await()
        SessionManager.currentUser = user
        true
    }catch (e: Exception){
        false
    }

    suspend fun getUser(userEmail: String): User? {
        val response = db.collection(USERS).whereEqualTo("email", userEmail).limit(1).get().await()
        for (document in response.documents) {//this will only have one document because we set the limit to 1
            return document.toObject(User::class.java)
        }
        return null
    }


    suspend fun signOut(context: Context){
        authUI.signOut(context).await()
        //this call will cause listenForUserSignOut() to trigger (successful or not)
    }

    /**
     * A listener to notify us if the user has signed out of Firebase*
     * ***/
    fun listenForUserSignOut(userIsLoggedIn: (Boolean)->Unit) =
        firebaseAuth.addAuthStateListener {
            if(it.currentUser == null){
                userIsLoggedIn.invoke(false)
            }else{
                userIsLoggedIn.invoke(true)
            }
        }

    fun deleteUser(email: String?) {

    }
}