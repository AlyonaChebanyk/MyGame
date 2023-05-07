package com.example.mygame.adapters

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mygame.databinding.SlotItemBinding
import com.example.mygame.databinding.SlotItemLandBinding

class SlotAdapter(private val orientationListener: OrientationListener, private val slotItems: List<SlotItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_PORTRAIT = 1
        private const val VIEW_TYPE_LANDSCAPE = 2
    }

    override fun getItemViewType(position: Int): Int {
//        val resources: Resources = context.resources
//        val configuration: Configuration = resources.configuration
//        return if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            VIEW_TYPE_PORTRAIT
//        } else {
//            VIEW_TYPE_LANDSCAPE
//        }
        return when(orientationListener.getOrientation()) {
            Orientation.PORTRAIT -> VIEW_TYPE_PORTRAIT
            Orientation.LANDSCAPE -> VIEW_TYPE_LANDSCAPE
        }
    }

    fun getItemByPosition(position: Int) = slotItems[position % slotItems.size]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_PORTRAIT -> {
                SlotPortraitViewHolder(
                    SlotItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false))
            }
            VIEW_TYPE_LANDSCAPE -> {
                SlotLandscapeViewHolder(
                    SlotItemLandBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false))
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SlotPortraitViewHolder -> {
                holder.bind(slotItems[position % slotItems.size].image)
            }
            is SlotLandscapeViewHolder -> {
                holder.bind(slotItems[position % slotItems.size].image)
            }
        }
    }

    class SlotPortraitViewHolder(private val binding: SlotItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Drawable?) {

            with(binding) {
                itemImageView.setImageDrawable(image)
            }
        }
    }

    class SlotLandscapeViewHolder(private val binding: SlotItemLandBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Drawable?) {

            with(binding) {
                itemImageView.setImageDrawable(image)
            }
        }
    }

}

