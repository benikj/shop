package vb.shop.model.exception;

public class ManufacturerIdException  extends RuntimeException{
    public ManufacturerIdException(Long id){
        super(String.format("Manufacturer with id %d is not found!", id));
    }
}
