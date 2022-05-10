package vb.shop.model.exception;

public class ProductOutOfStockException extends RuntimeException {
    public ProductOutOfStockException(String p_name){
        super(String.format("Product %s is out of stock",p_name));
    }
}
