package vb.shop.service;

import vb.shop.model.Author;
import vb.shop.model.Manufacturer;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
    Author findById(Long id);
    Author save(Author author);
    Author update(Long id, Author author);
    void deleteById(Long id);
}
