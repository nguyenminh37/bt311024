
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SpinnerAdapter(
    context: Context,
    private val resourceId: Int,
    private val items: List<String>
) : ArrayAdapter<String>(context, resourceId, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(convertView, position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(convertView, position, parent)
    }

    private fun createViewFromResource(convertView: View?, position: Int, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resourceId, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = items[position]
        return view
    }
}
