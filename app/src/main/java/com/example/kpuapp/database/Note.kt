package com.example.kpuapp.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,

    @ColumnInfo(name = "nama")
    val namaVoter: String,

    @ColumnInfo(name = "nik")
    val nik: String,

    @ColumnInfo(name = "gender")
    val gender: String,

    @ColumnInfo(name = "alamat")
    val alamat: String
)
