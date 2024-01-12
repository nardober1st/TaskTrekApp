package com.oechslerbernardo.mongodbteste.data

import com.oechslerbernardo.mongodbteste.util.toRealmInstant
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.time.Instant

open class Diary() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    var owner_id: String = ""
    var title: String = ""
    var description: String = ""
    var date: RealmInstant = Instant.now().toRealmInstant()
}
