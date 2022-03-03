package com.mythica.marveltest.ui.home

// Android stuff
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.mythica.marveltest.MainActivity

// Third party
import com.mythica.marveltest.R
import com.mythica.marveltest.Marvel

// ---------------------------------------------------------------------------------------
//  ComicListItemAdapter - Adapter for comic list view
// ---------------------------------------------------------------------------------------
class ComicListItemAdapter constructor(val clickListener: ComicItemClickListener): RecyclerView.Adapter<ComicListItemAdapter.ViewHolder>()
{
    // Use to subscribe to comic item clicks
    interface ComicItemClickListener {
        fun onComicClicked(comicIndex: Int)
        fun onComicLongPressed(comicIndex: Int)
    }

    // List of comics
    private var comicList = mutableListOf<Marvel.ComicInfo>()

    // ---------------------------------------------------------------------------------------
    //  getComicId - Get Comic ID from list index
    // ---------------------------------------------------------------------------------------
    fun getComicId(index: Int): Int {
        return comicList[index].id
    }

    // ---------------------------------------------------------------------------------------
    //  setList - Set updated list of Comics
    // ---------------------------------------------------------------------------------------
    fun setList(newList: MutableMap<Int, Marvel.ComicInfo>)
    {
        // Remove old list
        val oldSize = comicList.size
        comicList.clear()
        notifyItemRangeRemoved(0, oldSize)

        // Add new list
        for (item in newList) {
            comicList.add(item.value)
        }

        notifyItemRangeInserted(0, comicList.size)
    }

    // ---------------------------------------------------------------------------------------
    //  clear - Clear the list of Comics
    // ---------------------------------------------------------------------------------------
    fun clear()
    {
        val oldSize = comicList.size
        comicList.clear()
        notifyItemRangeRemoved(0, oldSize)
    }

    // ---------------------------------------------------------------------------------------
    //  ViewHolder - Holder for all the view items to be filled in by the data
    // ---------------------------------------------------------------------------------------
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val title = itemView.findViewById<TextView>(R.id.comic_title)
        val description = itemView.findViewById<TextView>(R.id.comic_desc)
        val cover = itemView.findViewById<ImageView>(R.id.comic_cover)

        init {
            itemView.setOnClickListener{ clickListener.onComicClicked(adapterPosition) }
            itemView.setOnLongClickListener{ clickListener.onComicLongPressed(adapterPosition)
                return@setOnLongClickListener true }
        }
    }

    // ---------------------------------------------------------------------------------------
    //  getItemCount - Return total number of items in the list
    // ---------------------------------------------------------------------------------------
    override fun getItemCount(): Int {
        return comicList.size
    }

    // ---------------------------------------------------------------------------------------
    //  onCreateViewHolder - Create the view for one item
    // ---------------------------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicListItemAdapter.ViewHolder
    {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val comicItemView = inflater.inflate(R.layout.comic_list_item, parent, false)
        return ViewHolder(comicItemView)
    }

    // ---------------------------------------------------------------------------------------
    //  onBindViewHolder - Populate the specified item view
    // ---------------------------------------------------------------------------------------
    override fun onBindViewHolder(viewHolder: ComicListItemAdapter.ViewHolder, position: Int)
    {
        // Get the data model based on position
        val comic: Marvel.ComicInfo = comicList[position]

        // Set text items
        viewHolder.title.text = comic.title
        viewHolder.description.text = comic.description

        // Set cover image
        if (comic.cover.isEmpty()) {
            viewHolder.cover.visibility = View.GONE
        }
        else {
            viewHolder.cover.visibility = View.VISIBLE
            MainActivity.instance.getMarvelApi().loadImage(comic.cover, viewHolder.cover!!)
        }
    }
}
