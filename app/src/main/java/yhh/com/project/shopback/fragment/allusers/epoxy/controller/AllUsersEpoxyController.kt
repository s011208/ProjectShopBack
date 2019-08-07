package yhh.com.project.shopback.fragment.allusers.epoxy.controller

import com.airbnb.epoxy.Typed2EpoxyController
import com.airbnb.epoxy.TypedEpoxyController
import io.reactivex.subjects.PublishSubject
import yhh.com.project.repository.entity.GithubUserEntity
import yhh.com.project.shopback.R
import yhh.com.project.shopback.fragment.allusers.epoxy.model.allUsers
import yhh.com.project.shopback.fragment.allusers.epoxy.model.loadingFail
import yhh.com.project.shopback.fragment.allusers.epoxy.model.userTitle

class AllUsersEpoxyController : Typed2EpoxyController<List<GithubUserEntity>, Boolean>() {

    val clickLoginIntent = PublishSubject.create<String>()
    val clickReloadIntent = PublishSubject.create<Unit>()

    override fun buildModels(data: List<GithubUserEntity>, isFail: Boolean) {

        if (isFail) {
            loadingFail {
                clickIntent(clickReloadIntent)
                message(R.string.epoxy_model_loading_fail)
                id("loadingFail")
            }
        } else {
            if (data.isNotEmpty()) {
                userTitle { id("userTitle") }

                data.forEach { entity ->
                    allUsers {
                        avatarUrl(entity.avatarUrl)
                        siteAdmin(entity.isSiteAdmin)
                        login(entity.login)
                        id(entity.login)
                        clickIntent(clickLoginIntent)
                    }
                }
            }
        }
    }
}