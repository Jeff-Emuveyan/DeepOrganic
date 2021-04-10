package com.bellogate.deeporganic.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bellogate.deeporganic.R
import com.bellogate.deeporganic.databinding.AccountFragmentBinding
import com.bellogate.deeporganic.ui.message.MessageBottomSheet
import com.bellogate.deeporganic.util.SIGN_UP_LOGIN_REQUEST_CODE
import com.firebase.ui.auth.IdpResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {

    companion object {
        private val name = AccountFragment::class.java.simpleName
        fun newInstance() = AccountFragment()
    }

    private val viewModel: AccountViewModel by viewModels()
    @Inject lateinit var messageBottomSheet: MessageBottomSheet

    private var _binding: AccountFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        _binding = AccountFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.signUpButton.setOnClickListener {
            signUpOrLogin()
        }
    }

    private fun signUpOrLogin() {
        startActivityForResult(viewModel.signUpOrLoginIntent(), SIGN_UP_LOGIN_REQUEST_CODE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        lifecycleScope.launch{
            viewModel.signUpResponseHandler?.processResponse(requestCode, resultCode, IdpResponse.fromResultIntent(data),
                success = {
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
                }, networkError = {
                    messageBottomSheet.message = getString(R.string.network_error_message)
                    messageBottomSheet.show(childFragmentManager, AccountFragment::class.java.simpleName)
                }, unknownError = {
                    messageBottomSheet.message = getString(R.string.error_message)
                    messageBottomSheet.show(childFragmentManager, AccountFragment::class.java.simpleName)
                })
        }
    }

}