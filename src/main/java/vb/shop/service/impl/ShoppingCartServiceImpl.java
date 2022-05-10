package vb.shop.service.impl;

import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.stereotype.Service;
import vb.shop.model.Product;
import vb.shop.model.ShoppingCart;
import vb.shop.model.User;
import vb.shop.model.dto.ChargeRequest;
import vb.shop.model.enumerations.CartStatus;
import vb.shop.model.exception.*;
import vb.shop.repository.ShoppingCartRepository;
import vb.shop.service.PaymentService;
import vb.shop.service.ProductService;
import vb.shop.service.ShoppingCartService;
import vb.shop.service.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class    ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final ProductService productService;
    private final PaymentService paymentService;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, UserService userService, ProductService productService,PaymentService paymentService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userService = userService;
        this.productService = productService;
        this.paymentService = paymentService;
    }

    @Override
    public ShoppingCart findActiveShoppingCartByUsername(String userId) {
            return this.shoppingCartRepository.findByUserUsernameAndCartStatus(userId, CartStatus.CREATE).orElseThrow(ShoppingCartNotFound::new);
    }

    @Override
    public List<ShoppingCart> findAllByUsername(String userId) {
        return this.shoppingCartRepository.findAllByUserUsername(userId);
    }

    @Override
    public ShoppingCart createNewShoppingCart(String userId) {
        User user = this.userService.findById(userId);
        if (this.shoppingCartRepository.existsByUserUsernameAndCartStatus(user.getUsername(), CartStatus.CREATE)){
            throw new ShoppingCartIsAlreadyCreated();
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart addProductToShoppingCart(String userId, Long productId) {
        ShoppingCart shoppingCart =this.getActiveShoppingCart(userId);
        Product product = this.productService.findById(productId);
        for (Product p : shoppingCart.getProducts()){
            if (p.getId().equals(productId)){
                throw new ProductIsAlreadyInShoppingCartException();
            }
        }
        shoppingCart.getProducts().add(product);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart removeProductFromShoppingCart(String userId, Long productId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(userId);
        shoppingCart.setProducts(
                shoppingCart.getProducts()
                .stream()
                .filter(product -> !product.getId().equals(productId))
                .collect(Collectors.toList())
        );
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart getActiveShoppingCart(String userId) {
        return this.shoppingCartRepository
                .findByUserUsernameAndCartStatus(userId,CartStatus.CREATE)
                .orElseGet(()-> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    User user =this.userService.findById(userId);
                    shoppingCart.setUser(user);
                    return this.shoppingCartRepository.save(shoppingCart);
                });
    }


    @Override
    public ShoppingCart cancelActiveShoppingCart(String userId) {
        ShoppingCart shoppingCart = this.shoppingCartRepository
                .findByUserUsernameAndCartStatus(userId,CartStatus.CREATE)
                .orElseThrow(ShoppingCartNotFound::new);
        shoppingCart.setCartStatus(CartStatus.CANCELED);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart checkoutShoppingCart(String userId, ChargeRequest request) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserUsernameAndCartStatus(userId,CartStatus.CREATE)
                .orElseThrow(()->new ShoppingCartIsNotActiveException(userId));

        List<Product> products = shoppingCart.getProducts();
        float price = 0;
        for (Product p: products){
            if (p.getQuantity()<=0){
                throw  new ProductOutOfStockException(p.getName());
            }
            p.setQuantity(p.getQuantity()-1);
            price+=p.getPrice();
        }
        Charge charge=null;
        try{
            charge = this.paymentService.pay(request);
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException |APIException | CardException e) {
            throw new TransactionFailedException(userId, e.getMessage());
        }


        shoppingCart.setProducts(products);
        shoppingCart.setCartStatus(CartStatus.FINISHED);

        return this.shoppingCartRepository.save(shoppingCart);
    }
}
