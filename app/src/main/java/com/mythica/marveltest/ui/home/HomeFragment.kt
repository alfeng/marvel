package com.mythica.marveltest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mythica.marveltest.MainActivity
import com.mythica.marveltest.databinding.FragmentHomeBinding
import com.mythica.marveltest.Marvel

class HomeFragment : Fragment(), ComicListItemAdapter.ComicItemClickListener, Marvel.ComicsCallback
{
    companion object {
        // Track current position of the Comics list
        var currentScrollPosition = 0
    }

    // This property is only valid between onCreateView and onDestroyView
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var comicAdapter: ComicListItemAdapter
    private val marvelApi = MainActivity.instance.getMarvelApi()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val comicList = binding.comicList

        // Initialize list and adapter
        comicAdapter = ComicListItemAdapter(this)
        binding.comicList.adapter = comicAdapter
        binding.comicList.layoutManager = LinearLayoutManager(comicList.context)
        binding.comicList.addItemDecoration(DividerItemDecoration(comicList.context, DividerItemDecoration.VERTICAL))

        // Remember current scroll position
        binding.comicList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int)
            {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    currentScrollPosition = layoutManager.findLastVisibleItemPosition()
                }
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause()
    {
        // Pause location services
        MainActivity.instance.getMarvelApi()
        marvelApi.unsubscribeComicEvents(this)
        super.onPause()
    }

    override fun onResume()
    {
        // Permissions can disappear at any time, so always check for them when foregrounded
        super.onResume()
        marvelApi.subscribeComicEvents(this)
    }

    override fun onComicClicked(comicIndex: Int)
    {
        Toast.makeText(activity, "Comic clicked: ${comicAdapter.getComicId(comicIndex)}", Toast.LENGTH_SHORT).show()

        // Launch "View Comic" Activity
//        val intent = Intent(this, ViewComicActivity::class.java)
//        intent.putExtra(ViewComicActivity.EXTRA_EVENT_ID, comicAdapter.getComicId(eventIndex))
//        startActivity(intent)
    }

    override fun onComicLongPressed(comicIndex: Int)
    {
        Toast.makeText(activity, "Comic long-pressed: ${comicAdapter.getComicId(comicIndex)}", Toast.LENGTH_SHORT).show()
    }

    override fun onComicsRefreshed(comics: MutableMap<Int, Marvel.ComicInfo>)
    {
        // Notify adapter there's a new set of data
        comicAdapter.setList(comics)

        // Set current scroll position
        binding.comicList.layoutManager?.scrollToPosition(currentScrollPosition)
    }
}
