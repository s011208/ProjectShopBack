package yhh.com.project.shopback.fragment.allusers.epoxy.model

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import io.reactivex.subjects.PublishSubject
import yhh.com.project.shopback.R
import yhh.com.project.shopback.external.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.epoxy_model_loading_fail)
abstract class LoadingFailEpoxyModel : EpoxyModelWithHolder<LoadingFailEpoxyModel.Holder>() {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickIntent: PublishSubject<Unit>

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.container.setOnClickListener { clickIntent.onNext(Unit) }
    }

    class Holder : KotlinEpoxyHolder() {
        val container by bind<TextView>(R.id.container)
    }
}