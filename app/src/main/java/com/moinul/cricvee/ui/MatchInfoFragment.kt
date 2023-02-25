package com.moinul.cricvee.ui

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Build
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import com.moinul.cricvee.R
import com.moinul.cricvee.databinding.FragmentMatchInfoBinding
import com.moinul.cricvee.model.fixtures.FixtureData
import com.moinul.cricvee.utils.UtilTools
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.fragment_match_info.*
import kotlinx.android.synthetic.main.fragment_match_info.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "MatchInfoFragment"
class MatchInfoFragment : Fragment() {
    private val viewModel: SportsViewModel by viewModels()
    private lateinit var binding: FragmentMatchInfoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMatchInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var match: FixtureData? = null
        viewModel.readFixtureById(UtilTools.CLICKED_FIXTURE_ID).observe(viewLifecycleOwner){
            match = it
            GlobalScope.launch(Dispatchers.IO){

                val matchValue = match?.round
                val seriesValue = match?.stage_id?.let { viewModel.readStageById(it).name }
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                var timeValue = ""
                try {
                    val date = formatter.parse(match?.starting_at)
                    var dateString = date.toString()
                    //countdownTimer.text = date.toString()
                    var timeZoneSliceIndex = dateString.indexOf("GMT")
                    var yearString = dateString.substring(timeZoneSliceIndex+10, dateString.length)
                    dateString = dateString.substring(0, timeZoneSliceIndex)
                    timeValue = dateString+yearString
                }catch (e:Exception){
                    Log.d(TAG, "onViewCreated: $e")
                }
                val venueValue = match?.venue_id?.let { viewModel.readVenueById(it).name+", "+viewModel.readVenueById(it).city }
                val tossValue = match?.toss_won_team_id?.let { viewModel.readTeamById(it).name }
                match?.man_of_match_id?.let { viewModel.getCareerByPlayerId(it) }

                val firstUmpireValue = match?.first_umpire_id?.let { viewModel.readOfficialById(it).fullname }
                val secondUmpireValue = match?.second_umpire_id?.let { viewModel.readOfficialById(it).fullname }
                val tvUmpireValue = match?.tv_umpire_id?.let { viewModel.readOfficialById(it).fullname }
                val refereeValue = match?.referee_id?.let { viewModel.readOfficialById(it).fullname }
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.playerCareer.observe(viewLifecycleOwner){
                        val manOfTheMatchValue = it.data?.fullname
                        binding.manOfTheMatchValue.text = manOfTheMatchValue
                    }
                }

                GlobalScope.launch(Dispatchers.Main) {
                    setTextValue(binding.matchRoundValue, matchValue)
                    setTextValue(binding.stageValue, seriesValue)
                    setTextValue(binding.timeValue, timeValue)
                    setTextValue(binding.venueValue, venueValue)
                    setTextValue(binding.firstUmpireValue, firstUmpireValue)
                    setTextValue(binding.secondUmpireValue, secondUmpireValue)
                    setTextValue(binding.tvUmpireValue, tvUmpireValue)
                    setTextValue(binding.refereeValue, refereeValue)

                    setTossValue(binding.tossWonValue, tossValue)
                }
            }
        }

    }

    fun setTextValue(textView: TextView, value: String?){
        if(value==null || value == "null" || value == ""){
            textView.text = requireContext().getString(R.string.missing)
        }else{
            textView.text = value
        }
    }
    fun setTossValue(textView: TextView, value: String?){
        if(value==null || value == "null" || value == ""){
            textView.text = requireContext().getString(R.string.missing)
        }else{
            textView.text = value+" won the toss."
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("matchRoundValue", binding.matchRoundValue.text.toString())
        outState.putString("stageValue", binding.stageValue.toString())
        outState.putString("timeValue",binding.timeValue.toString())
        outState.putString("venueValue",binding.venueValue.toString())
        outState.putString("firstUmpireValue",binding.firstUmpireValue.toString())
        outState.putString("secondUmpireValue",binding.secondUmpireValue.toString())
        outState.putString("tvUmpireValue",binding.tvUmpireValue.toString())
        outState.putString("refereeValue",binding.refereeValue.toString())
        outState.putString("tossWonValue",binding.tossWonValue.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            binding.matchRoundValue.text = savedInstanceState.getString("matchRoundValue")
            binding.stageValue.text = savedInstanceState.getString("stageValue")
            binding.timeValue.text = savedInstanceState.getString("timeValue")
            binding.venueValue.text = savedInstanceState.getString("venueValue")
            binding.firstUmpireValue.text = savedInstanceState.getString("firstUmpireValue")
            binding.secondUmpireValue.text = savedInstanceState.getString("secondUmpireValue")
            binding.tvUmpireValue.text = savedInstanceState.getString("tvUmpireValue")
            binding.refereeValue.text = savedInstanceState.getString("refereeValue")
            binding.tossWonValue.text = savedInstanceState.getString("tossWonValue")
        }
    }

}