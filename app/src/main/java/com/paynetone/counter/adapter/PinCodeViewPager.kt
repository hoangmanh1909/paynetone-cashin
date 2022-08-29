package com.paynetone.counter.adapter

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PinCodeViewPager(fm: FragmentManager, private val listFragment: List<Fragment>, private var listTitle:ArrayList<String>?=null) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getCount(): Int {
        return listFragment.size
    }

    override fun saveState(): Parcelable? {
        return null
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle?.get(position)
    }

}