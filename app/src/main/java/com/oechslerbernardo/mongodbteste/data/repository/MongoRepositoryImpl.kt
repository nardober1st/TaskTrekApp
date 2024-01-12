package com.oechslerbernardo.mongodbteste.data.repository

import android.util.Log
import com.oechslerbernardo.mongodbteste.data.Diary
import com.oechslerbernardo.mongodbteste.domain.repository.Diaries
import com.oechslerbernardo.mongodbteste.domain.repository.MongoRepository
import com.oechslerbernardo.mongodbteste.util.Constants.APP_ID
import com.oechslerbernardo.mongodbteste.util.RequestState
import com.oechslerbernardo.mongodbteste.util.Resource
import com.oechslerbernardo.mongodbteste.util.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import java.time.ZoneId

object MongoRepositoryImpl : MongoRepository {

    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm

    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if (user != null) {
            try {
                Log.d("TAGY", "Configuring Realm for user: ${user.id}")
                val config = SyncConfiguration.Builder(user, setOf(Diary::class))
                    .initialSubscriptions { sub ->
                        add(
                            query = sub.query<Diary>("owner_id == $0", user.id),
                            name = "User's Diaries"
                        )
                    }
                    .log(LogLevel.ALL)
                    .build()
                realm = Realm.open(config)
                Log.d("TAGY", "Realm configured for user: ${user.id}")
            } catch (e: Exception) {
                Log.d("TAGY", "Error initializing Realm: ${e.message}")
            }
        }
    }

    override fun getAllDiaries(): Flow<Diaries> {
        return if (user != null) {
            try {
                realm.query<Diary>(query = "owner_id == $0", user.id)
                    .sort(property = "date", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .map { result ->
                        Log.d("TAGY", "Diaries fetched: ${result.list}")
                        RequestState.Success(
                            data = result.list.groupBy {
                                it.date.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                        )
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { RequestState.Error(UserNotAuthenticatedException()) }
        }
    }

    override fun getSelectedDiary(diaryId: ObjectId): Flow<RequestState<Diary>> {
        Log.d("MongoRepositoryImpl", "Fetching diary with id: $diaryId")
        return if (user != null) {
            try {
                realm.query<Diary>("_id == $0", diaryId)
                    .asFlow()
                    .map { result ->
                        val diary = result.list.firstOrNull()
                        if (diary != null) {
                            Log.d("TAGUY", "Diary found with id: $diaryId")
                            RequestState.Success(diary)
                        } else {
                            Log.d("TAGUY", "No diary found with id: $diaryId")
                            RequestState.Error(NoSuchElementException("Diary not found with ID: $diaryId"))
                        }
                    }
            } catch (e: Exception) {
                Log.e("TAGUY", "Error fetching diary with id: $diaryId", e)
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            Log.d("TAGUY", "User not authenticated")
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override suspend fun insertDiary(diary: Diary): RequestState<Diary> {
        return if (user != null) {
            realm.write {
                try {
                    val addedDiary = copyToRealm(diary.apply { owner_id = user.id })
                    RequestState.Success(data = addedDiary)
                } catch (e: Exception) {
                    RequestState.Error(e)
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun updateDiary(diary: Diary): RequestState<Diary> {
        return if (user != null) {
            realm.write {
                val queriedDiary = query<Diary>(query = "_id == $0", diary._id).first().find()
                if (queriedDiary != null) {
                    queriedDiary.title = diary.title
                    queriedDiary.description = diary.description
                    queriedDiary.date = diary.date
                    RequestState.Success(queriedDiary)
                } else {
                    RequestState.Error(error = Exception("Queried Diary does not exist"))
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun deleteDiary(id: io.realm.kotlin.types.ObjectId): RequestState<Diary> {
        return if (user != null) {
            realm.write {
                val diary = query<Diary>(query = "_id == $0 AND owner_id == $1", id, user.id)
                    .first().find()
                if (diary != null) {
                    try {
                        delete(diary)
                        Log.d("TAGUY", "Diary successfully deleted with id: $id")
                        RequestState.Success(data = diary)
                    } catch (e: Exception) {
                        Log.e("TAGUY", "Error deleting diary with id: $id", e)
                        RequestState.Error(e)
                    }
                } else {
                    Log.d("TAGUY", "Diary not found for deletion with id: $id")
                    RequestState.Error(Exception("Diary does not exist"))
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }
}

private class UserNotAuthenticatedException : Exception("User is not Logged in.")