package yhh.com.project.shopback.fragment.singleuser.epoxy.controller

import com.airbnb.epoxy.Typed2EpoxyController
import com.airbnb.epoxy.TypedEpoxyController
import io.reactivex.subjects.PublishSubject
import yhh.com.project.repository.entity.GithubUserEntity
import yhh.com.project.shopback.R
import yhh.com.project.shopback.fragment.allusers.epoxy.model.loadingFail
import yhh.com.project.shopback.fragment.singleuser.epoxy.model.singleUser

class SingleUserEpoxyController : Typed2EpoxyController<GithubUserEntity?, Boolean>() {

    val backIntent = PublishSubject.create<Unit>()
    val clickReloadIntent = PublishSubject.create<Unit>()

    override fun buildModels(data: GithubUserEntity?, isFail: Boolean) {
        if (isFail) {
            loadingFail {
                clickIntent(clickReloadIntent)
                message(R.string.epoxy_model_loading_fail)
                id("loadingFail")
            }
        } else {
            if (data != null) {
                singleUser {
                    backIntent(backIntent)
                    avatarUrl(data.avatarUrl)
                    name(data.name ?: "")
                    bio(data.bio ?: "")
                    login(data.login)
                    siteAdmin(data.isSiteAdmin)
                    location(data.location ?: "")
                    blog(data.blog ?: "")
                    id(data.login)
                }
            }
        }
    }
}