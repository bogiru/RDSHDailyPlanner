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

        schoolViewModel.openNoticeFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.action_choseSchoolFragment_to_noticeFragment)
        })
    }

    private fun showRegionAutoCompleteTextView(regions: List<Region>) {
        val regionsAutoCompleteTV = binding.regionAutoCompleteTextView
        val regionNames = mutableListOf<String>()
        for (region in regions) regionNames.add(region.name)
        val regionsAdapter = ArrayAdapter(activity!!.applicationContext, android.R.layout.simple_dropdown_item_1line, regionNames)

        regionsAutoCompleteTV.setAdapter(regionsAdapter)

        regionsAutoCompleteTV.onItemClickListener = AdapterView.OnItemClickListener{
                parent, view, position, id ->
            schoolViewModel.updateUserRegion(regions[position])
            regionsAutoCompleteTV.isEnabled = false
        }
    }

    private fun showCitiesAutoCompleteTextView(cities: List<City>) {
        val citiesAutoCompleteTV = binding.cityAutoCompleteTextView
        val cityNames = mutableListOf<String>()
        for (city in cities) cityNames.add(city.name)
        val citiesAdapter = ArrayAdapter(activity!!.applicationContext, android.R.layout.simple_dropdown_item_1line, cityNames)

        citiesAutoCompleteTV.isFocusable = true
        citiesAutoCompleteTV.isEnabled = true
        citiesAutoCompleteTV.setAdapter(citiesAdapter)

        citiesAutoCompleteTV.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, position, id ->
            schoolViewModel.updateUserCity(cities[position])
            citiesAutoCompleteTV.isEnabled = false
        }
    }

    private fun showSchoolsAutoCompleteTextView(schools: List<School>) {
        val schoolsAutoCompleteTV = binding.schoolAutoCompleteTextView
        val schoolNames = mutableListOf<String>()
        for (school in schools) schoolNames.add(school.name)
        val schoolAdapter = ArrayAdapter(activity!!.applicationContext, android.R.layout.simple_dropdown_item_1line, schoolNames)

        schoolsAutoCompleteTV.isFocusable = true
        schoolsAutoCompleteTV.isEnabled = true
        schoolsAutoCompleteTV.setAdapter(schoolAdapter)

        schoolsAutoCompleteTV.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, position, id ->
            schoolsAutoCompleteTV.isEnabled = false
            schoolViewModel.updateUserSchool(schools[position])
        }
    }
}
