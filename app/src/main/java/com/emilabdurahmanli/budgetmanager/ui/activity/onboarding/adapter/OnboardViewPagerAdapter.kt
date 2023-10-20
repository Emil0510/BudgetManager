package com.emilabdurahmanli.budgetmanager.ui.activity.onboarding.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.emilabdurahmanli.budgetmanager.R


class OnboardViewPagerAdapter : PagerAdapter() {


    private var listImage = mutableListOf<Int>(R.drawable.coins_and_banknotes_in_the_air,
    R.drawable.girl_with_piggy_bank_and_coins,
    R.drawable.target_and_money,
    R.drawable.brown_wallet_with_money)
    override fun getCount(): Int {
        return 4
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }


    @SuppressLint("MissingInflatedId")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.onboard_view_pager_item,container,false)
        view.findViewById<ImageView>(R.id.onboardIV).setImageResource(listImage[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }
}