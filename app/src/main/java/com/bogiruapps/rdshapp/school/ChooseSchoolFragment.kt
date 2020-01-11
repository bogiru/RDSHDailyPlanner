package com.bogiruapps.rdshapp.school


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.UserRemoteDataSource
import com.bogiruapps.rdshapp.UserRepositoryImpl
import com.bogiruapps.rdshapp.databinding.FragmentChooseSchoolBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_choose_school.view.*

/**
 * A simple [Fragment] subclass.
 */
class ChooseSchoolFragment : Fragment() {

    private lateinit var binding: FragmentChooseSchoolBinding
    private lateinit var schoolViewModel: SchoolViewModel


    private lateinit var db: FirebaseFirestore
    private lateinit var userRepository: UserRepositoryImpl
    private lateinit var userDataSource: UserRemoteDataSource

    private lateinit var spinner: Spinner
    private lateinit var btnChoose: Button
    private lateinit var progressBar: ProgressBar
    private var chosenSchool: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureViewModel()
        configureBinding(inflater, container)
        setupObserverViewModel()
        configureFirebase()
        setupChooseButton()
    return binding.root
}

    private fun configureViewModel() {
        db = FirebaseFirestore.getInstance()
        userDataSource = UserRemoteDataSource.getInstance(db)
        userRepository = UserRepositoryImpl.getInstance(userDataSource)
        val factory = SchoolViewModelFactory(userRepository)
        schoolViewModel = ViewModelProviders.of(this, factory).get(SchoolViewModel::class.java)
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
    }

    private fun configureFirebase() {}

    private fun setupChooseButton() {
        btnChoose.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            schoolViewModel.updateSchool(auth.currentUser!!, chosenSchool)

            findNavController().navigate(R.id.noticeFragment)
        }
    }

    private fun setupSpinner(items: List<String>) {
        val spinnerAdapter: ArrayAdapter<String> =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_item, items)
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

    private fun hideProgress(/*view : View*/) {
        spinner.visibility = View.VISIBLE
        btnChoose.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
    }

}
