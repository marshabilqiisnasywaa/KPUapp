package com.example.kpuapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kpuapp.database.Note
import com.example.kpuapp.database.NoteDao
import com.example.kpuapp.database.NoteRoomDatabase
import com.example.kpuapp.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userPreferenceManager: UserPreferenceManager
    private lateinit var noteDao: NoteDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UserPreferenceManager and check login status
        userPreferenceManager = UserPreferenceManager.getInstance(this)
        checkLoginStatus()

        // Initialize Executor Service for background tasks
        executorService = Executors.newSingleThreadExecutor()

        // Initialize the database and DAO
        val db = NoteRoomDatabase.getDatabase(this)
        noteDao = db!!.noteDao()

        // Set up RecyclerView
        with(binding) {
            listPemilih.layoutManager = LinearLayoutManager(this@MainActivity)

            // Observe LiveData from DAO and update RecyclerView
            noteDao.allNotes.observe(this@MainActivity, Observer { pemilihList ->
                val adapter = voterAdapter(
                    pemilihList,
                    onDeleteClick = { position ->
                        val note = pemilihList[position]
                        executorService.execute {
                            noteDao.delete(note)
                        }
                        Toast.makeText(
                            this@MainActivity,
                            "Data deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onEditClick = { position ->
                        val pemilih = pemilihList[position]
                        val intent = Intent(this@MainActivity, Edit::class.java)
                        intent.putExtra("note_id", pemilih.id)
                        startActivity(intent)
                    },
                    onViewClick = { position ->
                        val pemilih = pemilihList[position]
                        val intent = Intent(this@MainActivity, LihatData::class.java)
                        intent.putExtra("note_id", pemilih.id)
                        startActivity(intent)
                    }
                )
                listPemilih.adapter = adapter
            })
        }

        // Handle "Tambah Data" button click
        binding.tambahDataButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, TambahData::class.java))
        }

        // Handle "Logout" button click
        binding.logoutButton.setOnClickListener {
            userPreferenceManager.setUserLoggedIn(false)
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }

    // Check login status
    private fun checkLoginStatus() {
        val isLoggedIn = userPreferenceManager.isUserLoggedIn()
        if (!isLoggedIn) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }
}
