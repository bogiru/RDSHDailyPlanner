package com.bogiruapps.rdshapp.school


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.FirestoreRepository
import com.bogiruapps.rdshapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_choose_school.view.*

/**
 * A simple [Fragment] subclass.
 */
class ChooseSchoolFragment : Fragment() {

    private lateinit var schoolViewModel: SchoolViewModel
    private lateinit var school: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_choose_school, container, false)
        val repository = FirestoreRepository.getInstance(activity!!.application)

        val factory = SchoolViewModelFactory(repository)
        schoolViewModel = ViewModelProviders.of(this, factory).get(SchoolViewModel::class.java)
        schoolViewModel.loadSchools()
        schoolViewModel.isFinishLoad.observe(viewLifecycleOwner, Observer {isFinish ->
            if (isFinish) {
                view.spinner.adapter =
                    ArrayAdapter(view.context, R.layout.support_simple_spinner_dropdown_item, schoolViewModel.schools)
                hideProgress(view)

                view.spinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        school = schoolViewModel.schools[position]
                    }

                }

                view.btn_choose.setOnClickListener {
                    schoolViewModel.setSchool(school)
                    findNavController().navigate(R.id.noticeFragment)
                }

            }
        })
    return view
}

    private fun hideProgress(view : View) {
        view.spinner.visibility = View.VISIBLE
        view.btn_choose.visibility = View.VISIBLE
        view.choose_progress_bar.visibility = View.INVISIBLE
    }

}
