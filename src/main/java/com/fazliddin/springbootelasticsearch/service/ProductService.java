package com.fazliddin.springbootelasticsearch.service;

import com.fazliddin.springbootelasticsearch.model.Product;

import java.util.List;

public interface ProductService {

    public void saveAll(final List<Product> products);

    public void save(final Product product);

}
