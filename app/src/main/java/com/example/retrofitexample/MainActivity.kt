package com.example.retrofitexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    private val wikiApiServe by lazy {
        WikiApiService.create()
    }

    private val weatherApiServe by lazy {
        WeatherApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_search.setOnClickListener {
            if (edit_search.text.toString().isNotEmpty()) {
                beginSearch(edit_search.text.toString())
            }
        }

        btn_cityWeather.setOnClickListener {
            if (edit_cityName.text.toString().isNotEmpty()) {
                cityWeather(edit_cityName.text.toString())
            }
        }
    }
    private fun beginSearch(searchString: String) {
        disposable = wikiApiServe.hitCountCheck("query", "json", "search", searchString)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> txt_search_result.text = "${result.query.searchinfo.totalhits} resultados" },
                { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
            )
    }

    private fun cityWeather(cityName:String){
        disposable=weatherApiServe.getWeather(cityName,"40d0434e613daa01fa9243314c5fe462")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> textWeather.text = "${result.main.temp} grados kelvin" },
                        { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
                )

    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
