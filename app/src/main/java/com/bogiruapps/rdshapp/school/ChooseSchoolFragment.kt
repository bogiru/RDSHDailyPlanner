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
        configureFirebase()
        setupChooseButton()
    return binding.root
}

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_school, container, false)
        binding.viewModel = schoolViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        btnChoose = binding.btnChoose
        spinner = binding.spinner
        progressBar = binding.chooseProgressBar
    }

    private fun setupObserverViewModel() {
        schoolViewModel.schools.observe(this, Observer {
            val schools = it
            setupSpinner(schools)
        })

        schoolViewModel.openNoticeFragmentEvent.observe(this, EventObserver {
            findNavController().navigate(R.id.action_choseSchoolFragment_to_noticeFragment)
        })
    }

    private fun configureFirebase() {}

    private fun setupChooseButton() {
        btnChoose.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            schoolViewModel.updateSchool(auth.currentUser!!, chosenSchool)
        }
    }

    private fun setupSpinner(items: List<School>) {
        val spinnerAdapter: ArrayAdapter<String> =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_item, getSchoolNames(items))
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        hideProgress()

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                chosenSchool = items[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun getSchoolNames(schools: List<School>): List<String> {
        val schoolName = mutableListOf<String>()
        for (school in schools) schoolName.add(school.name)
        return schoolName
    }

    private fun configureToolbar() {
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)

        activity?.window?.decorView?.systemUiVisibility = View.VISIBLE
        activity?.collapseToolbar?.title = ""
        activity?.appBar?.setExpanded(false)
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun hideProgress(/*view : View*/) {
        spinner.visibility = View.VISIBLE
        btnChoose.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
    }

}
