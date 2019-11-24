package com.bogiruapps.rdshapp.school


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogiruapps.rdshapp.R

/**
 * A simple [Fragment] subclass.
 */
class ChooseSchoolFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chose_school, container, false)
    }


}
