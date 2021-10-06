package com.project.web.ms.servicio;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.web.ms.modelo.Categoria;
import com.project.web.ms.modelo.Producto;
import com.project.web.ms.repositorio.ProductoRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServicioImplementa implements ProductoServicio {

	public final ProductoRepositorio productoRepositorio;
	
	@Override
	public List<Producto> ListAllProduct() {
		return productoRepositorio.findAll();
	}

	@Override
	public Producto getProduct(Long id) {
		return productoRepositorio.findById(id).orElse(null);
	}

	@Override
	public Producto createProducto(Producto producto) {
		producto.setStatus("CREADO");
		producto.setCreateAt(new Date());
		return productoRepositorio.save(producto);
	}

	@Override
	public Producto updateProduct(Producto producto) {
		Producto productoUpdate = getProduct(producto.getIdproducto());
		
		if(productoUpdate == null) {
			return null;
		}
		
		productoUpdate.setName(producto.getName());
		productoUpdate.setDescription(producto.getDescription());
		productoUpdate.setCategoria(producto.getCategoria());
		productoUpdate.setPrice(producto.getPrice());
		
		return productoRepositorio.save(productoUpdate);
	}

	@Override
	public Producto deleteProducto(Long id) {
		Producto productoDelete = getProduct(id);
		
		if(productoDelete == null) {
			return null;
		}
		
		productoDelete.setStatus("ELIMINADO");
		
		return productoRepositorio.save(productoDelete);
	}

	@Override
	public List<Producto> findByCategoria(Categoria categoria) {
		return productoRepositorio.findByCategoria(categoria);
	}

	@Override
	public Producto updateStock(Long id, Double quantity) {
		Producto productoUpdateStock = getProduct(id);
		
		if(productoUpdateStock == null) {
			return null;
		}
		
		Double stock = productoUpdateStock.getStock() + quantity;
		
		productoUpdateStock.setStock(stock);
		
		return productoRepositorio.save(productoUpdateStock);
	}

}
