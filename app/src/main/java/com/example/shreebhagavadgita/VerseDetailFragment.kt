package com.example.shreebhagavadgita

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
import com.example.shreebhagavadgita.databinding.FragmentVerseDetailBinding
import com.example.shreebhagavadgita.models.Commentary
import com.example.shreebhagavadgita.models.Translation
import com.example.shreebhagavadgita.viewmodel.MainViewModel
import kotlinx.coroutines.launch


class VerseDetailFragment : Fragment() {

    private  lateinit var binding: FragmentVerseDetailBinding
    private val viewModel: MainViewModel by viewModels()
    private var chapterNumber=0
    private var verseNumber=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVerseDetailBinding.inflate(inflater, container, false)
        changeStatusBarColor()
        onReadMoreClick()

        getAndSetChapAndVerseNum()
        getVerseDetail()
        checkInternetConnectivity()
        return binding.root
    }

//    NetworkManager__________________________________________________________________________________
        private fun checkInternetConnectivity() {

            val networkManager = NetworkManager(requireContext())
            networkManager.observe(viewLifecycleOwner) {
                if (it) {

                    binding.noInternetMessage.visibility = View.GONE
                    binding.noInternetImage.visibility = View.GONE
                } else {
                    binding.noInternetMessage.visibility = View.VISIBLE
                    binding.noInternetImage.visibility = View.VISIBLE

                }
            }
        }
//    _________________________________________________________________________________________________
        private fun onReadMoreClick() {
            var isExpanded = false
            binding.tvReadMore.setOnClickListener {

                if(!isExpanded) {
                    binding.tvCommentaryText.maxLines = 100
                    binding.tvReadMore.text = "Read Less"
                    isExpanded = true
                }else{
                    binding.tvCommentaryText.maxLines = 3
                    binding.tvReadMore.text = "Read More..."
                    isExpanded = false
                }
            }
        }

    private fun getAndSetChapAndVerseNum() {
        val bundle = arguments
        chapterNumber = bundle?.getInt("chapterNumber")!!
        verseNumber = bundle?.getInt("verseNumber")!!

        binding.tvVerseNumber.text = "||$chapterNumber.$verseNumber||"
    }

    private fun changeStatusBarColor() {
        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.orange)
        if (window != null) {
            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = true
            }
        }
    }

    private fun getVerseDetail() {
        lifecycleScope.launch {
            viewModel.getParticularVerse(chapterNumber, verseNumber).collect { verse ->
                binding.tvVerseText.text = verse.text
                binding.tvVerseTranslation.text = verse.transliteration
                binding.tvWordIfEnglish.text = verse.word_meanings

//                _________________________________________________________________________________________________
                val englishTranslationList = arrayListOf<Translation>()

                for (i in verse.translations) {
                    if (i.language == "english") {
                        englishTranslationList.add(i)
                    }
                }

                val englishTranslationSize = englishTranslationList.size

                if (englishTranslationList.isNotEmpty()) {
                    binding.tvAuthorName.text = englishTranslationList[0].author_name
                    binding.tvText.text = englishTranslationList[0].description
                    if (englishTranslationSize == 1) {
                        binding.fabTranslationLeft.visibility = View.GONE
                        binding.fabTranslationRight.visibility = View.GONE
                    }

                    var i = 0;
                    binding.fabTranslationRight.setOnClickListener {
                        if (i < englishTranslationSize - 1) {
                            i++
                            binding.tvAuthorName.text = englishTranslationList[i].author_name
                            binding.tvText.text = englishTranslationList[i].description
                            binding.fabTranslationLeft.visibility = View.VISIBLE

                            if (i == englishTranslationSize - 1) {
                                binding.fabTranslationRight.visibility = View.GONE
                            }
                        }
                    }


                    binding.fabTranslationLeft.setOnClickListener {
                        if (i > 0) {
                            i--
                            binding.tvAuthorName.text = englishTranslationList[i].author_name
                            binding.tvText.text = englishTranslationList[i].description
                            binding.fabTranslationRight.visibility = View.VISIBLE

                            if (i == 0) {
                                binding.fabTranslationLeft.visibility = View.GONE
                            }
                        }
                    }
                }

//                _________________________________________________________________________________________________

                val englishCommentaryList = arrayListOf<Commentary>()
                for(i in verse.commentaries){
                    if(i.language=="hindi"){
                        englishCommentaryList.add(i)
                    }
                }

                val englishCommentarySize = englishCommentaryList.size

                if(englishCommentaryList.isNotEmpty()){
                    binding.tvAuthorCommentary.text = englishCommentaryList[0].author_name
                    binding.tvCommentaryText.text = englishCommentaryList[0].description
                    if(englishCommentarySize==1){
                        binding.fabCommentaryLeft.visibility = View.GONE
                        binding.fabCommentaryRight.visibility = View.GONE
                    }

                    var j = 0;
                    binding.fabCommentaryRight.setOnClickListener {
                        if(j<englishCommentarySize-1){
                            j++
                            binding.tvAuthorCommentary.text = englishCommentaryList[j].author_name
                            binding.tvCommentaryText.text = englishCommentaryList[j].description
                            binding.fabCommentaryLeft.visibility = View.VISIBLE

                            if(j==englishCommentarySize-1){
                                binding.fabCommentaryRight.visibility = View.GONE
                            }
                        }
                    }

                    binding.fabCommentaryLeft.setOnClickListener {
                        if(j>0){
                            j--
                            binding.tvAuthorCommentary.text = englishCommentaryList[j].author_name
                            binding.tvCommentaryText.text = englishCommentaryList[j].description
                            binding.fabCommentaryRight.visibility = View.VISIBLE

                            if(j==0){
                                binding.fabCommentaryLeft.visibility = View.GONE
                            }
                        }
                    }
                }
            }


        }

    }
}