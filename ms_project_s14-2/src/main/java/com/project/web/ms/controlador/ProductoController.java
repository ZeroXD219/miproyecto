package com.project.web.ms.controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.web.ms.modelo.Categoria;
import com.project.web.ms.modelo.Producto;
import com.project.web.ms.servicio.ProductoServicio;

@RestController
@RequestMapping(value = "/producto")
public class ProductoController {

	@Autowired
	ProductoServicio productoServicio;
	
	//@GetMapping("/")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<List<Producto>> ListarProducto(@RequestParam(name = "categoriaId",
	required = false) Long categoriaId) {
		
		List<Producto> productos = new ArrayList<>();
		
		if(categoriaId == null) {
			productos = productoServicio.ListAllProduct();
			if(productos.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
		}else {
			productos = productoServicio.findByCategoria(Categoria.builder()
					.idcategoria(categoriaId).build());
			if(productos.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
		}
		
		return ResponseEntity.ok(productos);
	}
	
	@RequestMapping(value = "/{id}",method = RequestMethod.GET)
	public ResponseEntity<Producto> getProducto(@PathVariable("id") Long id){
		
		Producto producto = productoServicio.getProduct(id);
		if(producto == null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(producto);
	}
	
	//@RequestMapping(value = "/",method = RequestMethod.POST)
	@PostMapping
	public ResponseEntity<Producto> CrearProducto(@Valid @RequestBody Producto producto,BindingResult result){
		
		if(result.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,this.formatMessage(result));
		}

		Producto productoCreado = productoServicio.createProducto(producto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(productoCreado);
	}
	
	@RequestMapping(value = "/{id}",method = RequestMethod.PUT)
	public ResponseEntity<Producto> actualizarProducto(@PathVariable("id") Long id,
			@RequestBody Producto producto){
		
		producto.setIdproducto(id);
		Producto productoEncontrado = productoServicio.updateProduct(producto);
		
		if(productoEncontrado == null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(productoEncontrado);
	}
	
	@RequestMapping(value = "/eliminar/{id}",method = RequestMethod.PUT)
	public ResponseEntity<Producto> eliminarProducto(@PathVariable("id") Long id,
			@RequestBody Producto producto){
		
		producto.setIdproducto(id);
		Producto productoEliminado = productoServicio.updateProduct(producto);
		
		if(productoEliminado == null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(productoEliminado);
	}
	
    private String formatMessage( BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String>  error =  new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;

                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }	
}
