package yhh.com.project.shopback.fragment.singleuser

import android.content.Context
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import yhh.com.project.shopback.fragment.singleuser.mvrx.viewmodel.SingleUserFragmentViewModel
import javax.inject.Inject

class SingleUserFragment : BaseMvRxFragment() {

    @Inject
    lateinit var viewModelFactory: SingleUserFragmentViewModel.Factory

    private val viewModel: SingleUserFragmentViewModel by fragmentViewModel()

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun invalidate() {

    }
}