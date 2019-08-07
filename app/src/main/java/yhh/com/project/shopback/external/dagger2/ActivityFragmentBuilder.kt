package yhh.com.project.shopback.external.dagger2

import dagger.Module
import dagger.android.ContributesAndroidInjector
import yhh.com.project.shopback.activity.main.MainActivity
import yhh.com.project.shopback.fragment.allusers.AllUsersFragment
import yhh.com.project.shopback.fragment.singleuser.SingleUserFragment

@Module
abstract class ActivityFragmentBuilder {
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindAllUsersFragment(): AllUsersFragment

    @ContributesAndroidInjector
    abstract fun bindSingleUserFragment(): SingleUserFragment
}
