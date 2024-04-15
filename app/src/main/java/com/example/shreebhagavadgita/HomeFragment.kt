package com.example.shreebhagavadgita

import android.os.Bundle
import android.util.Log
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
import com.example.shreebhagavadgita.databinding.FragmentHomeBinding
import com.example.shreebhagavadgita.models.ChaptersItem
import com.example.shreebhagavadgita.view.adapter.AdapterChapters
import com.example.shreebhagavadgita.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {


    private  lateinit var  binding: FragmentHomeBinding
    private  lateinit var adapterChapters: AdapterChapters
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(layoutInflater)

        changeStatusBarColor()
        checkInternetConnectivity()
//        getAllChapters()
        return binding.root
    }

    private fun checkInternetConnectivity() {

//        what is requiredContext()?  https://stackoverflow.com/questions/50699201/what-is-requirecontext
//
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner) {
            if (it) {
                getAllChapters()
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.chapterRecyclerView.visibility = View.VISIBLE
                binding.noInternetMessage.visibility = View.GONE
            } else {
                binding.shimmerLayout.visibility = View.GONE
                binding.chapterRecyclerView.visibility = View.GONE
                binding.noInternetMessage.visibility = View.VISIBLE

            }
        }
    }

    private  fun onChapterIVClicked(chaptersItem: ChaptersItem){

            val bundle = Bundle()
            bundle.putInt ("chapterNumber",chaptersItem.chapter_number)
            bundle.putString ("chapterTitle", chaptersItem.name_translated)
            bundle.putString ("chapterDesc", chaptersItem.chapter_summary)
            bundle.putInt ("verseCount", chaptersItem.verses_count)

            findNavController().navigate(R.id.action_homeFragment_to_versesFragment,bundle)
    }

    private fun getAllChapters() {
        lifecycleScope.launch {

            viewModel.getAllChapters().collect {
                chapterList ->
                 for(i in chapterList){
                     adapterChapters = AdapterChapters(::onChapterIVClicked)
                     binding.chapterRecyclerView.adapter = adapterChapters
                     adapterChapters.differ.submitList(chapterList)
                     binding.shimmerLayout.visibility = View.GONE

                     Log.d("TAG", "getAllChapters: $chapterList")
                 }
            }
        }
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