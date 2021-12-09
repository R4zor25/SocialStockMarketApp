package hu.bme.aut.android.socialstockmarketapp.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//Required for Hilt Dependency Injection
@HiltAndroidApp
class StockApplication: Application() {
}