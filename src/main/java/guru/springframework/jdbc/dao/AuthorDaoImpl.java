package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * Created by jt on 8/22/21.
 */
@Component
public class AuthorDaoImpl implements AuthorDao {
    private final JdbcTemplate template;

    public AuthorDaoImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Author getById(Long id) {
       return template.queryForObject("SELECT * FROM author WHERE id = ?",getRowMapper(),id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        return template.queryForObject("SELECT * FROM author WHERE first_name = ? and last_name = ?"
                ,getRowMapper(),firstName,lastName);
    }

    @Override
    public Author saveNewAuthor(Author author) {
        template.update("INSERT INTO author (first_name, last_name) VALUES (?, ?)",
                author.getFirstName(), author.getLastName());
        Long savedId = template.queryForObject("SELECT LAST_INSERT_ID()",Long.class);
        return this.getById(savedId);
    }

    @Override
    public Author updateAuthor(Author author) {
        template.update("UPDATE author SET first_name = ?, last_name = ? WHERE id = ?",
                author.getFirstName(),author.getLastName(),author.getId());
        return this.getById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        template.update("DELETE FROM author WHERE id = ?", id);
    }

    private RowMapper<Author> getRowMapper(){
        return new AuthorMapper();
    }
}
