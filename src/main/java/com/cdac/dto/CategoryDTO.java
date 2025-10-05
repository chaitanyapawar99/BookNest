package com.cdac.dto;

import lombok.Data;

@Data

public class CategoryDTO extends BaseDTO
{
	private Long id;
    private String name;
    private String description;
}
