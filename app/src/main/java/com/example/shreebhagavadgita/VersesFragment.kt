package com.example.shreebhagavadgita

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shreebhagavadgita.databinding.FragmentVersesBinding
import com.example.shreebhagavadgita.view.adapter.AdapterVerses
import com.example.shreebhagavadgita.viewmodel.MainViewModel
import kotlinx.coroutines.launch


class VersesFragment : Fragment() {

    private lateinit var binding:FragmentVersesBinding
    private val viewModel : MainViewModel by viewModels()
    private lateinit var adapterVerses: AdapterVerses
    private  var  chapterNumber=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVersesBinding.inflate(inflater, container, false)

        getAndSetChapterDetail()
//        getAllVerses()
        changeStatusBarColor()
        onReadMoreClick()
        checkInternet()

        return binding.root
    }


//    _________________________________________________________________________________________________
    private fun checkInternet() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner) {
            if (it) {
                getAllVerses()
                binding.verseShimmer.visibility = View.VISIBLE
                binding.verseRecyclerView.visibility = View.VISIBLE
                binding.noInternetMessage.visibility = View.GONE
            } else {
                binding.verseShimmer.visibility = View.GONE
                binding.verseRecyclerView.visibility = View.GONE
                binding.noInternetMessage.visibility = View.VISIBLE

            }
        }
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    private fun onReadMoreClick() {
        var isExpanded = false
            binding.verseReadMore.setOnClickListener {

                if(!isExpanded) {
                    binding.verseDescription.maxLines = 100
                    binding.verseReadMore.text = "Read Less"
                    isExpanded = true
                }else{
                    binding.verseDescription.maxLines = 4
                    binding.verseReadMore.text = "Read More..."
                    isExpanded = false
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun getAndSetChapterDetail() {
        val bundle = arguments
        chapterNumber = bundle?.getInt("chapterNumber")!!
        binding.versechapterNumber.text = "Chapter ${bundle?.getInt("chapterNumber")}"
        binding.verseChapterTitle.text = bundle?.getString("chapterTitle")
        binding.verseDescription.text = bundle?.getString("chapterDesc")
        binding.noOfVerses.text = bundle?.getInt("verseCount").toString()
    }

    private fun getAllVerses() {
        lifecycleScope.launch {
            viewModel.getVerse(chapterNumber).collect {
                adapterVerses = AdapterVerses(::onVersesItemClicked)
                binding.verseRecyclerView.adapter = adapterVerses

                var verseList = arrayListOf<String>()

                for(currentVerse in it){
                    for(verse in currentVerse.translations){
                        if(verse.language=="english"){
                            verseList.add(verse.description)
                            break
                        }
                    }
                }

                adapterVerses.differ.submitList(verseList)

                binding.verseShimmer.visibility = View.GONE
//                binding.verseRecyclerView.visibility = View.VISIBLE

            }
        }
    }


    private fun onVersesItemClicked(verse: String,verseNumber:Int){
        val bundle = Bundle()
        bundle.putInt("chapterNumber",chapterNumber)
        bundle.putInt("verseNumber",verseNumber)
        findNavController().navigate(R.id.action_versesFragment_to_verseDetailFragment,bundle)
    }



    fun changeStatusBarColor(){
        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.orange)
        if (window != null) {
            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = true
            }
        }
    }


}