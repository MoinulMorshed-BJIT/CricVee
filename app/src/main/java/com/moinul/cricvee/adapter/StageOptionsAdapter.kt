package com.moinul.cricvee.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.moinul.cricvee.R
import com.moinul.cricvee.databinding.StatOptionsItemBinding
import com.moinul.cricvee.utils.UtilTools
import kotlinx.android.synthetic.main.stat_options_item.view.*

class StageOptionsAdapter (private val context: Context,
                           private val values: List<UtilTools.Companion.Option>
) : RecyclerView.Adapter<StageOptionsAdapter.ViewHolder>() {

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
        Glide.with(context).load(item.optionImagePath).fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .priority(Priority.HIGH)
            .error(R.drawable.ic_connection_error)
            .into(holder.optionImgView)
        holder.optionTextView.setText(item.optionName)

        holder.itemView.setOnClickListener{
            val bundle = Bundle()
            bundle.putInt("stageId", item.optionStageId)
            bundle.putInt("seasonId", item.optionSeasonId)
            bundle.putString("leagueImagePath", item.optionImagePath)
            bundle.putString("leagueName", item.optionLeagueName)
            holder.itemView.findNavController().navigate(R.id.tournamentFixtureFragment, bundle)
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