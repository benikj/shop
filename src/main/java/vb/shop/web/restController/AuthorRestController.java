package vb.shop.web.restController;

import org.springframework.web.bind.annotation.*;
import vb.shop.model.Author;
import vb.shop.service.AuthorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorRestController {

    private final AuthorService authorService;

    public AuthorRestController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<Author> findAll(){
        return this.authorService.findAll();
    }

    @GetMapping("/{id}")
    public Author findById(@PathVariable Long id){
        return this.authorService.findById(id);
    }

    @PostMapping
    public Author save(@RequestParam String name){
        Author author = new Author();
        author.setName(name);
        return this.authorService.save(author);
    }

    @PutMapping("/{id}")
   public Author update(@PathVariable Long id,@Valid Author author){
        return this.authorService.update(id, author);
    }

    @DeleteMapping("/{id}")
   public void deleteById(@PathVariable Long id){
        this.authorService.deleteById(id);
    }
}
