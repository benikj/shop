package vb.shop.service;

import vb.shop.model.User;

public interface AuthService {
   User getCurrentUser();
   String getCurrentUserId();
   User login(String username,String password);
   User register(String username,String password, String repeatedPassword);

}
