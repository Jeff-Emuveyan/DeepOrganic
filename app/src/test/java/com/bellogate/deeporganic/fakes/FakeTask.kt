package com.bellogate.deeporganic.fakes

import android.app.Activity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import java.lang.Exception
import java.util.concurrent.Executor

open class FakeTask: Task<Void>() {

    override fun isComplete(): Boolean {
        return true
    }

    override fun isSuccessful(): Boolean {
        return true
    }

    override fun isCanceled(): Boolean {
        return false
    }

    override fun getResult(): Void? {
        return null
    }

    override fun <X : Throwable?> getResult(p0: Class<X>): Void? {
        return null
    }

    override fun getException(): Exception? {
        return null
    }

    override fun addOnSuccessListener(p0: OnSuccessListener<in Void>): Task<Void> {
        return  this
    }

    override fun addOnSuccessListener(p0: Executor, p1: OnSuccessListener<in Void>): Task<Void> {
        return  this
    }

    override fun addOnSuccessListener(p0: Activity, p1: OnSuccessListener<in Void>): Task<Void> {
        return  this
    }

    override fun addOnFailureListener(p0: OnFailureListener): Task<Void> {
        return  this
    }

    override fun addOnFailureListener(p0: Executor, p1: OnFailureListener): Task<Void> {
        return  this
    }

    override fun addOnFailureListener(p0: Activity, p1: OnFailureListener): Task<Void> {
        return  this
    }
}