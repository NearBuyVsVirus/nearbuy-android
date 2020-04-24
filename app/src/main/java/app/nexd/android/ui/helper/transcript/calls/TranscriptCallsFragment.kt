package app.nexd.android.ui.helper.transcript.calls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.nexd.android.R
import app.nexd.android.api.model.Call
import app.nexd.android.di.sharedGraphViewModel
import app.nexd.android.ui.common.CallBinder
import app.nexd.android.ui.helper.transcript.TranscriptViewModel
import app.nexd.android.ui.helper.transcript.calls.TranscriptCallsFragmentDirections.Companion.actionCallOverviewFragmentToTranscriptInfoFragment
import kotlinx.android.synthetic.main.fragment_transcript_calls.*
import mva2.adapter.ListSection
import mva2.adapter.MultiViewAdapter

class TranscriptCallsFragment: Fragment() {

    private val transcriptViewModel : TranscriptViewModel by sharedGraphViewModel(R.id.nav_transcript)

    private val callsAdapter = MultiViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transcript_calls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView_calls.adapter = callsAdapter
        recyclerView_calls.layoutManager = LinearLayoutManager(context)

        callsAdapter.registerItemBinders(
            CallBinder()
        )

        observeViewModels()
    }

    override fun onResume() {
        super.onResume()

        transcriptViewModel.reloadCalls()
    }

    private fun observeViewModels() {
        transcriptViewModel.calls.observe(viewLifecycleOwner, Observer { calls: List<Call> ->
            callsAdapter.removeAllSections()

            val callsList = ListSection<Call>()
            callsList.addAll(calls)
            callsAdapter.addSection(callsList)

            callsList.setOnSelectionChangedListener { call, _, _ ->
                transcriptViewModel.transcriptCall(call)
            }
        })

        transcriptViewModel.call.observe(viewLifecycleOwner, Observer {
            it?.run {
                findNavController().navigate(actionCallOverviewFragmentToTranscriptInfoFragment())
            }
        })
    }

}