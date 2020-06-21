package app.nexd.android.ui.helper.detail

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import app.nexd.android.R
import app.nexd.android.api.model.HelpRequestArticle
import app.nexd.android.databinding.FragmentHelperRequestDetailBinding
import app.nexd.android.ui.common.DefaultSnackBar
import app.nexd.android.ui.common.HelpRequestArticleBinder
import com.google.android.material.snackbar.Snackbar
import mva2.adapter.ListSection
import mva2.adapter.MultiViewAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class HelperDetailFragment : Fragment() {

    private val args: HelperDetailFragmentArgs by navArgs()

    private val viewModel: HelperDetailViewModel by viewModel()
    private lateinit var adapter: MultiViewAdapter
    private lateinit var binding: FragmentHelperRequestDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHelperRequestDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.setInfo(args.requestId)
        initRecyclerView()
        setVmObserver()
    }

    private fun initRecyclerView() {
        adapter = MultiViewAdapter()
        binding.recyclerViewRequests.adapter = adapter
        binding.recyclerViewRequests.layoutManager = LinearLayoutManager(context)
        adapter.registerItemBinders(
            HelpRequestArticleBinder()
        )
    }

    private fun setVmObserver() {
        viewModel.requestArticles
            .observe(viewLifecycleOwner, Observer { articles ->
                adapter.removeAllSections()
                val list = ListSection<HelpRequestArticle>()
                articles?.let {
                    list.addAll(it)
                }
                adapter.addSection(list)
            })

        viewModel.idOfRequest.observe(viewLifecycleOwner, Observer { idOfRequest ->
            binding.buttonAccept.setOnClickListener {
                viewModel.acceptOrDeclineRequest(idOfRequest)
            }
        })

        viewModel.progress.observe(viewLifecycleOwner, Observer { progress ->
            when (progress) {
                is HelperDetailViewModel.Progress.Idle -> {
                    // do nothing
                }
                is HelperDetailViewModel.Progress.Loading -> {
                    // TODO: Show loading circle
                }
                is HelperDetailViewModel.Progress.Finished -> {
                    DefaultSnackBar(
                        requireParentFragment().requireView(),
                        progress.message,
                        Snackbar.LENGTH_SHORT
                    )
                    findNavController().popBackStack()
                }
                is HelperDetailViewModel.Progress.Error -> {
                    DefaultSnackBar(
                        requireParentFragment().requireView(),
                        progress.message,
                        Snackbar.LENGTH_SHORT
                    )
                    findNavController().popBackStack()
                }
            }

        })
        viewModel.requestIsAccepted.observe(viewLifecycleOwner, Observer { requestIsAccepted ->
            if (requestIsAccepted) {
                binding.buttonAccept.apply {
                    text = getString(R.string.helper_request_detail_button_cancel)
                    setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
                    setTypeface(null, Typeface.BOLD)
                }
            } else {
                binding.buttonAccept.apply {
                    text = getString(R.string.helper_request_detail_button_accept)
                    setTextColor(resources.getColor(android.R.color.black, null))
                    setTypeface(null, Typeface.NORMAL)
                }
            }
        })
    }


}
