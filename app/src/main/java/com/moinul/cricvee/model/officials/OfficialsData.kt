package com.moinul.cricvee.model.officials

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="officials_table")
data class OfficialsData(
    val country_id: Int?,
    val dateofbirth: String?,
    val firstname: String?,
    val fullname: String?,
    val gender: String?,
    @PrimaryKey
    val id: Int,
    val lastname: String?,
    val resource: String?,
    val updated_at: String?
){
    constructor():this(
        0,
    null,
    null,
    null,
    null,
    0,
    null,
    null,
    null
    )
}