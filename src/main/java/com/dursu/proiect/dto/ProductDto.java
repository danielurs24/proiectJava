package com.dursu.proiect.dto;

import com.dursu.proiect.entity.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
	
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private Integer stock;
	@NotNull
	private String description;
	@NotNull
	private String categoryName;
}
