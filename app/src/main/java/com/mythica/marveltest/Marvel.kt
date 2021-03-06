package com.mythica.marveltest

import android.content.Context
import android.util.Log
import android.widget.ImageView

import com.arnaudpiroelle.marvel.api.MarvelApi
import com.arnaudpiroelle.marvel.api.objects.Comic
import com.arnaudpiroelle.marvel.api.objects.ref.DataWrapper
import com.arnaudpiroelle.marvel.api.params.name.comic.ListComicParamName
import com.arnaudpiroelle.marvel.api.services.async.ComicsAsyncService
import com.squareup.picasso.Picasso

import retrofit.RetrofitError
import retrofit.client.Response

class Marvel(val context: Context)
{
    class ComicInfo(val id: Int, val title: String, val description: String, val cover: String) {
    }

    interface ComicsCallback {
        fun onComicsRefreshed(comics: MutableMap<Int, ComicInfo>)
    }

    var comicsService: ComicsAsyncService
    private val pubKey = "9f2a244b8e7bbc4c6f5acbd30981c591"
    private val privKey = "d4e531853163b095ffa1e1bd32c6818bfc76d59d"

    // Current list of comics
    private val comicInfo = mutableMapOf<Int, ComicInfo>()

    // List of subscribers to Comics events
    private val comicsListeners: MutableSet<ComicsCallback> = mutableSetOf()

    init {
        // Init API
        MarvelApi.configure().withApiKeys(pubKey, privKey).init()
        comicsService = MarvelApi.getService(ComicsAsyncService::class.java)
    }

    fun getComics()
    {
//        https://howtodoandroid.com/retrofit-android-example-kotlin/
//        https://developer.marvel.com/docs#!/public/getComicsCollection_get_6
//        https://developer.marvel.com/documentation/images

        // Get list of comics
        val options = mutableMapOf<ListComicParamName, String>()
        comicsService.listComic(options, object: retrofit.Callback<DataWrapper<Comic>> {
            override fun success(data: DataWrapper<Comic>?, response: Response?) {
                Log.d("Marvel", data.toString())

                // Marshall data to structs
                comicInfo.clear()
                val comics = data!!.data!!.results
                for (comic in comics) {
                    var imageUrl = ""
                    if (!comic.thumbnail.path.isEmpty()) {
                        Log.e("Marvel", "Thumbnail Path: " + comic.thumbnail.path)
                        imageUrl = comic.thumbnail.path + "/portrait_medium." + comic.thumbnail.extension
                        Log.e("Marvel", "Image URL: " + imageUrl)
                    }
                    comicInfo[comic.id] = ComicInfo(comic.id, comic.title, comic.description, imageUrl)
                }

                // Notify subscribers
                onComicsRefreshed()
            }

            override fun failure(error: RetrofitError?) {
                Log.d("Marvel", error!!.message!!)
            }
        })
    }

    fun loadImage(imageUrl: String, imageView: ImageView)
    {
        if (!imageUrl.isEmpty()) {
            Picasso.with(context).load(imageUrl).fit().centerCrop()
                .into(imageView);
        }
    }

    fun subscribeComicEvents(listener: ComicsCallback) {
        comicsListeners += listener
    }

    fun unsubscribeComicEvents(listener: ComicsCallback) {
        comicsListeners -= listener
    }

    fun onComicsRefreshed() {
        for (callback in comicsListeners) {
            callback.onComicsRefreshed(comicInfo)
        }
    }
}
