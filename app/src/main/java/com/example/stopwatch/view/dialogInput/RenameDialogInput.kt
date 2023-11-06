package com.example.stopwatch.view.dialogInput

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stopwatch.databinding.RenameDialogInputBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RenameDialogInput : BottomSheetDialogFragment(){
    private var _binding: RenameDialogInputBinding? = null
    private val binding get() = _binding!!
    private var onRenameClickListener: OnRenameClickListener? = null

    private val textWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (binding.renameEditText.text != null &&
                !binding.renameEditText.text.toString().isEmpty())
            {
                binding.renameButtonTextview.isEnabled = true
                binding.clearTextImageview.visibility = View.VISIBLE
            } else {
                binding.renameButtonTextview.isEnabled = false
                binding.clearTextImageview.visibility = View.GONE
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) { }
    }

    private val onRenameButtonClickListener =
        View.OnClickListener {
            onRenameClickListener?.onClick(binding.renameEditText.text.toString())
            dismiss()
        }

    internal fun setOnSearchClickListener(listener: OnRenameClickListener) {
        onRenameClickListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RenameDialogInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.renameButtonTextview.setOnClickListener(onRenameButtonClickListener)
        binding.renameEditText.addTextChangedListener(textWatcher)
        addOnClearClickListener()
    }

    override fun onDestroyView() {
        onRenameClickListener = null
        super.onDestroyView()
    }

    private fun addOnClearClickListener() {
        binding.clearTextImageview.setOnClickListener {
            binding.renameEditText.setText("")
            binding.renameButtonTextview.isEnabled = false
        }
    }

    interface OnRenameClickListener {
        fun onClick(newTimerName: String)
    }

    companion object {
        fun newInstance(): RenameDialogInput {
            return RenameDialogInput()
        }
    }
}