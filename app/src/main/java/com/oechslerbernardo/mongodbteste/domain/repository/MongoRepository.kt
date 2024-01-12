package com.oechslerbernardo.mongodbteste.domain.repository

import com.oechslerbernardo.mongodbteste.data.Diary
import com.oechslerbernardo.mongodbteste.util.RequestState
import com.oechslerbernardo.mongodbteste.util.Resource
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

typealias Diaries = RequestState<Map<LocalDate, List<Diary>>>

interface MongoRepository {

    fun configureTheRealm()
    fun getAllDiaries(): Flow<Diaries>
    fun getSelectedDiary(diaryId: ObjectId): Flow<RequestState<Diary>>
    suspend fun insertDiary(diary: Diary): RequestState<Diary>
    suspend fun updateDiary(diary: Diary): RequestState<Diary>
    suspend fun deleteDiary(id: io.realm.kotlin.types.ObjectId): RequestState<Diary>
}