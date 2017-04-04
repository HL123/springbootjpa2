package com.example.dao;

import com.example.domain.Author;

import java.util.List;

/**
 * Created by huanglei on 17/4/4.
 */
public interface AuthorDao {
    List<Author> findAll();
    Author findAuthor(Long id);
}
