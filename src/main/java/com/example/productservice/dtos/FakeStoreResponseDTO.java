package com.example.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreResponseDTO {
    private Long id;
    private String title;
    private String category;
    private String description;
    private String image;
}
