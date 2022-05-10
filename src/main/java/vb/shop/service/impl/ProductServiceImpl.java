package vb.shop.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vb.shop.model.Manufacturer;
import vb.shop.model.Product;
import vb.shop.model.exception.ManufacturerIdException;
import vb.shop.model.exception.ProductIsAlreadyInShoppingCartException;
import vb.shop.model.exception.ProductNotFoundException;
import vb.shop.repository.ManufacturerRepository;
import vb.shop.repository.ProductRepository;
import vb.shop.service.ManufacturerService;
import vb.shop.service.ProductService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ManufacturerService manufacturerService;

    public ProductServiceImpl(ProductRepository productRepository, ManufacturerService manufacturerService) {
        this.productRepository = productRepository;
        this.manufacturerService = manufacturerService;

    }

    @Override
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    @Override
    public List<Product> findAllByManufacturerId(Long manufacturerId) {
        return this.productRepository.findAllByManufacturerId(manufacturerId);
    }

    @Override
    public List<Product> findAllSortedByPrice(boolean asc) {
        if (asc){
            return this.productRepository.findAllByOrderByPriceAsc();
        }

        return this.productRepository.findAllByOrderByPriceDesc();
    }

    @Override
    public Product findById(Long id) {
        return this.productRepository.findById(id).orElseThrow(()->new ProductNotFoundException(id));
    }

    @Override
    public Product saveProduct(String name, Float price, Integer quantity, Long manufacturerId) {
        Manufacturer manufacturer = this.manufacturerService.findById(manufacturerId);
        Product product = new Product(null,name,price,quantity,manufacturer);
        return this.productRepository.save(product);
    }

    @Override
    public Product saveProduct(Product product, MultipartFile multipartFile) throws IOException {
        Manufacturer manufacturer = this.manufacturerService.findById(product.getManufacturer().getId());
        product.setManufacturer(manufacturer);
        return getProduct(product, multipartFile);
    }

    private Product getProduct(Product product, MultipartFile multipartFile) throws IOException {
        if (multipartFile!=null && !multipartFile.getName().isEmpty()){
            byte[] bytes = multipartFile.getBytes();
            String base64Image = String.format("data:%s;base64,%s",multipartFile.getContentType(), Base64.getEncoder().encodeToString(bytes));
            product.setImage64base(base64Image);
        }
        return this.productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product, MultipartFile multipartFile) throws IOException {
        Product product1 = this.productRepository.findById(id).orElseThrow(()->new ProductNotFoundException(id));
        Manufacturer manufacturer = this.manufacturerService.findById(product.getManufacturer().getId());
        product1.setManufacturer(manufacturer);
        product1.setPrice(product.getPrice());
        product1.setQuantity(product.getQuantity());
        return getProduct(product, multipartFile);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Product product = this.findById(id);
        if (product.getShoppingCarts().size() > 0) {
            //avoid deleting product that is already in shopping cart!
            throw new ProductIsAlreadyInShoppingCartException();
        }
        this.productRepository.deleteById(id);
    }
}
