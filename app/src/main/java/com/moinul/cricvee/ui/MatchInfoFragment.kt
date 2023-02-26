package com.moinul.cricvee.ui

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.moinul.cricvee.R
import com.moinul.cricvee.databinding.FragmentMatchInfoBinding
import com.moinul.cricvee.utils.Constants
import com.moinul.cricvee.utils.UtilTools
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.fragment_match_info.*
import kotlinx.android.synthetic.main.fragment_match_info.view.*
import java.util.*

private const val TAG = "MatchInfoFragment"

class MatchInfoFragment : Fragment() {
    private val viewModel: SportsViewModel by viewModels()
    private lateinit var binding: FragmentMatchInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMatchInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar()

        viewModel.readFixtureById(UtilTools.CLICKED_FIXTURE_ID)
        viewModel.currentFixture.observe(viewLifecycleOwner) {
            if (it != null) {
                val matchValue = it.round
                setTextValue(binding.matchRoundValue, matchValue)
                it.stage_id?.let { it1 -> viewModel.readStageByIdLive(it1) }
                it.venue_id?.let { it1 -> viewModel.readVenueById(it1) }
                it.first_umpire_id?.let { it1 -> viewModel.read1stUmpireById(it1) }
                it.second_umpire_id?.let { it1 -> viewModel.read2ndUmpireById(it1) }
                it.tv_umpire_id?.let { it1 -> viewModel.readTVUmpireById(it1) }
                it.referee_id?.let { it1 -> viewModel.readRefereeById(it1) }
                it.toss_won_team_id?.let { it1 -> viewModel.readTeamByIdLive(it1) }
                it.man_of_match_id?.let { it1 -> viewModel.getCareerByPlayerId(it1) }

                val formatter = SimpleDateFormat(Constants.DATE_FORMAT_STRING, Locale.getDefault())
                formatter.timeZone = TimeZone.getTimeZone(Constants.UTC_CODE)
                var timeValue = Constants.BLANK_STRING
                try {
                    val date = formatter.parse(it.starting_at)
                    var dateString = date.toString()
                    var timeZoneSliceIndex = dateString.indexOf(Constants.GMT_CODE)
                    var yearString =
                        dateString.substring(timeZoneSliceIndex + 10, dateString.length)
                    dateString = dateString.substring(0, timeZoneSliceIndex)
                    timeValue = dateString + yearString

                } catch (e: Exception) {
                    setTextValue(binding.timeValue, timeValue)
                    Log.d(TAG, "onViewCreated: $e")
                }
                setTextValue(binding.timeValue, timeValue)
            }
            hideProgressBar()
        }
        viewModel.currentStage.observe(viewLifecycleOwner) {
            if (it != null) {
                val seriesValue = it.name
                setTextValue(binding.stageValue, seriesValue)
            } else {
                setTextValue(binding.stageValue, Constants.BLANK_STRING)
            }
        }
        viewModel.currentVenue.observe(viewLifecycleOwner) {
            if (it != null) {
                val venueValue = "${it.name}, ${it.city}"
                setTextValue(binding.venueValue, venueValue)
            } else {
                setTextValue(binding.venueValue, Constants.BLANK_STRING)
            }
        }
        viewModel.currentTeam.observe(viewLifecycleOwner) {
            if (it != null) {
                val tossValue = it.name
                setTossValue(binding.tossWonValue, tossValue)
            } else {
                setTossValue(binding.tossWonValue, Constants.BLANK_STRING)
            }

        }

        viewModel.playerCareer.observe(viewLifecycleOwner) {
            if (it != null) {
                val manOfTheMatchValue = it.data?.fullname
                setTextValue(binding.manOfTheMatchValue, manOfTheMatchValue)
            } else {
                setTextValue(binding.manOfTheMatchValue, Constants.BLANK_STRING)
            }
        }
        viewModel.current1stUmpire.observe(viewLifecycleOwner) {
            if (it != null) {
                val firstUmpireValue = it.fullname
                setTextValue(binding.firstUmpireValue, firstUmpireValue)
            } else {
                setTextValue(binding.firstUmpireValue, Constants.BLANK_STRING)
            }
        }
        viewModel.current2ndUmpire.observe(viewLifecycleOwner) {
            if (it != null) {
                val secondUmpireValue = it.fullname
                setTextValue(binding.secondUmpireValue, secondUmpireValue)
            } else {
                setTextValue(binding.secondUmpireValue, Constants.BLANK_STRING)
            }
        }
        viewModel.currentTVUmpire.observe(viewLifecycleOwner) {
            if (it != null) {
                val tvUmpireValue = it.fullname
                setTextValue(binding.tvUmpireValue, tvUmpireValue)
            }
        }
        viewModel.currentReferee.observe(viewLifecycleOwner) {
            if (it != null) {
                val refereeValue = it.fullname
                setTextValue(binding.refereeValue, refereeValue)
            }
        }
    }

    fun setTextValue(textView: TextView, value: String?) {
        if (value == null || value == Constants.NULL_STRING || value == Constants.BLANK_STRING) {
            textView.text = requireContext().getString(R.string.missing)

        } else {
            textView.text = value
        }
    }

    fun setTossValue(textView: TextView, value: String?) {
        if (value == null || value == Constants.NULL_STRING || value == Constants.BLANK_STRING) {
            textView.text = requireContext().getString(R.string.missing)
        } else {
            textView.text = "$value ${Constants.WON_THE_TOSS}"
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.MATCH_ROUND_VALUE_KEY, binding.matchRoundValue.text.toString())
        outState.putString(Constants.STAGE_VALUE_KEY, binding.stageValue.toString())
        outState.putString(Constants.TIME_VALUE_KEY, binding.timeValue.toString())
        outState.putString(Constants.VENUE_VALUE_KEY, binding.venueValue.toString())
        outState.putString(Constants.FIRST_UMPIRE_VALUE_KEY, binding.firstUmpireValue.toString())
        outState.putString(Constants.SECOND_UMPIRE_VALUE_KEY, binding.secondUmpireValue.toString())
        outState.putString(Constants.TV_UMPIRE_VALUE_KEY, binding.tvUmpireValue.toString())
        outState.putString(Constants.REFEREE_VALUE_KEY, binding.refereeValue.toString())
        outState.putString(Constants.TOSS_WON_VALUE_KEY, binding.tossWonValue.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            binding.matchRoundValue.text =
                savedInstanceState.getString(Constants.MATCH_ROUND_VALUE_KEY)
            binding.stageValue.text = savedInstanceState.getString(Constants.STAGE_VALUE_KEY)
            binding.timeValue.text = savedInstanceState.getString(Constants.TIME_VALUE_KEY)
            binding.venueValue.text = savedInstanceState.getString(Constants.VENUE_VALUE_KEY)
            binding.firstUmpireValue.text =
                savedInstanceState.getString(Constants.FIRST_UMPIRE_VALUE_KEY)
            binding.secondUmpireValue.text =
                savedInstanceState.getString(Constants.SECOND_UMPIRE_VALUE_KEY)
            binding.tvUmpireValue.text = savedInstanceState.getString(Constants.TV_UMPIRE_VALUE_KEY)
            binding.refereeValue.text = savedInstanceState.getString(Constants.REFEREE_VALUE_KEY)
            binding.tossWonValue.text = savedInstanceState.getString(Constants.TOSS_WON_VALUE_KEY)
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

}