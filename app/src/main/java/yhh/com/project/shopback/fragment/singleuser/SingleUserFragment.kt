package yhh.com.project.shopback.fragment.singleuser

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.*
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_single_user.*
import yhh.com.project.shopback.R
import yhh.com.project.shopback.fragment.singleuser.epoxy.controller.SingleUserEpoxyController
import yhh.com.project.shopback.fragment.singleuser.mvrx.viewmodel.SingleUserFragmentViewModel
import javax.inject.Inject

class SingleUserFragment : BaseMvRxFragment() {

    @Inject
    lateinit var viewModelFactory: SingleUserFragmentViewModel.Factory

    private val viewModel: SingleUserFragmentViewModel by fragmentViewModel()

    private lateinit var compositeDisposable: CompositeDisposable

    private val epoxyController = SingleUserEpoxyController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                findNavController()
                    .popBackStack(R.id.allUsersFragment, false)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        epoxyController.onRestoreInstanceState(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        compositeDisposable = CompositeDisposable()
        return inflater.inflate(R.layout.fragment_single_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = epoxyController.adapter

        compositeDisposable += epoxyController.backIntent
            .subscribe { findNavController().popBackStack(R.id.allUsersFragment, false) }

        compositeDisposable += epoxyController.clickReloadIntent
            .subscribe { viewModel.reload() }

        viewModel.reload()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        epoxyController.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        epoxyController.cancelPendingModelBuild()
        compositeDisposable.dispose()
    }

    override fun invalidate() {
        withState(viewModel) {
            when (it.userAsync) {
                is Uninitialized -> {
                    progressBar.visibility = View.INVISIBLE
                }
                is Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Fail -> {
                    progressBar.visibility = View.INVISIBLE
                    epoxyController.setData(null, true)
                }
                is Success -> {
                    progressBar.visibility = View.INVISIBLE
                    epoxyController.setData(it.userAsync.invoke(), false)
                }
            }
        }
    }
}