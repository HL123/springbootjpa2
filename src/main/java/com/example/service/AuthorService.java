package com.example.service;

import com.example.dao.AuthorDao;
import com.example.domain.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huanglei on 17/4/4.
 */
@Service
public class AuthorService {

    @Autowired
    private AuthorDao authorDao;

    public List<Author> findAll(){
        return this.authorDao.findAll();
    }

    public Author findAuthor(Long id){
        return this.authorDao.findAuthor(id);
    }
}
