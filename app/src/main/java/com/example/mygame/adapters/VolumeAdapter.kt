package com.example.mygame.adapters

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mygame.R
import com.example.mygame.databinding.VolumeItemBinding
import com.example.mygame.ui.settings.VolumeListener
import timber.log.Timber

class VolumeAdapter(
    var volume: Int,
    private val listener: VolumeListener,
    private val volumeType: Volume
) :
    RecyclerView.Adapter<VolumeAdapter.VolumeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolumeViewHolder {
        return VolumeViewHolder(
            VolumeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = 8

    override fun onBindViewHolder(holder: VolumeViewHolder, position: Int) {
        holder.bind(position, volume, volumeType, listener)
    }

    class VolumeViewHolder(private val binding: VolumeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, volume: Int, volumeType: Volume, listener: VolumeListener) {

            with(binding) {
                if (position == 0 && volume == 0) {
                    binding.volumeImageView.setImageResource(R.drawable.volume_item_transparent)
                } else if (position <= volume) {
                    binding.volumeImageView.setImageResource(R.drawable.volume_item_full)
                } else {
                    binding.volumeImageView.setImageResource(R.drawable.volume_item_transparent)
                }
                rootLayout.setOnClickListener {
                    Timber.d("volume: $volume")
                    Timber.d("position: $position")
                    when (volumeType) {
                        Volume.Music -> listener.onSetMusic(position)
                        Volume.Vibration -> listener.onSetVibration(position)
                    }
                }
                rootLayout.setOnLongClickListener {
                    when (volumeType) {
                        Volume.Music -> {
                            listener.onSetMusic(position)
                            false
                        }
                        Volume.Vibration -> {
                            listener.onSetVibration(position)
                            false
                        }
                    }
                }
                rootLayout.setOnTouchListener { _, event ->
                    when (event.action) {
                        MotionEvent.ACTION_MOVE -> {
                            when (volumeType) {
                                Volume.Music -> listener.onSetMusic(position)
                                Volume.Vibration -> listener.onSetVibration(position)
                            }
                            true
                        }
//                        MotionEvent.ACTION_UP -> {
//                            when (volumeType) {
//                                Volume.Music -> listener.onSetMusic(position)
//                                Volume.Vibration -> listener.onSetVibration(position)
//                            }
//                            true
//                        }
                        else -> false
                    }
                }
            }
        }
    }
}

enum class Volume {
    Music, Vibration
}