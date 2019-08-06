package yhh.com.project.shopback.external.dagger2

import dagger.Module
import dagger.android.ContributesAndroidInjector
import yhh.com.project.shopback.activity.MainActivity

@Module
abstract class ActivityFragmentBuilder {
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}
