package com.bogiruapps.rdshapp.school


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.EventObserver
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentChooseSchoolBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class ChooseSchoolFragment : Fragment() {

    private lateinit var binding: FragmentChooseSchoolBinding
    private val schoolViewModel: SchoolViewModel by viewModel()

    private lateinit var spinner: Spinner
    private lateinit var btnChoose: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var chosenSchool: School


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        setupObserverViewModel()
    return binding.root
}

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_school, container, false)
        binding.viewModel = schoolViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        btnChoose = binding.btnNext
        progressBar = binding.chooseProgressBar
    }

    private fun setupObserverViewModel() {
        schoolViewModel.dataLoading.observe(viewLifecycleOwner, Observer { isLoad ->
            if (isLoad) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.INVISIBLE
            }
        })

        schoolViewModel.regions.observe(viewLifecycleOwner, Observer { regions ->
            showRegionAutoCompleteTextView(regions)
        })

        schoolViewModel.cities.observe(viewLifecycleOwner, Observer { cities ->
            showCitiesAutoCompleteTextView(cities)
        })

        schoolViewModel.schools.observe(viewLifecycleOwner, Observer { schools ->
            showSchoolsAutoCompleteTextView(schools)
        })

        schoolViewModel.showNextButton.observe(viewLifecycleOwner, EventObserver {
            binding.btnNext.visibility = View.VISIBLE
        })

        schoolViewModel.resetAdress.observe(viewLifecycleOwner, EventObserver {
            resetAutoCompleteTextView()
        })

        schoolViewModel.openNoticeFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.action_choseSchoolFragment_to_noticeFragment)
        })
    }

    private fun showRegionAutoCompleteTextView(regions: List<Region>) {
        val regionsAutoCompleteTV = binding.regionAutoCompleteTextView
        val regionsAdapter = ArrayAdapter(activity!!.applicationContext, android.R.layout.simple_dropdown_item_1line, regions)

        regionsAutoCompleteTV.setAdapter(regionsAdapter)

        regionsAutoCompleteTV.onItemClickListener = AdapterView.OnItemClickListener{
                parent, view, position, id ->
            val currentRegion = parent.getItemAtPosition(position) as Region
            schoolViewModel.updateUserRegion(currentRegion)
            regionsAutoCompleteTV.isEnabled = false
        }
    }

    private fun showCitiesAutoCompleteTextView(cities: List<City>) {
        val citiesAutoCompleteTV = binding.cityAutoCompleteTextView
        val citiesAdapter = ArrayAdapter(
            activity!!.applicationContext,
            android.R.layout.simple_dropdown_item_1line,
            cities
        )

        citiesAutoCompleteTV.visibility = View.VISIBLE
        binding.btnResetAdress.visibility = View.VISIBLE
        citiesAutoCompleteTV.setAdapter(citiesAdapter)

        citiesAutoCompleteTV.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, position, id ->
            val ida = id
            val currentCity = parent.getItemAtPosition(position) as City
            schoolViewModel.updateUserCity(currentCity)
            citiesAutoCompleteTV.isEnabled = false
        }
    }

    private fun showSchoolsAutoCompleteTextView(schools: List<School>) {
        val schoolsAutoCompleteTV = binding.schoolAutoCompleteTextView
        val schoolAdapter = ArrayAdapter(
            activity!!.applicationContext,
            android.R.layout.simple_dropdown_item_1line,
            schools
        )

        schoolsAutoCompleteTV.visibility = View.VISIBLE
        schoolsAutoCompleteTV.setAdapter(schoolAdapter)

        schoolsAutoCompleteTV.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, position, id ->
            schoolsAutoCompleteTV.isEnabled = false
            val currentSchool = parent.getItemAtPosition(position) as School
            schoolViewModel.updateUserSchool(currentSchool)
        }
    }

    private fun resetAutoCompleteTextView() {
        binding.schoolAutoCompleteTextView.setText("")
        binding.cityAutoCompleteTextView.setText("")
        binding.regionAutoCompleteTextView.setText("")

        binding.schoolAutoCompleteTextView.visibility = View.GONE
        binding.cityAutoCompleteTextView.visibility = View.GONE

        binding.regionAutoCompleteTextView.isEnabled = true
        binding.cityAutoCompleteTextView.isEnabled = true
        binding.schoolAutoCompleteTextView.isEnabled = true

        binding.btnNext.visibility = View.INVISIBLE
        binding.btnNext.visibility = View.INVISIBLE
    }

}
