package com.eagskunst.simplepostapp.presentation.viewholder

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.eagskunst.simplepostapp.R
import com.eagskunst.simplepostapp.databinding.ViewHolderPostBinding
import com.eagskunst.simplepostapp.domain.entity.PostEntity

@EpoxyModelClass
abstract class PostView : EpoxyModelWithHolder<PostView.Holder>() {

    @EpoxyAttribute
    lateinit var post: PostEntity

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onDeleteClick: View.OnClickListener

    override fun getDefaultLayout(): Int {
        return R.layout.view_holder_post
    }

    override fun bind(holder: Holder) {
        super.bind(holder)
        with(holder.binding) {
            tvPostName.text = post.name
            tvPostDescription.text = post.description
            btPostDelete.setOnClickListener(onDeleteClick)
        }
    }

    class Holder : EpoxyHolder() {

        lateinit var binding: ViewHolderPostBinding

        override fun bindView(itemView: View) {
            binding = ViewHolderPostBinding.bind(itemView)
        }
    }
}
