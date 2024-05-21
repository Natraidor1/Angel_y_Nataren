package Angel.Nataren.crudangelynataren

import DataClassMusica
import RecyclerViewHelpers.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.claseConexion

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //mandar a llamar a todos los elementos
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtDuracion = findViewById<EditText>(R.id.txtDuracion)
        val txtAutor = findViewById<EditText>(R.id.txtAutor)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)

        val rcvMusica = findViewById<RecyclerView>(R.id.rcvMusica)
        //asignarle un layout al RCV

        rcvMusica.layoutManager = LinearLayoutManager(this)


        fun mostrarDatos(): List<DataClassMusica> {
        //Crea un objeto de la clase conexion
            val objConexion = claseConexion().cadenaDeConexion()

            //crea un statement
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM Musica")!!

            //Voy a guardar todo lo que traiga el select
            val canciones = mutableListOf<DataClassMusica>()

            while (resultSet.next()){

            val nombre = resultSet.getString("nombreCancion")
            val cancion = DataClassMusica(nombre)
            canciones.add(cancion)

            }

            return canciones



        }

        //Asognar el adapter al RCV
        //Ejecutar la funcion para mostrar datos
        CoroutineScope(Dispatchers.IO).launch{
            //variable que ejecute la funcion de mostrar datos

            val musicaDB = mostrarDatos()

            withContext(Dispatchers.Main){
                val miAdaptador = Adaptador(musicaDB)
                rcvMusica.adapter = miAdaptador
            }
        }

        //Programar el boton

        btnAgregar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                //1- crear un objeto de la clase conexiòm

                val objConexion = claseConexion().cadenaDeConexion()

                //2- crear una variable que contenga un PrepareStatement

                val addMusica = objConexion?.prepareStatement("insert into Musica values(?,?,?)")!!
                addMusica.setString( 1,txtNombre.text.toString())
                addMusica.setInt( 2, txtDuracion.text.toString().toInt())
                addMusica.setString(3, txtAutor.text.toString())
                addMusica.executeUpdate()
            }
        }
    }
}