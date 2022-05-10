package vb.shop.service.impl;

import org.springframework.stereotype.Service;
import vb.shop.model.Author;
import vb.shop.model.exception.AuthorDoesNotExistException;
import vb.shop.repository.AuthorRepository;
import vb.shop.service.AuthorService;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> findAll() {
        return this.authorRepository.findAll();
    }

    @Override
    public Author findById(Long id) {
        return this.authorRepository.findById(id).orElseThrow(()->new AuthorDoesNotExistException());
    }

    @Override
    public Author save(Author author) {
        return this.authorRepository.save(author);
    }

    @Override
    public Author update(Long id, Author author) {
        Author author1 = this.findById(id);
        author1.setName(author.getName());
        return this.authorRepository.save(author1);
    }

    @Override
    public void deleteById(Long id) {
        this.authorRepository.deleteById(id);
    }
}
