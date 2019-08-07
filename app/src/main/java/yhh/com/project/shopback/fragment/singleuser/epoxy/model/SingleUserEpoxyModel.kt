package yhh.com.project.shopback.fragment.singleuser.epoxy.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.subjects.PublishSubject
import yhh.com.project.shopback.R
import yhh.com.project.shopback.external.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.epoxy_model_single_user)
abstract class SingleUserEpoxyModel : EpoxyModelWithHolder<SingleUserEpoxyModel.Holder>() {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var backIntent: PublishSubject<Unit>

    @EpoxyAttribute
    lateinit var avatarUrl: String

    @EpoxyAttribute
    var name: String = ""

    @EpoxyAttribute
    var bio: String = ""

    @EpoxyAttribute
    lateinit var login: String

    @EpoxyAttribute
    var siteAdmin: Boolean = false

    @EpoxyAttribute
    var location: String = ""

    @EpoxyAttribute
    var blog: String = ""

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.back.setOnClickListener { backIntent.onNext(Unit) }

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

        holder.name.text = name
        holder.bio.text = bio
        holder.login.text = login
        holder.siteAdmin.visibility = if (siteAdmin) View.VISIBLE else View.GONE

        if (location.isBlank()) {
            holder.location.visibility = View.GONE
        } else {
            holder.location.visibility = View.VISIBLE
            holder.location.text = location
        }

        if (blog.isBlank()) {
            holder.blog.visibility = View.GONE
        } else {
            holder.blog.visibility = View.VISIBLE
            holder.blog.text = blog
        }
    }

    override fun unbind(holder: Holder) {
        super.unbind(holder)
        Glide
            .with(holder.avatar)
            .clear(holder.avatar)
    }

    class Holder : KotlinEpoxyHolder() {
        val back by bind<ImageView>(R.id.back)
        val avatar by bind<ImageView>(R.id.avatar)
        val name by bind<TextView>(R.id.name)
        val bio by bind<TextView>(R.id.bio)
        val login by bind<TextView>(R.id.login)
        val siteAdmin by bind<TextView>(R.id.siteAdmin)
        val location by bind<TextView>(R.id.location)
        val blog by bind<TextView>(R.id.blog)
    }
}