package com.example.shoutbox

import android.app.Application
import com.example.shoutbox.api.ShoutboxApi
import com.example.shoutbox.db.ShoutboxDatabase
import com.example.shoutbox.repository.ShoutboxRepository
import com.example.shoutbox.ui.shoutbox.ShoutboxViewModelFactory
import net.danlew.android.joda.JodaTimeAndroid
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber

class BaseApplication : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@BaseApplication))
        bind() from singleton { ShoutboxDatabase(instance()) }
        bind() from singleton { instance<ShoutboxDatabase>().messageDao() }

        bind() from singleton { ShoutboxApi() }
        bind() from singleton { ShoutboxRepository(instance(), instance()) }

        bind() from provider { ShoutboxViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()

        JodaTimeAndroid.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}