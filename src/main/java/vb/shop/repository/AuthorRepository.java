package vb.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vb.shop.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
