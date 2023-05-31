package com.eagskunst.simplepostapp.presentation.viewholder

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.eagskunst.simplepostapp.R
import com.eagskunst.simplepostapp.databinding.ViewHolderSearchBarBinding

@EpoxyModelClass
abstract class SearchBarView : EpoxyModelWithHolder<SearchBarView.Holder>() {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onImeClick: (String) -> Unit

    @EpoxyAttribute
    lateinit var savedText: String

    override fun getDefaultLayout(): Int {
        return R.layout.view_holder_search_bar
    }

    override fun bind(holder: Holder) {
        super.bind(holder)
        with(holder.binding) {
            etSearch.setText(savedText, TextView.BufferType.EDITABLE)
            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onImeClick(etSearch.text.toString())
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
    }

    class Holder : EpoxyHolder() {
        lateinit var binding: ViewHolderSearchBarBinding

        override fun bindView(itemView: View) {
            binding = ViewHolderSearchBarBinding.bind(itemView)
        }
    }
}
