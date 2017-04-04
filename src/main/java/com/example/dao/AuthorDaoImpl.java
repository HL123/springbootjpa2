package com.example.dao;

import com.example.domain.Author;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by huanglei on 17/4/4.
 */
@Repository
public class AuthorDaoImpl implements AuthorDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Author> findAll() {
        return this.entityManager.createQuery("select t from Author t ",Author.class).getResultList();
    }

    @Override
    public Author findAuthor(Long id) {
        return this.entityManager.createQuery("select t from Author t where id = ?", Author.class).setParameter(1,id).getSingleResult();
    }
}
