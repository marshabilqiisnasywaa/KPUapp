package com.example.kpuapp // Ganti dengan nama package yang sesuai


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kpuapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userPreferenceManager: UserPreferenceManager // Menggunakan UserPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi UserPreferenceManager
        userPreferenceManager = UserPreferenceManager.getInstance(this)

        with(binding) {
            // Ketika tombol register diklik
            RegisterButton.setOnClickListener {
                val usernameInput = usernameInput.text.toString() // Mengambil input username
                val passwordInput = passwordInput.text.toString() // Mengambil input password
                val confirmPasswordInput = confirmPasswordInput.text.toString() // Mengambil input konfirmasi password

                // Validasi input
                if (usernameInput.isEmpty() || passwordInput.isEmpty() || confirmPasswordInput.isEmpty()) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (passwordInput != confirmPasswordInput) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Password tidak sama",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Simpan data ke SharedPreferences menggunakan UserPreferenceManager
                    userPreferenceManager.saveUserName(usernameInput)
                    userPreferenceManager.saveUserPassword(passwordInput)
                    userPreferenceManager.setUserLoggedIn(true)

                    // Periksa status registrasi
                    checkRegistrationStatus()
                }
            }

            // Ketika teks untuk login diklik, pindah ke LoginActivity
            loginText.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
    }

    // Fungsi untuk memeriksa status registrasi
    private fun checkRegistrationStatus() {
        val isRegistered = userPreferenceManager.isUserLoggedIn() // Mengecek status login
        if (isRegistered) {
            Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Registrasi gagal", Toast.LENGTH_SHORT).show()
        }
    }
}
