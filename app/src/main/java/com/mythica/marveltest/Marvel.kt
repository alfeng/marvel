package com.mythica.marveltest

import android.content.Context
import android.util.Log

import com.arnaudpiroelle.marvel.api.MarvelApi
import com.arnaudpiroelle.marvel.api.objects.Comic
import com.arnaudpiroelle.marvel.api.objects.ref.DataWrapper
import com.arnaudpiroelle.marvel.api.params.name.comic.ListComicParamName
import com.arnaudpiroelle.marvel.api.services.async.ComicsAsyncService

import retrofit.RetrofitError
import retrofit.client.Response

class Marvel(val context: Context)
{
    var comicsService: ComicsAsyncService
    private val pubKey = "9f2a244b8e7bbc4c6f5acbd30981c591"
    private val privKey = "d4e531853163b095ffa1e1bd32c6818bfc76d59d"

    init {
        // Init API
        MarvelApi.configure().withApiKeys(pubKey, privKey).init()
        comicsService = MarvelApi.getService(ComicsAsyncService::class.java)
    }

    fun getComics() {

//        https://howtodoandroid.com/retrofit-android-example-kotlin/
//        https://developer.marvel.com/docs#!/public/getComicsCollection_get_6

        // Get list of comics
        val options = mutableMapOf<ListComicParamName, String>()
        comicsService.listComic(options, object: retrofit.Callback<DataWrapper<Comic>> {
            override fun success(data: DataWrapper<Comic>?, response: Response?) {
                Log.d("Marvel", data.toString())
            }

            override fun failure(error: RetrofitError?) {
                Log.d("Marvel", error!!.message!!)
            }
        })
    }

    fun getComicInfo(id: Int) {
        // Get comic title, description, cover image
    }

    // Create ListView of comic titles, descriptions, and images
    // Need adapter and RecyclerView

}
