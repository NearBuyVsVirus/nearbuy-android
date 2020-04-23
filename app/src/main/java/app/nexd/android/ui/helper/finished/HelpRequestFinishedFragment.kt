package app.nexd.android.ui.helper.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.nexd.android.R
import app.nexd.android.api.model.HelpRequest
import app.nexd.android.ui.common.HelpRequestBinder
import kotlinx.android.synthetic.main.fragment_helper_request_finished.*
import mva2.adapter.ListSection
import mva2.adapter.MultiViewAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HelpRequestFinishedFragment: Fragment() {

    private val vm: HelpRequestFinishedViewModel by viewModel()

    private val adapter = MultiViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_helper_request_finished, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView_requests.layoutManager = LinearLayoutManager(context)
        recyclerView_requests.adapter = adapter
        adapter.registerItemBinders(
            HelpRequestBinder()
        )

        vm.getFinishedRequests().observe(viewLifecycleOwner, Observer { requests ->
            adapter.removeAllSections()

            val requestList = ListSection<HelpRequest>()
            requestList.addAll(requests)
            adapter.addSection(requestList)

            requestList.setOnSelectionChangedListener { request: HelpRequest,
                                                                 b: Boolean, _: MutableList<HelpRequest> ->
                if (b) {
                    request.id?.let { id ->
                        val action =
                            HelpRequestFinishedFragmentDirections.toDetailFragment(
                                id
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        })
    }

}