package yhh.com.project.shopback.app

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import yhh.com.project.repository.database.AppDatabase
import yhh.com.project.repository.database.AppDatabaseModule
import yhh.com.project.shopback.external.dagger2.ActivityFragmentBuilder
import yhh.com.project.shopback.external.dagger2.AppAssistedModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppAssistedModule::class,
        ActivityFragmentBuilder::class,
        AndroidSupportInjectionModule::class,
        AppDatabaseModule::class]
)
interface MainApplicationComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(applicationContext: Context): Builder

        fun build(): MainApplicationComponent
    }

    fun inject(application: MainApplication)

    override fun inject(instance: DaggerApplication?)

    fun provideDatabase(): AppDatabase
}