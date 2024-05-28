package RecyclerViewHelpers

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.R

class ViewHolder(view: View): RecyclerView.ViewHolder(view){

    val txtNombre: TextView = view.findViewById(Angel.Nataren.crudangelynataren.R.id.txtNombreCard)

    val imgBorrar: ImageView = view.findViewById(Angel.Nataren.crudangelynataren.R.id.imgBorrar)
}