package RecyclerViewHelpers

import Angel.Nataren.crudangelynataren.R
import DataClassMusica
import android.app.AlertDialog
import android.net.Uri.Builder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import modelo.claseConexion

class Adaptador(var Datos: List<DataClassMusica>): RecyclerView.Adapter<ViewHolder>(){


    //funcion para que cueando agrugue datos
    //se actualize la lsita automaticamente

    fun actualizarListado(nuevaLista: List<DataClassMusica>){

        Datos = nuevaLista
        notifyDataSetChanged()
    }

    ////////// Funcion TODO ELIMINAR DATOS

    fun eliminarDatos(nombreCancion: String, position: Int){

        //Eliminarlo de la lista
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(position)

        //Eliminar de la base de datos
        GlobalScope.launch(Dispatchers.IO){
            //Creo un objeto de la clase conexion
            val objConexion = claseConexion().cadenaDeConexion()

            //-2 creo una variable que tenga un prepare statement

            val deleteCancion = objConexion?.prepareStatement("delete Musica where nombreCancion = ?")!!

            deleteCancion.setString(1, nombreCancion)
            deleteCancion.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }
        Datos = listaDatos.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista =  LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = Datos[position]
        holder.txtNombre.text = item.nombreCancion

        //TODO: click al icono de borrar
        holder.imgBorrar.setOnClickListener{

        //Creo la alerta para confirmar la eliminacion
        //-1 invoco el contexto
            val context = holder.itemView.context

        //-2 creo la alerta usando los 3 pasos
            //TODO: titulo, mensaje, botones

            val builder =AlertDialog.Builder(context)

            builder.setTitle("Confirmacion")
            builder.setMessage("¿Estas seguro?")

            builder.setPositiveButton("Si"){ dialog, wich ->
                eliminarDatos(item.nombreCancion, position)

            }

            builder.setNegativeButton("No"){ dialog, wich ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

        }

    }




}