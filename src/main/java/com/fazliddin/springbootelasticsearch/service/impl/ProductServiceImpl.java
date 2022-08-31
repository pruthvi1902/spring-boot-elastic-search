package com.fazliddin.springbootelasticsearch.service.impl;

import com.fazliddin.springbootelasticsearch.model.Product;
import com.fazliddin.springbootelasticsearch.repository.ProductRepository;
import com.fazliddin.springbootelasticsearch.service.ProductService;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCT_INDEX = "productindex";

    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private ProductRepository productRepository;


    @Override
    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }


    /*
     * Elastic Search
     */


    // bulkIndexing
        public List<String> createProductIndexBulk(List<Product> products) {

        List<IndexQuery> queries = products.stream()
                .map(product ->
                        new IndexQueryBuilder()
                                .withId(product.getId().toString())
                                .withObject(product).build())
                .collect(Collectors.toList());
        ;

        List<IndexedObjectInformation> returning =
                elasticsearchOperations.bulkIndex(queries, IndexCoordinates.of(PRODUCT_INDEX));

       return returning.stream().map(IndexedObjectInformation::toString).collect(Collectors.toList());
    }

    // indexing
    public String createProductIndex(Product product) {

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(product.getId().toString())
                .withObject(product).build();

        String documentId = elasticsearchOperations
                .index(indexQuery, IndexCoordinates.of(PRODUCT_INDEX));

        return documentId;
    }


    // native query
    public void findProductByName(String brandName) {

        QueryBuilder queryBuilder =
                QueryBuilders
                        .matchQuery("manufacturer", brandName);

        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();

        SearchHits<Product> productHits =
                elasticsearchOperations
                        .search(searchQuery,
                                Product.class,
                                IndexCoordinates.of(PRODUCT_INDEX));


    }

    // string query
    public void findByProductName(final String productName) {
        Query searchQuery = new StringQuery(
                "{\"match\":{\"name\":{\"query\":\""+ productName + "\"}}}\"");

        SearchHits<Product> products = elasticsearchOperations.search(
                searchQuery,
                Product.class,
                IndexCoordinates.of(PRODUCT_INDEX));
    }


}
