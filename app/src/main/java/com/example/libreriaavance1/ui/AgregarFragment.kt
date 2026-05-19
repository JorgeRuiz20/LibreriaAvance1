package com.example.libreriaavance1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.libreriaavance1.R
import com.example.libreriaavance1.model.Producto
import com.example.libreriaavance1.viewmodel.ProductoViewModel
import com.google.android.material.textfield.TextInputEditText

class AgregarFragment : Fragment() {

    private val viewModel: ProductoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_agregar, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etNombre = view.findViewById<TextInputEditText>(R.id.etNombre)
        val spinnerCategoria = view.findViewById<Spinner>(R.id.spinnerCategoria)
        val etCantidad = view.findViewById<TextInputEditText>(R.id.etCantidad)
        val etPrecio = view.findViewById<TextInputEditText>(R.id.etPrecio)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelar)

        val categorias = listOf(
            "Cuadernos", "Lapiceros", "Cartulinas", "Folders",
            "Borradores", "Reglas", "Tijeras", "Pegamento", "Otros"
        )
        val spinnerAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, categorias
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = spinnerAdapter

        btnGuardar.setOnClickListener {
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
                Toast.makeText(requireContext(), "Cantidad y precio deben ser números válidos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevoProducto = Producto(
                id = 0, // ViewModel asigna el ID
                nombre = nombre,
                categoria = categoria,
                cantidad = cantidad,
                precio = precio
            )

            viewModel.agregarProducto(nuevoProducto)
            Toast.makeText(requireContext(), "¡Producto agregado!", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        btnCancelar.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
