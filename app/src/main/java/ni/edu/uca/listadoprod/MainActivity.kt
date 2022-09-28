package ni.edu.uca.listadoprod

import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ni.edu.uca.listadoprod.dataadapter.ProductoAdapter
import ni.edu.uca.listadoprod.databinding.ActivityMainBinding
import ni.edu.uca.listadoprod.dataclass.Producto

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var listaProd = ArrayList<Producto>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        iniciar()
    }

    private fun limpiar() {
        with(binding) {
            etID.setText("")
            etNombreProd.setText("")
            etPrecio.setText("")
            etID.requestFocus()

        }
    }

    private fun agregarProd() {
        with(binding) {
            try {
                val id: Int = etID.text.toString().toInt()
                val nombre: String = etNombreProd.text.toString()
                val precio: Double = etPrecio.text.toString().toDouble()
                val prod = Producto(id, nombre, precio)
                listaProd.add(prod)
            } catch (ex: Exception) {
                Toast.makeText(
                    this@MainActivity, "Error: $ex",
                    Toast.LENGTH_LONG
                ).show()
            }
            rcvLista.layoutManager = LinearLayoutManager(this@MainActivity)
            rcvLista.adapter = ProductoAdapter(listaProd, { producto -> onItemSelected(producto) },
                { position -> onDeleteItem(position) }, { position -> onUpdateItem(position) })
            limpiar()
        }
    }

    private fun onItemSelected(producto: Producto) {
        with(binding) {
            etID.text = producto.id.toString().toEditable()
            etNombreProd.text = producto.nombre.toEditable()
            etPrecio.text = producto.precio.toString().toEditable()
        }
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun onUpdateItem(position: Int) {

        with(binding) {
            try {
                val id: Int = etID.text.toString().toInt()
                val nombre: String = etNombreProd.text.toString()
                val precio: Double = etPrecio.text.toString().toDouble()
                val prod = Producto(id, nombre, precio)
                listaProd[position] = prod
                rcvLista.adapter?.notifyItemChanged(position)
                Toast.makeText(this@MainActivity, "Producto Editado Correctamente ", Toast.LENGTH_LONG).show()


            } catch (ex: Exception) {
                Toast.makeText(this@MainActivity, "Error: $ex ", Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun onDeleteItem(position: Int) {

        try {

            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Eliminar Producto")
            dialog.setMessage("Esta Accion Eliminara el Producto de forma PERMANENTE")
            dialog.setPositiveButton("SI") { _, _ ->
                with(binding) {
                    listaProd.removeAt(position)
                    rcvLista.adapter?.notifyItemRemoved(position)
                }
                Toast.makeText(this, "Producto Eliminado Correctamente", Toast.LENGTH_SHORT).show()

            }
            dialog.setNegativeButton("NO") { _, _ ->
                Toast.makeText(this, "La Eliminación fue Cancelada", Toast.LENGTH_SHORT).show()

            }
            dialog.show()
        } catch (ex: Exception) {
            Toast.makeText(
                this@MainActivity, "Error: $ex ",
                Toast.LENGTH_LONG
            ).show()

        }

    }

    private fun iniciar() {
        binding.btnAgregar.setOnClickListener { agregarProd() }
        binding.btnLimpiar.setOnClickListener { limpiar() }

    }
}

