package vb.shop.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vb.shop.model.Manufacturer;
import vb.shop.model.Product;
import vb.shop.repository.AuthorRepository;
import vb.shop.service.ManufacturerService;
import vb.shop.service.ProductService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ProductController {

        private final ProductService productService;
        private final ManufacturerService manufacturerService;
        private final AuthorRepository authorRepository;

    public ProductController(ProductService productService, ManufacturerService manufacturerService, AuthorRepository authorRepository) {
        this.productService = productService;
        this.manufacturerService = manufacturerService;
        this.authorRepository = authorRepository;
    }


    @GetMapping("/products")
    public String getProductPage(Model model){
        // mi se prikazuvaat produktite
        List<Product> products = this.productService.findAll();
        model.addAttribute("products",products);
        return "products";
    }

    @GetMapping("/products/add-new")
    public String addNewProductPage(Model model){
        List<Manufacturer> manufacturers = this.manufacturerService.findAll();
        model.addAttribute("manufacturers",manufacturers);
        model.addAttribute("authors",this.authorRepository.findAll());
        model.addAttribute("product",new Product());
        return "add-product";
    }

    @GetMapping("/products/{id}/edit")
    public String editProductPage(Model model, @PathVariable Long id){
      try {
          Product product = this.productService.findById(id);
          List<Manufacturer> manufacturers = this.manufacturerService.findAll();
          model.addAttribute("product",product);
          model.addAttribute("manufacturers",manufacturers);
          model.addAttribute("authors",this.authorRepository.findAll());
          return "add-product";
      }catch (Exception ex){
          return "redirect:/products?error=" + ex.getMessage();
      }
    }

    @PostMapping("/products")
    public String saveProduct(@Valid Product product,
                              BindingResult bindingResult,
                              @RequestParam MultipartFile image,
                              Model model){
        if (bindingResult.hasErrors()){
            List<Manufacturer> manufacturers = this.manufacturerService.findAll();
            model.addAttribute("manufacturers",manufacturers);
            model.addAttribute("authors",this.authorRepository.findAll());
            return "add-product";
        }try{
            this.productService.saveProduct(product,image);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "redirect:/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id){
        this.productService.deleteById(id);
        return "redirect:/products";
    }







}
