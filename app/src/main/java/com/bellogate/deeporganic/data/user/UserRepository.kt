package com.bellogate.deeporganic.data.user

import com.bellogate.deeporganic.data.BaseRepository
import com.bellogate.deeporganic.model.User
import com.bellogate.deeporganic.util.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Provides
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


open class UserRepository @Inject constructor (var db: FirebaseFirestore):  BaseRepository() {

    /*** This will retrieve the user's details from Auth and save them to the FireStoreDatabase ***/
    open suspend fun saveUserFromAuthToDatabase(authUser: FirebaseUser?): Boolean{
        if(authUser != null){
            val newUser = User(name = authUser.displayName ?: "You", email = authUser.email!!)
            return save(newUser)
        }
        return false
    }

    open suspend fun save(user: User): Boolean = try{
        db.collection(USERS).document(user.timeCreated.toString()).set(user).await()
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
}