package com.bogiruapps.rdshapp.events


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.bogiruapps.rdshapp.R
import kotlinx.android.synthetic.main.fragment_events.*
import kotlinx.android.synthetic.main.fragment_events.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class EventsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_events, container, false)
        val recyclerView =   view.recycler_view_events.apply {
            layoutManager = LinearLayoutManager(activity?.applicationContext)
            adapter = EventsAdapter(listOf(Event(), Event(), Event()))
        }
        return view
    }


}
