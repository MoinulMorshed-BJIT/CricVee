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


        viewModel.readFixtureById(UtilTools.CLICKED_FIXTURE_ID)
        viewModel.currentFixture.observe(viewLifecycleOwner) {
            if(it!=null){
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

                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                var timeValue = ""
                try {
                    val date = formatter.parse(it.starting_at)
                    var dateString = date.toString()
                    //countdownTimer.text = date.toString()
                    var timeZoneSliceIndex = dateString.indexOf("GMT")
                    var yearString = dateString.substring(timeZoneSliceIndex+10, dateString.length)
                    dateString = dateString.substring(0, timeZoneSliceIndex)
                    timeValue = dateString+yearString

                }catch (e:Exception){
                    setTextValue(binding.timeValue, timeValue)
                    Log.d(TAG, "onViewCreated: $e")
                }
                setTextValue(binding.timeValue, timeValue)
            }
        }
        viewModel.currentStage.observe(viewLifecycleOwner){
            if(it!=null){
                val seriesValue = it.name
                setTextValue(binding.stageValue, seriesValue)
            }else{
                setTextValue(binding.stageValue, "")
            }
        }
        viewModel.currentVenue.observe(viewLifecycleOwner){
            if(it!=null){
                val venueValue = "${it.name}, ${it.city}"
                setTextValue(binding.venueValue, venueValue)
            }else{
                setTextValue(binding.venueValue, "")
            }
        }
        viewModel.currentTeam.observe(viewLifecycleOwner){
            if(it!=null){
                val tossValue = it.name
                setTossValue(binding.tossWonValue, tossValue)
            }else{
                setTossValue(binding.tossWonValue, "")
            }

        }

        viewModel.playerCareer.observe(viewLifecycleOwner){
            if(it!=null){
                val manOfTheMatchValue = it.data?.fullname
                setTextValue(binding.manOfTheMatchValue, manOfTheMatchValue)
            }else{
                setTextValue(binding.manOfTheMatchValue, "")
            }
        }
        viewModel.current1stUmpire.observe(viewLifecycleOwner) {
            if(it!=null){
                val firstUmpireValue = it.fullname
                setTextValue(binding.firstUmpireValue, firstUmpireValue)
            }else{
                setTextValue(binding.firstUmpireValue, "")
            }
        }
        viewModel.current2ndUmpire.observe(viewLifecycleOwner) {
            if(it!=null){
                val secondUmpireValue = it.fullname
                setTextValue(binding.secondUmpireValue, secondUmpireValue)
            }else{
                setTextValue(binding.secondUmpireValue, "")
            }
        }
        viewModel.currentTVUmpire.observe(viewLifecycleOwner) {
            if(it!=null){
                val tvUmpireValue = it.fullname
                setTextValue(binding.tvUmpireValue, tvUmpireValue)
            }
        }
        viewModel.currentReferee.observe(viewLifecycleOwner) {
            if(it!=null){
                val refereeValue = it.fullname
                setTextValue(binding.refereeValue, refereeValue)
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