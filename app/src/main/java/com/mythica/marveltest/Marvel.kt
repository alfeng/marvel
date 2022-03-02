package com.mythica.marveltest

import android.content.Context
import android.util.Log
import android.widget.ImageView

import com.arnaudpiroelle.marvel.api.MarvelApi
import com.arnaudpiroelle.marvel.api.objects.Comic
import com.arnaudpiroelle.marvel.api.objects.Image
import com.arnaudpiroelle.marvel.api.objects.ref.DataWrapper
import com.arnaudpiroelle.marvel.api.params.name.comic.ListComicParamName
import com.arnaudpiroelle.marvel.api.services.async.ComicsAsyncService

import retrofit.RetrofitError
import retrofit.client.Response

class Marvel(val context: Context)
{
    class ComicInfo(val id: Int, val title: String, val description: String, val cover: Image) {
    }

    var comicsService: ComicsAsyncService
    private val pubKey = "9f2a244b8e7bbc4c6f5acbd30981c591"
    private val privKey = "d4e531853163b095ffa1e1bd32c6818bfc76d59d"

    init {
        // Init API
        MarvelApi.configure().withApiKeys(pubKey, privKey).init()
        comicsService = MarvelApi.getService(ComicsAsyncService::class.java)
    }

    fun getComics()
    {
//        https://howtodoandroid.com/retrofit-android-example-kotlin/
//        https://developer.marvel.com/docs#!/public/getComicsCollection_get_6

        // Get list of comics
        val options = mutableMapOf<ListComicParamName, String>()
        comicsService.listComic(options, object: retrofit.Callback<DataWrapper<Comic>> {
            override fun success(data: DataWrapper<Comic>?, response: Response?) {
                Log.d("Marvel", data.toString())

                // Marshall data to structs
                val comicInfo = mutableMapOf<Int, ComicInfo>()
                val comics = data!!.data!!.results
                for (comic in comics) {
                    comicInfo[comic.id] = ComicInfo(comic.id, comic.title, comic.description, comic.thumbnail)
                }
            }

            override fun failure(error: RetrofitError?) {
                Log.d("Marvel", error!!.message!!)
            }
        })
    }

    fun loadImage(filename: String, imgView: ImageView) {
        // Get comic title, description, cover image
    }

    // Create ListView of comic titles, descriptions, and images
    // Need adapter and RecyclerView

    // Comic Book ID
    // Title
    // Description
    // Cover image

}