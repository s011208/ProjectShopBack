package yhh.com.project.shopback.fragment.allusers.epoxy.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.subjects.PublishSubject
import yhh.com.project.shopback.R
import yhh.com.project.shopback.external.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.epoxy_model_all_users)
abstract class AllUsersEpoxyModel : EpoxyModelWithHolder<AllUsersEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var avatarUrl: String

    @EpoxyAttribute
    lateinit var login: String

    @EpoxyAttribute
    var siteAdmin: Boolean = false

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickIntent: PublishSubject<String>

    override fun bind(holder: Holder) {
        super.bind(holder)

        holder.login.text = login
        holder.siteAdmin.visibility = if (siteAdmin) View.VISIBLE else View.GONE
        holder.container.setOnClickListener { clickIntent.onNext(login) }

        Glide
            .with(holder.avatar)
            .load(avatarUrl)
            .apply(
                RequestOptions()
                    .error(android.R.drawable.ic_delete)
                    .placeholder(
                        CircularProgressDrawable(holder.avatar.context)
                            .apply {
                                strokeWidth = 5f
                                centerRadius = 30f
                                start()
                            }).centerCrop()
            )
            .into(holder.avatar)
    }

    override fun unbind(holder: Holder) {
        super.unbind(holder)
        Glide
            .with(holder.avatar)
            .clear(holder.avatar)
    }

    class Holder : KotlinEpoxyHolder() {
        val container by bind<ConstraintLayout>(R.id.container)
        val avatar by bind<ImageView>(R.id.avatar)
        val login by bind<TextView>(R.id.login)
        val siteAdmin by bind<TextView>(R.id.siteAdmin)
    }
}