package com.example.libreriaavance1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.libreriaavance1.model.Producto

class ProductoViewModel : ViewModel() {

    private val _productos = MutableLiveData<MutableList<Producto>>()
    val productos: LiveData<MutableList<Producto>> get() = _productos

    private var nextId = 5 // ID autoincremental

    init {
        // Datos de ejemplo para arrancar la app
        _productos.value = mutableListOf(
            Producto(1, "Cuaderno A4 Rayado", "Cuadernos", 25, 2.50),
            Producto(2, "Lapicero Azul BIC", "Lapiceros", 80, 0.80),
            Producto(3, "Cartulina Blanca", "Cartulinas", 40, 0.50),
            Producto(4, "Folder Manila A4", "Folders", 30, 0.30)
        )
    }

    fun agregarProducto(producto: Producto) {
        val lista = _productos.value ?: mutableListOf()
        val nuevo = producto.copy(id = nextId++)
        lista.add(nuevo)
        _productos.value = lista
    }

    fun editarProducto(productoActualizado: Producto) {
        val lista = _productos.value ?: return
        val index = lista.indexOfFirst { it.id == productoActualizado.id }
        if (index != -1) {
            lista[index] = productoActualizado
            _productos.value = lista
        }
    }

    fun eliminarProducto(id: Int) {
        val lista = _productos.value ?: return
        lista.removeAll { it.id == id }
        _productos.value = lista
    }

    fun buscarPorNombre(query: String): List<Producto> {
        return _productos.value?.filter {
            it.nombre.contains(query, ignoreCase = true)
        } ?: emptyList()
    }

    fun getTotalProductos(): Int = _productos.value?.sumOf { it.cantidad } ?: 0

    fun getProductoById(id: Int): Producto? = _productos.value?.find { it.id == id }
}
