package com.oechslerbernardo.mongodbteste.domain.repository

import com.oechslerbernardo.mongodbteste.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun signup(email: String, password: String): Flow<Resource<String>>
    fun login(email: String, password: String): Flow<Resource<String>>
    fun logout(): Flow<Resource<Unit>>
}