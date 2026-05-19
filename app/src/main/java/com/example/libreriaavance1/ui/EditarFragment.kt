package com.example.libreriaavance1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.libreriaavance1.R
import com.example.libreriaavance1.model.Producto
import com.example.libreriaavance1.viewmodel.ProductoViewModel
import com.google.android.material.textfield.TextInputEditText

class EditarFragment : Fragment() {

    private val viewModel: ProductoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_editar, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Leer el ID del producto pasado por Bundle
        val productoId = arguments?.getInt("productoId") ?: return

        val etNombre = view.findViewById<TextInputEditText>(R.id.etNombreEditar)
        val spinnerCategoria = view.findViewById<Spinner>(R.id.spinnerCategoriaEditar)
        val etCantidad = view.findViewById<TextInputEditText>(R.id.etCantidadEditar)
        val etPrecio = view.findViewById<TextInputEditText>(R.id.etPrecioEditar)
        val btnActualizar = view.findViewById<Button>(R.id.btnActualizar)
        val btnEliminar = view.findViewById<Button>(R.id.btnEliminarEditar)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarEditar)

        val categorias = listOf(
            "Cuadernos", "Lapiceros", "Cartulinas", "Folders",
            "Borradores", "Reglas", "Tijeras", "Pegamento", "Otros"
        )
        val spinnerAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, categorias
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = spinnerAdapter

        // Cargar datos del producto
        val producto = viewModel.getProductoById(productoId)
        producto?.let {
            etNombre.setText(it.nombre)
            etCantidad.setText(it.cantidad.toString())
            etPrecio.setText(it.precio.toString())
            val catIndex = categorias.indexOf(it.categoria)
            if (catIndex >= 0) spinnerCategoria.setSelection(catIndex)
        }

        btnActualizar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val categoria = spinnerCategoria.selectedItem.toString()
            val cantidadStr = etCantidad.text.toString().trim()
            val precioStr = etPrecio.text.toString().trim()

            if (nombre.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cantidad = cantidadStr.toIntOrNull()
            val precio = precioStr.toDoubleOrNull()

            if (cantidad == null || precio == null || cantidad < 0 || precio < 0) {
                Toast.makeText(requireContext(), "Valores inválidos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.editarProducto(
                Producto(id = productoId, nombre = nombre, categoria = categoria,
                         cantidad = cantidad, precio = precio)
            )
            Toast.makeText(requireContext(), "Producto actualizado ✓", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        btnEliminar.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Eliminar producto")
                .setMessage("¿Estás seguro de eliminar este producto?")
                .setPositiveButton("Sí, eliminar") { _, _ ->
                    viewModel.eliminarProducto(productoId)
                    Toast.makeText(requireContext(), "Producto eliminado", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_editar_to_lista)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        btnCancelar.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
