package com.emilabdurahmanli.budgetmanager.ui.activity.onboarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.ui.activity.onboarding.adapter.OnboardViewPagerAdapter
import com.emilabdurahmanli.budgetmanager.databinding.ActivityOnboardBinding
import com.emilabdurahmanli.budgetmanager.ui.activity.sign_in_log_in.LoginSignupActivity

class OnboardActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityOnboardBinding
    private var position = -1

    private var listString = mutableListOf<String>(
        "Take Control of Your Finances with Budget Genius",
        "Stay on top of your finances and achieve your financial goals",
        "Eliminate financial stress and never overspend again",
        "Secure your financial future and take charge of your money"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE)
        val isFirst = sharedPreferences.getBoolean("isFirst", true)
        if (isFirst) {
            binding.onboardTV.setText(listString[0])
            binding.onboardViewPager.adapter = OnboardViewPagerAdapter()
            binding.previousButton.visibility = View.GONE

            binding.nextButton.setOnClickListener {
                if (position == 3) {
                    startActivity(Intent(this@OnboardActivity, LoginSignupActivity::class.java))
                    finish()
                } else {
                    binding.onboardViewPager.setCurrentItem(getItem(+1), true)
                }
            }
            binding.previousButton.setOnClickListener {
                binding.onboardViewPager.setCurrentItem(getItem(-1), true)
            }

            binding.onboardViewPager.addOnPageChangeListener(object :
                ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    this@OnboardActivity.position = position
                    binding.onboardTV.setText(listString[position])
                    if (position != 0) {
                        binding.previousButton.visibility = View.VISIBLE
                    } else {
                        binding.previousButton.visibility = View.GONE
                    }
                    if (position == 3) {
                        binding.nextButton.setImageResource(R.drawable.button_navigation_conturi_finish)
                    } else {
                        binding.nextButton.setImageResource(R.drawable.button_navigation_conturi)
                    }

                }

                override fun onPageScrollStateChanged(state: Int) {

                }

            })
            binding.onboardDotsIndicator.attachTo(binding.onboardViewPager)

        }else{
            val intent = Intent(this, LoginSignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getItem(i: Int): Int {
        return binding.onboardViewPager.getCurrentItem() + i;
    }
}