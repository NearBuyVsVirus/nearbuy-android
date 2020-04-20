package app.nexd.android.ui.role

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import app.nexd.android.R
import kotlinx.android.synthetic.main.fragment_role.*

class RoleFragment : Fragment() {

    private val viewModel: RoleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_role, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMe().observe(viewLifecycleOwner, Observer { currentUser ->
            textView_title.text = getString(
                R.string.role_screen_title,
                currentUser.firstName
            )
            button_profile.text = getString(
                R.string.user_name_abbreviation,
                currentUser.firstName?.first() ?: "",
                currentUser.lastName?.first() ?: ""
            )
        })

        role_button_helper.setOnClickListener {
            findNavController().navigate(RoleFragmentDirections.toHelperTypeFragment())
        }

        role_button_seeker.setOnClickListener {
            findNavController().navigate(RoleFragmentDirections.toSeekerTypeFragment())
        }

        button_logout.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.actionLogin)
        }
    }

}
