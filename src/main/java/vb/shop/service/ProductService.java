package vb.shop.service;

import org.springframework.web.multipart.MultipartFile;
import vb.shop.model.Manufacturer;
import vb.shop.model.Product;

import java.io.IOException;

import java.util.List;


public interface ProductService {
    List<Product> findAll();
    List<Product> findAllByManufacturerId(Long manufacturerId);
    List<Product> findAllSortedByPrice(boolean asc);
    Product findById(Long id);
    Product saveProduct(String name,Float price, Integer quantity, Long manufacturerId);
    Product saveProduct(Product product, MultipartFile multipartFile) throws IOException;
    Product updateProduct(Long id, Product product, MultipartFile multipartFile) throws IOException;
    void deleteById(Long id);

}
