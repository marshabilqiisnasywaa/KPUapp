package com.example.kpuapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    // Menyisipkan data. Jika ada ID yang sama, data tidak akan dimasukkan
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: Note)

    // Mengupdate data
    @Update
    fun update(note: Note)

    // Menghapus data
    @Delete
    fun delete(note: Note)

    // Mengambil semua data dalam tabel 'note_table' dan mengurutkan berdasarkan id secara ascending
    @get:Query("SELECT * FROM note_table ORDER BY id ASC")
    val allNotes: LiveData<List<Note>>

    // Mengambil data berdasarkan id tertentu
    @Query("SELECT * FROM note_table WHERE id = :id")
    fun getVoterById(id: Int): Flow<Note> // Menggunakan 'Note' bukan 'Pemilih'
}
