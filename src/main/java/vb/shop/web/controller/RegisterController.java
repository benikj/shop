package vb.shop.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vb.shop.service.AuthService;

import java.util.List;

@Controller
@RequestMapping("/register")
public class RegisterController {

        private final AuthService authService;

    public RegisterController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String getRegisterPage(@RequestParam(required=false) String error, Model model){
        if(error!=null && !error.isEmpty()){
            model.addAttribute("hasError",true);
            model.addAttribute("error",error);
        }

        return "register";
    }

    @PostMapping
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String repeatedPassword
                              ){

        try{
            this.authService.register(username, password, repeatedPassword);
            return "redirect:/login?info=Successful registration!";
        }catch (RuntimeException e){
            return "redirect:/register?error= " + e.getLocalizedMessage();
        }

    }
}
