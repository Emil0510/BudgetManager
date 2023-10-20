package com.emilabdurahmanli.budgetmanager.ui.activity.sign_in_log_in

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emilabdurahmanli.budgetmanager.databinding.ActivityLoginSignupBinding

class LoginSignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginSignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}