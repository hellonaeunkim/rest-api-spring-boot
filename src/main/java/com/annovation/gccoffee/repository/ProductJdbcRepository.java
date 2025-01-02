package com.annovation.gccoffee.repository;

import com.annovation.gccoffee.model.Category;
import com.annovation.gccoffee.model.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;

import static com.annovation.gccoffee.JdbcUtils.toLocalDateTime;
import static com.annovation.gccoffee.JdbcUtils.toUUID;

public class ProductJdbcRepository implements ProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM products", productRowMapper);
    }

    @Override
    public Product insert(Product product) {
        var update = jdbcTemplate.update("INSERT INTO products(product_id, product_name, category, price, description, created_at, update_at)" +
                " VALUES(UUID_TO_BIN(:productId), :productName,:category,:price,:description,:createdAt,:updateAt)", toParamMap(product));
        // DB에 데이터가 추가되지 않거나 문제가 생기면 0을 반환함
        if (update != 1) {
            throw new RuntimeException("Nothing was inserted");
        }
        return product;
    }

    @Override
    public Product update(Product product) {
        return null;
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        return Optional.empty();
    }

    @Override
    public Optional<Product> findByName(String productName) {
        return Optional.empty();
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return List.of();
    }

    @Override
    public void deleteAll() {

    }

    // ResultSet 각 행을 Product 객체로 변환
    private static final RowMapper<Product> productRowMapper = (resultSet, i) -> {
        var productId = toUUID(resultSet.getBytes("product_id"));
        var productName = resultSet.getString("product_name");
        var category = Category.valueOf(resultSet.getString("category"));
        var price = resultSet.getLong("price");
        var description = resultSet.getString("description");
        var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        var updateAt = toLocalDateTime(resultSet.getTimestamp("update_at"));
        return new Product(productId, productName, category, price, description, createdAt, updateAt);
    };

    private Map<String, Object> toParamMap(Product product) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("productId", product.getProductId().toString().getBytes());
        paramMap.put("productName", product.getProductName());
        paramMap.put("category", product.getCategory().toString());
        paramMap.put("price", product.getPrice());
        paramMap.put("description", product.getDescription());
        paramMap.put("createdAt", product.getCreatedAt());
        paramMap.put("updateAt", product.getUpdateAt());

        return paramMap;
    }
}
