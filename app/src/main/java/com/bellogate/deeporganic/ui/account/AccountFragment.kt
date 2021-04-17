package com.bellogate.deeporganic.ui.account

import android.content.Context
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
import com.bellogate.deeporganic.model.User
import com.bellogate.deeporganic.ui.message.MessageBottomSheet
import com.bellogate.deeporganic.util.SIGN_UP_LOGIN_REQUEST_CODE
import com.bellogate.deeporganic.util.common.SessionManager
import com.firebase.ui.auth.IdpResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
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

        val existingUser = SessionManager.currentUser
        if(existingUser == null){
            setUpUIState(UIState.NO_USER, existingUser)
        }else{
            setUpUIState(UIState.USER_LOGGED_IN, existingUser)
        }

        //we place a constant listener to know when the user has signed out,
        // So that we can know when to delete the user from db
        //This will trigger anytime the user sign out. Successfully or not.
        viewModel.listenForUserSignOut(SessionManager)

        //we now observe to know when the user has signed out so that we can update the UI
        viewModel.userSignedOut.observe(viewLifecycleOwner){
            setUpUIState(UIState.NO_USER, null)
        }

        binding.signUpButton.setOnClickListener {
            signUpOrLogin()
        }

        binding.tvSignOut.setOnClickListener {
           lifecycleScope.launch{
               signOut(requireContext())
           }
        }
    }

    private suspend fun signOut(context: Context) {
        viewModel.signOut(context)
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
                    setUpUIState(UIState.USER_LOGGED_IN, it)
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
                }, networkError = {
                    setUpUIState(UIState.NETWORK_ERROR, null)
                }, unknownError = {
                    setUpUIState(UIState.UNKNOWN_ERROR, null)
                })
        }
    }


    private fun setUpUIState(uiState: UIState, user: User?){
        when(uiState){
            UIState.NO_USER ->{
                binding.layoutNoUser.visibility = View.VISIBLE
            }
            UIState.USER_LOGGED_IN ->{
                binding.layoutNoUser.visibility = View.GONE
            }
            UIState.NETWORK_ERROR ->{
                messageBottomSheet.message = getString(R.string.network_error_message)
                messageBottomSheet.show(childFragmentManager, AccountFragment::class.java.simpleName)
            }
            UIState.UNKNOWN_ERROR ->{
                messageBottomSheet.message = getString(R.string.error_message)
                messageBottomSheet.show(childFragmentManager, AccountFragment::class.java.simpleName)
            }
        }
    }

    enum class UIState{
        NO_USER, USER_LOGGED_IN, NETWORK_ERROR, UNKNOWN_ERROR
    }

}