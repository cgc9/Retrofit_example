package com.example.retrofitexample

object ModelWeather {
    data class Result(val main: Main)
    data class Main(val temp:String)
}