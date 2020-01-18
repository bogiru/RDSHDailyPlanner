package com.bogiruapps.rdshapp.notice


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.EventObserver
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentNoticeBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_notice.*
import org.koin.android.viewmodel.ext.android.viewModel

class NoticeFragment : Fragment() {

    private val noticeViewModel: NoticeViewModel by viewModel()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoticeAdapter
    private lateinit var binding: FragmentNoticeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        setupObserverViewModel()
        configureFirebase()
        setupListenerOnFub()

        return binding.root
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notice, container, false)
        binding.viewModel = noticeViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner


    }

    private fun setupObserverViewModel() {
        /*showProgress()*/
        noticeViewModel.openChooseSchoolFragmentEvent.observe(this, EventObserver {
            openChooseSchoolFragment()
        })
        noticeViewModel.openNoticeFragmentEvent.observe(this, EventObserver {
            hideProgress()
            configureRecyclerView()
        })

        noticeViewModel.openNoticeEditFragmentEvent.observe(this, EventObserver {
            showANoticeDetail()
            hideProgress()

        })

        noticeViewModel.openAlertDialogDeleteEvent.observe(this, EventObserver {
            showAllertDialogDelete()
        })
    }

    @SuppressLint("ResourceType")
    private fun showAllertDialogDelete(){
        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setTitle("Удалить объявление")
        alertBuilder.setMessage("Вы уверены, что хотите удалить объявление?")
        alertBuilder.setIconAttribute(R.drawable.rdsh_image)
        alertBuilder.setCancelable(true)
        alertBuilder.setPositiveButton(
            "Да"
        ) { _: DialogInterface, _: Int ->
            noticeViewModel.deleteNotice()
        }
        alertBuilder.setNegativeButton(
            "Нет"
        ) { _: DialogInterface, _: Int ->
        }
        alertBuilder.show()
    }




    private fun configureFirebase() {
        val auth = FirebaseAuth.getInstance()
        noticeViewModel.checkUserSchool()
    }

    private fun openChooseSchoolFragment() {
        hideProgress()
        findNavController().navigate(R.id.choseSchoolFragment)
    }

    private fun configureRecyclerView() {
        adapter = NoticeAdapter(noticeViewModel.fetchFirestoreRecyclerOptions(), noticeViewModel)
        binding.recyclerViewNotice.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewNotice.adapter = adapter
        adapter.startListening()

    }

    private fun setupListenerOnFub() {
        binding.fubNotice.setOnClickListener {
            showANoticeDetail()
        }
    }

    private fun showANoticeDetail() {
        findNavController().navigate(R.id.action_noticeFragment_to_noticeDetailFragment)
        adapter.stopListening()
    }

    private fun hideProgress() {
        pb_notice.visibility = View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

}
