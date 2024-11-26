package com.example.kpuapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kpuapp.database.NoteDao
import com.example.kpuapp.database.NoteRoomDatabase
import com.example.kpuapp.databinding.ActivityLihatDataBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LihatData : AppCompatActivity() {

    private lateinit var binding: ActivityLihatDataBinding
    private lateinit var noteDao: NoteDao
    private var noteId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLihatDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize DAO
        val db = NoteRoomDatabase.getDatabase(this)
        noteDao = db?.noteDao()!!

        // Get the note ID passed via Intent
        noteId = intent.getIntExtra("note_id", 0)

        // Load and display voter data
        loadPemilihData()

        // Set up "Kembali" button click listener
        binding.btnKembali.setOnClickListener {
            startActivity(Intent(this@LihatData, MainActivity::class.java))
            finish()
        }
    }

    private fun loadPemilihData() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Collect data from Flow
            noteDao.getVoterById(noteId).collect { pemilih ->
                withContext(Dispatchers.Main) {
                    // Check if the data is not null and update UI
                    binding.etNamaPemilih.setText(pemilih.namaVoter)
                    binding.etNIK.setText(pemilih.nik)
                    binding.etJenisKelamin.setText(pemilih.gender)
                    binding.etAlamat.setText(pemilih.alamat)
                }
            }
        }
    }
}
