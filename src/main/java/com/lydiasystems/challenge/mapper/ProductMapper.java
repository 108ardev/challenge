package com.lydiasystems.challenge.mapper;

import com.lydiasystems.challenge.model.dto.ProductDto;
import com.lydiasystems.challenge.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    Product toEntity(ProductDto dto);

    ProductDto toDto(Product entity);

    List<ProductDto> toDto(List<Product> entities);

    void update (ProductDto dto, @MappingTarget Product entity);
}
