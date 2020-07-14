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
import com.bogiruapps.rdshapp.utils.showSnackbar
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class ChooseSchoolFragment : Fragment() {

    private lateinit var binding: FragmentChooseSchoolBinding
    private val schoolViewModel: SchoolViewModel by viewModel()

    private lateinit var progressBar: ProgressBar

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
        progressBar = binding.chooseProgressBar
    }

    private fun setupObserverViewModel() {

        schoolViewModel.regions.observe(viewLifecycleOwner, Observer { regions ->
            showRegionAutoCompleteTextView(regions)
        })

        schoolViewModel.cities.observe(viewLifecycleOwner, Observer { cities ->
            showCitiesAutoCompleteTextView(cities)
        })

        schoolViewModel.schools.observe(viewLifecycleOwner, Observer { schools ->
            showSchoolsAutoCompleteTextView(schools)
        })

        schoolViewModel.showSubmitButton.observe(viewLifecycleOwner, EventObserver {
            binding.btnSubmit.visibility = View.VISIBLE
        })

        schoolViewModel.resetAddress.observe(viewLifecycleOwner, EventObserver {
            resetAutoCompleteTextView()
        })

        schoolViewModel.openNoticeFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.action_choseSchoolFragment_to_noticeFragment)
        })

        schoolViewModel.showSnackbar.observe(viewLifecycleOwner, Observer { message ->
            showSnackbar(view!!, message)
        })
    }

    private fun showRegionAutoCompleteTextView(regions: List<Region>) {
        val tvRegionsAutoComplete = binding.regionAutoCompleteTextView

        val regionsAdapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_dropdown_item_1line,
            regions)
        tvRegionsAutoComplete.setAdapter(regionsAdapter)

        tvRegionsAutoComplete.onItemClickListener = AdapterView.OnItemClickListener{
                parent, _, position, _ ->
            val currentRegion = parent.getItemAtPosition(position) as Region
            schoolViewModel.updateUserRegion(currentRegion)
            tvRegionsAutoComplete.isEnabled = false
        }
    }

    private fun showCitiesAutoCompleteTextView(cities: List<City>) {
        val tvCitiesAutoComplete = binding.cityAutoCompleteTextView
        val citiesAdapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_dropdown_item_1line,
            cities
        )

        tvCitiesAutoComplete.visibility = View.VISIBLE
        binding.btnResetAdress.visibility = View.VISIBLE
        tvCitiesAutoComplete.setAdapter(citiesAdapter)

        tvCitiesAutoComplete.onItemClickListener = AdapterView.OnItemClickListener {
                parent, _, position, _ ->
            val currentCity = parent.getItemAtPosition(position) as City
            schoolViewModel.updateUserCity(currentCity)
            tvCitiesAutoComplete.isEnabled = false
        }
    }

    private fun showSchoolsAutoCompleteTextView(schools: List<School>) {
        val tvSchoolsAutoComplete = binding.schoolAutoCompleteTextView
        val schoolAdapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_dropdown_item_1line,
            schools
        )

        tvSchoolsAutoComplete.visibility = View.VISIBLE
        tvSchoolsAutoComplete.setAdapter(schoolAdapter)

        tvSchoolsAutoComplete.onItemClickListener = AdapterView.OnItemClickListener {
                parent, _, position, _ ->
            tvSchoolsAutoComplete.isEnabled = false
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

        binding.btnSubmit.visibility = View.INVISIBLE
        binding.btnSubmit.visibility = View.INVISIBLE
    }

}
