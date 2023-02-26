package com.moinul.cricvee.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
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
import com.moinul.cricvee.utils.Constants
import com.moinul.cricvee.utils.UtilTools

class StageOptionsAdapter(
    private val context: Context,
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
        holder.optionTextView.text = item.optionName

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(Constants.STAGE_ID_KEY, item.optionStageId)
            bundle.putInt(Constants.SEASON_ID, item.optionSeasonId)
            bundle.putString(Constants.LEAGUE_IMG_PATH_KEY, item.optionImagePath)
            bundle.putString(Constants.LEAGUE_NAME_KEY, item.optionLeagueName)
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