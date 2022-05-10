package vb.shop.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vb.shop.model.Product;
import vb.shop.model.ShoppingCart;
import vb.shop.model.dto.ChargeRequest;
import vb.shop.service.AuthService;
import vb.shop.service.ShoppingCartService;

import javax.transaction.Transactional;

@Controller
@RequestMapping("/payments")
@Transactional
public class PaymentController{

    @Value("${STRIPE_P_KEY}")
    private String publicKey;
    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;

    public PaymentController(ShoppingCartService shoppingCartService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }

    @GetMapping("/charge")
    public String getCheckoutPage(Model model){
        try {
            ShoppingCart shoppingCart = this.shoppingCartService.findActiveShoppingCartByUsername(this.authService.getCurrentUserId());
            model.addAttribute("shoppingCart",shoppingCart);
            model.addAttribute("stripePublicKey",this.publicKey);
            model.addAttribute("currency", "eur");
            model.addAttribute("amount", (int) (shoppingCart.getProducts().stream().mapToDouble(Product::getPrice).sum() * 100));
            return "checkout";
        }catch (RuntimeException e){
            return "redirect:/products?error=" + e.getLocalizedMessage();
        }

    }

    @PostMapping("/charge")
    public String checkout(ChargeRequest chargeRequest,Model model){

        try {
            ShoppingCart shoppingCart = this.shoppingCartService.checkoutShoppingCart(this.authService.getCurrentUserId(), chargeRequest);
            return "redirect:/products?message=Successful Payment";
        } catch (RuntimeException ex) {
            return "redirect:/payments/charge?error=" + ex.getLocalizedMessage();
        }
    }

}
