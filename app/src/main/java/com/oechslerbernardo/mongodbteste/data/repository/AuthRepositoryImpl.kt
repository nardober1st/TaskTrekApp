package com.oechslerbernardo.mongodbteste.data.repository

import android.util.Log
import com.oechslerbernardo.mongodbteste.data.Diary
import com.oechslerbernardo.mongodbteste.domain.repository.AuthRepository
import com.oechslerbernardo.mongodbteste.util.Constants
import com.oechslerbernardo.mongodbteste.util.Resource
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.internal.interop.ErrorCode
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val app: App
) : AuthRepository {

    private val user = app.currentUser
    private lateinit var realm: Realm

    override fun signup(email: String, password: String): Flow<Resource<String>> {
        return flow {
            val emailPattern = android.util.Patterns.EMAIL_ADDRESS
            val isValidEmail = emailPattern.matcher(email).matches()

            if (email.isEmpty() || password.isEmpty()) {
                emit(Resource.Error("Email and password cannot be empty!"))
            } else if (!isValidEmail) {
                emit(Resource.Error("Invalid email format!"))
            } else {
                emit(Resource.Loading())
                try {
                    app.emailPasswordAuth.registerUser(email, password)
                    // Attempt to log in the user immediately after successful registration
//                    app.login(Credentials.emailPassword(email, password))
                    emit(Resource.Success("User registered successfully!"))
                } catch (e: Exception) {
                    emit(Resource.Error(e.message ?: "Error during login"))
                }
            }
        }
    }

    override fun login(email: String, password: String): Flow<Resource<String>> {
        return flow {
            val emailPattern = android.util.Patterns.EMAIL_ADDRESS
            val isValidEmail = emailPattern.matcher(email).matches()
            if (email.isEmpty() || password.isEmpty()) {
                emit(Resource.Error("Email and password cannot be empty!"))
            } else if (!isValidEmail) {
                emit(Resource.Error("Invalid email format!"))
            } else {
                emit(Resource.Loading())

                try {
                    Log.d("TAGY", "Starting login process for email: $email")
                    app.login(Credentials.emailPassword(email, password))
                    emit(Resource.Success("Logged in successfully!"))
                    Log.d("TAGY", "Login successful for email: $email")
                } catch (e: Exception) {
                    emit(Resource.Error("Unkown error!"))
                }
            }
        }
    }

    // Function to perform logout
    override fun logout(): Flow<Resource<Unit>> {
        return flow {
            Log.d("TAGY", "Starting logout process")
            emit(Resource.Loading()) // Emit loading state
            try {
                app.currentUser?.logOut()
                emit(Resource.Success(Unit)) // Emit success state
                Log.d("TAGY", "Logout successful")
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Logout failed")) // Emit error state
            }
        }
    }
}