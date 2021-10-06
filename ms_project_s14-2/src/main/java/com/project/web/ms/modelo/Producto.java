package com.project.web.ms.modelo;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Producto")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Producto {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idproducto;
	
	@NotEmpty(message = "El nombre no debe ser vacío")
	private String name;
	private String description;
	
	@Positive(message = "El stock debe ser mayor que cero")
	private Double stock;
	private Double price;
	private String status;

	@Column(name="create_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createAt;
	
	@NotNull(message = "La categoria no puede ser vacia")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idcategoria")
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Categoria categoria;
	
	
}
