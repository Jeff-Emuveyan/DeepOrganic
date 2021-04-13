package com.bellogate.deeporganic.data.user

import com.bellogate.deeporganic.fakes.FakeTask
import com.bellogate.deeporganic.model.User
import com.bellogate.deeporganic.util.USERS
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.`when`


class UserRepositoryTest {


    @Test
    fun `saveUserFromAuthToDatabase() should return null when FirebaseUser is null`()= runBlocking {
        val firebaseFirestore: FirebaseFirestore = mock()


        val userRepository = UserRepository(firebaseFirestore)
        //case 1: saveUserFromAuthToDatabase() should return false when 'authUser' is null
        val result = userRepository.saveUserFromAuthToDatabase(null)

        assertEquals(null, result)
    }

    @Test
    fun `saveUserFromAuthToDatabase() should return true when FirebaseUser is not null`()= runBlocking {
        //https://www.baeldung.com/mockito-spy
        //https://stackoverflow.com/questions/11620103/mockito-trying-to-spy-on-method-is-calling-the-original-method

        val firebaseFirestore: FirebaseFirestore = mock()
        val firebaseUser: FirebaseUser = mock()

        `when`(firebaseUser.displayName).thenReturn("Jeff")
        `when`(firebaseUser.email).thenReturn("Jeff@mail.com")

        val userRepository = UserRepository(firebaseFirestore)
        //Now we need a spy so that we can mock the 'save' method because we only want to test saveUserFromAuthToDatabase():
        val spyUserRepository = spy(userRepository)
        doReturn(true).`when`(spyUserRepository).save(any())

        //case 2: saveUserFromAuthToDatabase() should return true when 'authUser' is not null
        val result = spyUserRepository.saveUserFromAuthToDatabase(firebaseUser)

        assertEquals(result?.name, "Jeff")
        assertEquals(result?.email, "Jeff@mail.com")
    }


    @Test
    fun `save() should return true when db saves a user`() = runBlocking{
        val firebaseFirestore: FirebaseFirestore = mock()
        val collectionReference: CollectionReference = mock()
        val documentReference: DocumentReference = mock()
        val fakeTask = FakeTask()

        `when`(firebaseFirestore.collection(USERS)).thenReturn(collectionReference)
        `when`(collectionReference.document(any())).thenReturn(documentReference)
        `when`(documentReference.set(any())).thenReturn(fakeTask)

        val userRepository = UserRepository(firebaseFirestore)
        val user = User("Jeff", "Jeff@mymail.com")

        val result =  userRepository.save(user)

        assertEquals(true, result)
    }


    @Test
    fun `save() should return false when db could not save a user`() = runBlocking{

        val firebaseFirestore: FirebaseFirestore = mock()

        val userRepository = UserRepository(firebaseFirestore)
        val user = User("Jeff", "Jeff@mymail.com")

        val result =  userRepository.save(user)
        //save should return false because we did not mock how 'firebaseFirestore' should save the user.

        assertEquals(false, result)
    }
}