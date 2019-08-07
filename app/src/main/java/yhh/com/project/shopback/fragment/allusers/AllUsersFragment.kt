package yhh.com.project.shopback.fragment.allusers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.*
import com.jakewharton.rxbinding3.view.clicks
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_all_users.*
import timber.log.Timber
import yhh.com.project.shopback.R
import yhh.com.project.shopback.fragment.allusers.epoxy.controller.AllUsersEpoxyController
import yhh.com.project.shopback.fragment.allusers.mvrx.viewmodel.AllUsersFragmentViewModel
import javax.inject.Inject

class AllUsersFragment : BaseMvRxFragment() {

    @Inject
    lateinit var viewModelFactory: AllUsersFragmentViewModel.Factory

    private val viewModel: AllUsersFragmentViewModel by fragmentViewModel()

    private val compositeDisposable = CompositeDisposable()

    private val epoxyController = AllUsersEpoxyController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        epoxyController.onRestoreInstanceState(savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compositeDisposable += previous.clicks().subscribe { viewModel.loadPreviousPage() }
        compositeDisposable += next.clicks().subscribe { viewModel.loadNextPage() }
        compositeDisposable += epoxyController.clickLoginIntent.subscribe {
            Timber.v("login: $it")
        }
        compositeDisposable += epoxyController.clickReloadIntent.subscribe { viewModel.reload() }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = epoxyController.adapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        epoxyController.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
        epoxyController.cancelPendingModelBuild()
    }

    override fun invalidate() {
        withState(viewModel) { state ->
            next.isEnabled = state.hasNextPage
            previous.isEnabled = state.hasPreviousPage
            when (state.usersAsync) {
                is Uninitialized -> {
                    progressBar.visibility = View.INVISIBLE
                    epoxyController.setData(ArrayList(), false)
                }
                is Loading -> {
                    progressBar.visibility = View.VISIBLE
                    epoxyController.setData(ArrayList(), false)
                }
                is Success -> {
                    progressBar.visibility = View.INVISIBLE
                    epoxyController.setData(state.usersAsync.invoke(), false)
                }
                is Fail -> {
                    progressBar.visibility = View.INVISIBLE
                    epoxyController.setData(ArrayList(), true)
                }
            }
        }
    }
}