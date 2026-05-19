package com.example.libreriaavance1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.libreriaavance1.R
import com.example.libreriaavance1.viewmodel.ProductoViewModel

class HomeFragment : Fragment() {

    private val viewModel: ProductoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTotalProductos = view.findViewById<TextView>(R.id.tvTotalProductos)
        val tvTotalItems = view.findViewById<TextView>(R.id.tvTotalItems)
        val btnVerInventario = view.findViewById<Button>(R.id.btnVerInventario)
        val btnAgregar = view.findViewById<Button>(R.id.btnAgregarHome)

        viewModel.productos.observe(viewLifecycleOwner) { lista ->
            tvTotalProductos.text = "${lista.size}"
            tvTotalItems.text = "${viewModel.getTotalProductos()}"
        }

        btnVerInventario.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_lista)
        }

        btnAgregar.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_agregar)
        }
    }
}
