package com.bangkit.elevate.data.firebase

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bangkit.elevate.data.IdeaEntity
import com.bangkit.elevate.data.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FirebaseServices {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestoreRef: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userRef: CollectionReference = firestoreRef.collection("User")
    private val ideasRef: CollectionReference = firestoreRef.collection("Ideas")

    fun uploadFiles(uri: Uri, uid: String, type: String, name: String): LiveData<String> {
        val mStorage: FirebaseStorage = Firebase.storage
        val storageRef = mStorage.reference
        val fileRef = storageRef.child("$uid/$type/$name")
        val downloadUrl = MutableLiveData<String>()

        fileRef.putFile(uri).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            fileRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                downloadUrl.postValue(downloadUri.toString())
                Log.d("uploadFiles: ", downloadUri.toString())
            } else {
                task.exception?.let {
                    throw it
                }
            }
        }
        return downloadUrl
    }

    fun signInWithGoogle(idToken: String): LiveData<UserEntity> {
        val authenticatedUser = MutableLiveData<UserEntity>()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        CoroutineScope(IO).launch {
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val isNewUser = task.result?.additionalUserInfo?.isNewUser
                        val user: FirebaseUser? = firebaseAuth.currentUser
                        if (user != null) {
                            val uid = user.uid
                            val name = user.displayName
                            val email = user.email
                            val avatar = user.photoUrl
                            val phone = user.phoneNumber
                            val userInfo = UserEntity(
                                false,
                                null,
                                avatar.toString(),
                                0,
                                email,
                                phone,
                                uid,
                                name
                            )
                            userInfo.isNew = isNewUser
                            authenticatedUser.postValue(userInfo)
                        }
                    } else {
                        Log.d("Error Authentication", "signInWithGoogle: ", task.exception)
                    }
                }
        }
        return authenticatedUser
    }

    fun createUserToFirestore(authUser: UserEntity): LiveData<UserEntity> {
        val createdUserData = MutableLiveData<UserEntity>()
        CoroutineScope(IO).launch {
            val docRef: DocumentReference = userRef.document(authUser.uid.toString())
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document: DocumentSnapshot? = task.result
                    if (document?.exists() == false) {
                        docRef.set(authUser).addOnCompleteListener {
                            if (it.isSuccessful) {
                                authUser.isCreated = true
                                authUser.isNew = false
                                createdUserData.postValue(authUser)
                            } else {
                                Log.d(
                                    "errorCreateUser: ",
                                    it.exception?.message.toString()
                                )
                            }
                        }
                    }
                }
            }
                .addOnFailureListener {
                    Log.d("ErrorGetUser: ", it.message.toString())
                }
        }
        return createdUserData
    }

    fun uploadIdeaData(ideaData: IdeaEntity, uid: String): LiveData<Boolean> {
        val isSuccessful = MutableLiveData<Boolean>()
        CoroutineScope(IO).launch {
            val docRef: DocumentReference = ideasRef.document(uid)
            docRef.set(ideaData, SetOptions.merge()).addOnCompleteListener { uploadIdea ->
                if (uploadIdea.isSuccessful) {
                    val updateUserRef: DocumentReference = userRef.document(uid)
                    updateUserRef.update("activeIdea", true)
                        .addOnSuccessListener {
                            isSuccessful.postValue(true)
                            Log.d("Update Active Idea: ", "Success!")
                        }.addOnFailureListener { e ->
                            isSuccessful.postValue(false)
                            Log.d("Update Active Idea", "Error Updating Document: ", e)
                        }
                } else {
                    isSuccessful.postValue(false)
                    Log.d(
                        "errorUpdateProfile: ",
                        uploadIdea.exception?.message.toString()
                    )
                }
            }
                .addOnFailureListener {
                    isSuccessful.postValue(false)
                    Log.d(
                        "errorCreateUser: ", it.message.toString()
                    )
                }
        }
        return isSuccessful
    }

    fun editUserProfile(authUser: UserEntity): LiveData<UserEntity> {
        val editedUserData = MutableLiveData<UserEntity>()
        CoroutineScope(IO).launch {
            val docRef: DocumentReference = userRef.document(authUser.uid.toString())
            docRef.set(authUser, SetOptions.merge()).addOnCompleteListener {
                if (it.isSuccessful) {
                    authUser.isCreated = true
                    authUser.isNew = false
                    editedUserData.postValue(authUser)
                } else {
                    Log.d(
                        "errorUpdateProfile: ",
                        it.exception?.message.toString()
                    )
                }
            }
                .addOnFailureListener {
                    Log.d(
                        "errorCreateUser: ", it.message.toString()
                    )
                }
        }
        return editedUserData
    }

    fun getUserProfile(uid: String): LiveData<UserEntity> {
        val docRef: DocumentReference = userRef.document(uid)
        val userProfileData = MutableLiveData<UserEntity>()
        CoroutineScope(IO).launch {
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val userProfile = document.toObject<UserEntity>()
                    userProfileData.postValue(userProfile!!)
                    Log.d("getUserProfile: ", userProfile.toString())
                } else {
                    Log.d("Error getting Doc", "Document Doesn't Exist")
                }
            }
                .addOnFailureListener {

                }
        }
        return userProfileData
    }

    fun getIdeaData(uid: String): LiveData<IdeaEntity> {
        val docRef: DocumentReference = ideasRef.document(uid)
        val ideaData = MutableLiveData<IdeaEntity>()
        CoroutineScope(IO).launch {
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val userIdeaData = document.toObject<IdeaEntity>()
                    ideaData.postValue(userIdeaData!!)
                    Log.d("getUserProfile: ", userIdeaData.toString())
                } else {
                    Log.d("Error getting Doc", "Document Doesn't Exist")
                }
            }
                .addOnFailureListener {

                }
        }
        return ideaData
    }

}