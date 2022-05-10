package vb.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vb.shop.model.ShoppingCart;
import vb.shop.model.enumerations.CartStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    List<ShoppingCart> findAllByUserUsername(String username);
    Optional<ShoppingCart> findByUserUsernameAndCartStatus(String username,CartStatus status);
    boolean existsByUserUsernameAndCartStatus(String username,CartStatus status);
}
