package com.dursu.proiect.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
	
	private Long id;
	@NotNull
	private Integer rating;
	@NotNull
	private String description;
	@NotNull
	private Long productId;
}
