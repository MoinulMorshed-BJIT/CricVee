package com.moinul.cricvee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.moinul.cricvee.R
import com.moinul.cricvee.databinding.StatOptionsItemBinding
import com.moinul.cricvee.utils.UtilTools


class StatOptionsAdapter(
    private val values: List<UtilTools.Companion.Option>
) : RecyclerView.Adapter<StatOptionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            StatOptionsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.optionImgView.setImageResource(item.optionImage)
        holder.optionTextView.text = item.optionName
        when (position) {
            0 -> {
                holder.itemView.setOnClickListener {
                    holder.itemView.findNavController().navigate(R.id.ICCTeamRankingsFragment)
                }
            }
            1 -> {
                holder.itemView.setOnClickListener {
                    holder.itemView.findNavController().navigate(R.id.ICCWomenTeamRankingsFragment)
                }
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: StatOptionsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val optionImgView: ImageView = binding.optionImg
        val optionTextView: TextView = binding.optionName

        override fun toString(): String {
            return super.toString() + " '" + optionTextView.text + "'"
        }
    }

}