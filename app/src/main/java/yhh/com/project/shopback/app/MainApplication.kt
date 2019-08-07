package yhh.com.project.shopback.app

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class MainApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<DaggerApplication> =
        DaggerMainApplicationComponent
            .builder()
            .application(this)
            .build()
            .also {
                it.inject(this)
            }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}