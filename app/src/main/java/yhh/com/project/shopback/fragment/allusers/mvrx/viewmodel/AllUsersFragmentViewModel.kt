package yhh.com.project.shopback.fragment.allusers.mvrx.viewmodel

import com.airbnb.mvrx.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import yhh.com.project.repository.entity.GithubUserEntity
import yhh.com.project.repository.repository.GithubRepository
import yhh.com.project.shopback.external.mvrx.MvRxViewModel
import yhh.com.project.shopback.fragment.allusers.AllUsersFragment

data class AllUsersFragmentState(
    val currentPage: Long = 0,
    val usersAsync: Async<List<GithubUserEntity>> = Uninitialized,
    val hasPreviousPage: Boolean = false,
    val hasNextPage: Boolean = false
) : MvRxState

class AllUsersFragmentViewModel @AssistedInject constructor(
    @Assisted initialState: AllUsersFragmentState,
    private val githubRepository: GithubRepository
) : MvRxViewModel<AllUsersFragmentState>(initialState) {

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: AllUsersFragmentState): AllUsersFragmentViewModel
    }

    companion object : MvRxViewModelFactory<AllUsersFragmentViewModel, AllUsersFragmentState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: AllUsersFragmentState
        ): AllUsersFragmentViewModel? {
            return (viewModelContext as FragmentViewModelContext).fragment<AllUsersFragment>()
                .viewModelFactory.create(state)
        }
    }

    private var disposable: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    init {
        loadPage(0)
    }

    fun loadNextPage() {
        withState {
            loadPage(it.currentPage + 1)
        }
    }

    fun loadPreviousPage() {
        withState {
            loadPage(it.currentPage - 1)
        }
    }

    fun reload() {
        withState {
            loadPage(it.currentPage)
        }
    }

    private fun loadPage(page: Long) {
        disposable?.dispose()
        disposable = githubRepository.getUsers(page)
            .subscribeOn(Schedulers.io())
            .doOnError { Timber.w(it, "failed to load page: $page") }
            .execute { result ->
                if (result is Loading) {
                    copy(usersAsync = result, hasPreviousPage = false, hasNextPage = false)
                } else {
                    copy(currentPage = page, usersAsync = result, hasPreviousPage = page > 0, hasNextPage = githubRepository.getSince(page + 1).blockingGet() >= 0).also {
                        Timber.v("load page($page) result: $result")
                    }
                }
            }
    }
}