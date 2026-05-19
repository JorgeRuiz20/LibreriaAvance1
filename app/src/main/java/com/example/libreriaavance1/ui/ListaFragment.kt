package com.example.libreriaavance1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libreriaavance1.R
import com.example.libreriaavance1.adapter.ProductoAdapter
import com.example.libreriaavance1.viewmodel.ProductoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaFragment : Fragment() {

    private val viewModel: ProductoViewModel by activityViewModels()
    private lateinit var adapter: ProductoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_lista, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        val fab = view.findViewById<FloatingActionButton>(R.id.fabAgregar)

        adapter = ProductoAdapter(
            lista = emptyList(),
            onEditar = { producto ->
                findNavController().navigate(
                    R.id.action_lista_to_editar,
                    bundleOf("productoId" to producto.id)
                )
            },
            onEliminar = { producto ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar producto")
                    .setMessage("¿Deseas eliminar '${producto.nombre}'?")
                    .setPositiveButton("Sí") { _, _ -> viewModel.eliminarProducto(producto.id) }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.productos.observe(viewLifecycleOwner) { lista ->
            adapter.actualizarLista(lista)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val resultado = viewModel.buscarPorNombre(newText ?: "")
                adapter.actualizarLista(resultado)
                return true
            }
        })

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_lista_to_agregar)
        }
    }
}
