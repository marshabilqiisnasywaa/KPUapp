package com.example.kpuapp // Ganti dengan nama package yang sesuai


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kpuapp.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userPreferenceManager: UserPreferenceManager // Menggunakan UserPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi UserPreferenceManager
        userPreferenceManager = UserPreferenceManager.getInstance(this)

        with(binding) {
            // Ketika tombol login diklik
            loginButton.setOnClickListener {
                val usernameInput = usernameInput.text.toString() // Mengambil input username
                val passwordInput = passwordInput.text.toString() // Mengambil input password

                // Validasi input
                if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Memeriksa apakah username dan password sesuai
                    if (isValidUserLogin(usernameInput, passwordInput)) {
                        userPreferenceManager.setUserLoggedIn(true)
                        checkLoginStatus()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Username atau password salah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            // Ketika teks untuk registrasi diklik, pindah ke RegisterActivity
            registerText.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    // Fungsi untuk memvalidasi username dan password
    private fun isValidUserLogin(inputUsername: String, inputPassword: String): Boolean {
        val storedUsername = userPreferenceManager.getUserName()
        val storedPassword = userPreferenceManager.getUserPassword()

        return storedUsername == inputUsername && storedPassword == inputPassword
    }

    // Fungsi untuk memeriksa status login
    private fun checkLoginStatus() {
        val isLoggedIn = userPreferenceManager.isUserLoggedIn() // Mengecek status login
        if (isLoggedIn) {
            Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Login gagal", Toast.LENGTH_SHORT).show()
        }
    }
}
