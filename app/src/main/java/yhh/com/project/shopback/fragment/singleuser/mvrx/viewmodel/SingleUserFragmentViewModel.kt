package yhh.com.project.shopback.fragment.singleuser.mvrx.viewmodel

import android.os.Parcelable
import com.airbnb.mvrx.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.disposables.Disposable
import kotlinx.android.parcel.Parcelize
import yhh.com.project.repository.entity.GithubUserEntity
import yhh.com.project.repository.repository.GithubRepository
import yhh.com.project.shopback.external.mvrx.MvRxViewModel
import yhh.com.project.shopback.fragment.singleuser.SingleUserFragment

@Parcelize
data class SingleUserFragmentStateArgs(val login: String = "") : Parcelable

data class SingleUserFragmentState(
    val login: String,
    val userAsync: Async<GithubUserEntity> = Uninitialized
) : MvRxState {
    constructor(args: SingleUserFragmentStateArgs) : this(login = args.login)
}

class SingleUserFragmentViewModel @AssistedInject constructor(
    @Assisted initialState: SingleUserFragmentState,
    private val githubRepository: GithubRepository
) : MvRxViewModel<SingleUserFragmentState>(initialState) {

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: SingleUserFragmentState): SingleUserFragmentViewModel
    }

    companion object : MvRxViewModelFactory<SingleUserFragmentViewModel, SingleUserFragmentState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SingleUserFragmentState
        ): SingleUserFragmentViewModel? {
            return (viewModelContext as FragmentViewModelContext).fragment<SingleUserFragment>()
                .viewModelFactory.create(state)
        }
    }

    private var disposable: Disposable? = null

    init {

    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}