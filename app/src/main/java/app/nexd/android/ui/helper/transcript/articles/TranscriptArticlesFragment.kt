package app.nexd.android.ui.helper.transcript.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.nexd.android.R
import app.nexd.android.databinding.FragmentTranscriptArticlesBinding
import app.nexd.android.di.sharedGraphViewModel
import app.nexd.android.ui.helper.transcript.TranscriptViewModel
import kotlinx.android.synthetic.main.fragment_transcript_articles.*
import mva2.adapter.ListSection
import mva2.adapter.MultiViewAdapter

class TranscriptArticlesFragment : Fragment() {

    private val transcriptViewModel: TranscriptViewModel by sharedGraphViewModel(R.id.nav_transcript)

    private lateinit var binding: FragmentTranscriptArticlesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTranscriptArticlesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = transcriptViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MultiViewAdapter()
        adapter.registerItemBinders(TranscriptArticlesItemBinder())

        recyclerview_helpRequestArticles.adapter = adapter
        recyclerview_helpRequestArticles.layoutManager = LinearLayoutManager(context)

        transcriptViewModel.articles.observe(viewLifecycleOwner, Observer { articles ->
            adapter.removeAllSections()

            val section = ListSection<TranscriptArticlesItemViewModel>()
            section.addAll(articles)
            adapter.addSection(section)
        })

        button_transcript.setOnClickListener {
            transcriptViewModel.saveHelpRequest()
        }

        transcriptViewModel.call.observe(viewLifecycleOwner,  Observer {
            if (it == null) {
                findNavController().popBackStack(R.id.callOverviewFragment, false)
            }
        })
    }

}