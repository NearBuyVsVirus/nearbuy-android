package app.nexd.android.ui.auth.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import app.nexd.android.R
import app.nexd.android.databinding.FragmentRegisterDetailedBinding
import app.nexd.android.ui.MainViewModel
import app.nexd.android.ui.auth.register.RegisterDetailedViewModel.Progress.*
import app.nexd.android.ui.common.Constants
import app.nexd.android.ui.common.DefaultSnackBar
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterDetailedFragment : Fragment() {

    private val vm: RegisterDetailedViewModel by viewModel()
    private val activityVm by sharedViewModel<MainViewModel>()

    private lateinit var binding: FragmentRegisterDetailedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterDetailedBinding.inflate(inflater, container, false)
        binding.viewModel = vm
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(findNavController()) {
            val appBarConfiguration = AppBarConfiguration(setOf(R.id.registerDetailedFragment))
            binding.registerDetailedToolbar.setupWithNavController(this, appBarConfiguration)
        }

        binding.editTextCity.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && binding.buttonRegister.isEnabled) {
                binding.buttonRegister.performClick()
            }
            false
        }

        binding.buttonRegister.setOnClickListener {
            vm.setUserDetails()
        }

        vm.progress.observe(viewLifecycleOwner, Observer { progress ->
            when (progress) {
                is Idle -> { /* do nothing in idle */ }
                is Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Error -> {
                    progress.message?.let {
                        DefaultSnackBar(view, it, Snackbar.LENGTH_SHORT)
                    }
                    binding.progressBar.visibility = View.GONE

                }
                is Finished -> {
                    activityVm.setUserAsComplete()
                    findNavController().navigateUp()
                    binding.progressBar.visibility = View.GONE
                }
            }
        })

        binding.buttonDataProtectionDetailRegistration.setOnClickListener {
            showPrivacyPolicy()
        }
    }

    private fun showPrivacyPolicy() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(Constants.PRIVACY_POLICY_URL)
            )
        )
    }

}