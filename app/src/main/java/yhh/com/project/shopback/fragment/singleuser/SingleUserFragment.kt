package yhh.com.project.shopback.fragment.singleuser

import android.content.Context
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import yhh.com.project.shopback.R
import yhh.com.project.shopback.fragment.singleuser.mvrx.viewmodel.SingleUserFragmentViewModel
import javax.inject.Inject

class SingleUserFragment : BaseMvRxFragment() {

    @Inject
    lateinit var viewModelFactory: SingleUserFragmentViewModel.Factory

    private val viewModel: SingleUserFragmentViewModel by fragmentViewModel()

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = object: OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.allUsersFragment, false)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun invalidate() {

    }
}