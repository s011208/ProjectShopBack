package yhh.com.project.shopback.fragment.allusers.epoxy.model

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import yhh.com.project.shopback.R
import yhh.com.project.shopback.external.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.epoxy_model_user_title)
abstract class UserTitleEpoxyModel : EpoxyModelWithHolder<UserTitleEpoxyModel.Holder>() {

    class Holder : KotlinEpoxyHolder()
}