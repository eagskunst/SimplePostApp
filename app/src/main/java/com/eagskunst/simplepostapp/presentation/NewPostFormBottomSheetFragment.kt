package com.eagskunst.simplepostapp.presentation

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.eagskunst.simplepostapp.databinding.FragmentNewPostFormBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewPostFormBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        fun show(container: FragmentActivity) {
            NewPostFormBottomSheetFragment().show(container.supportFragmentManager, null)
        }
    }

    private var _binding: FragmentNewPostFormBottomSheetBinding? = null
    private val binding: FragmentNewPostFormBottomSheetBinding
        get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        (dialog as? BottomSheetDialog)?.setOnShowListener {
            Handler(Looper.getMainLooper()).post {
                val bottomSheet = dialog.findViewById<View>(
                    com.google.android.material.R.id.design_bottom_sheet,
                ) as? FrameLayout

                bottomSheet?.let {
                    BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNewPostFormBottomSheetBinding
            .inflate(inflater, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            etPostName.doAfterTextChanged {
                changeSubmitButtonState()
            }
            etPostDescription.doAfterTextChanged {
                changeSubmitButtonState()
            }
            btPostSubmit.setOnClickListener {
                dismiss()
                viewModel.addPost(etPostName.text.toString(), etPostDescription.text.toString())
            }
        }
    }

    private fun changeSubmitButtonState() {
        with(binding) {
            btPostSubmit.isEnabled = !etPostName.text.isNullOrEmpty() &&
                !etPostDescription.text.isNullOrEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
