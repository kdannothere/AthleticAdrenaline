package com.adrenaline.ofathlet.slot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adrenaline.ofathlet.databinding.SlotBinding
import com.adrenaline.ofathlet.utilities.ImageUtility
import com.adrenaline.ofathlet.utilities.ViewUtility

class SlotAdapter(private val slotList: List<Slot>) : RecyclerView.Adapter<SlotAdapter.SlotViewHolder>() {

    class SlotViewHolder(val binding: SlotBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val slotBinding = SlotBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return SlotViewHolder(slotBinding)
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        val imageId = slotList[position].imageId
        holder.binding.image.apply {
            setImageResource(ImageUtility.getDrawableId(imageId))
        }
        holder.binding.root.apply {
            minHeight = (ViewUtility.getSlotHeight() * 0.8).toInt()
            maxHeight = (ViewUtility.getSlotHeight() * 0.8).toInt()
        }
    }

    override fun getItemCount(): Int {
        return slotList.size
    }
}