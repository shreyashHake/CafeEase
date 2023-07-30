package com.cafe.serviceIml;

import com.cafe.JWT.JwtFilter;
import com.cafe.constants.CafeConstants;
import com.cafe.dao.ProductDao;
import com.cafe.model.Category;
import com.cafe.model.Product;
import com.cafe.service.ProductService;
import com.cafe.util.CafeUtils;
import com.cafe.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductDao productDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductMap(requestMap, false)) {
                    productDao.save(getProductFromMap(requestMap, false));
                    return CafeUtils.getResoponseEntity("Product Added Successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResoponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            }
            return CafeUtils.getResoponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResoponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductMap(requestMap, true)) {
                    Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
                    if (optional.isPresent()) {
                        Product product = getProductFromMap(requestMap, true);
                        product.setStatus(optional.get().getStatus());
                        productDao.save(product);
                        return CafeUtils.getResoponseEntity("Product updated successfully", HttpStatus.OK);
                    } else {
                        return CafeUtils.getResoponseEntity("Product does not exists", HttpStatus.OK);
                    }
                } else {
                    return CafeUtils.getResoponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResoponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResoponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")
                && requestMap.containsKey("prize")
                && requestMap.containsKey("description")) {
            if (requestMap.containsKey("id") && validateId) return true;
            else return !validateId;
        }
        return false;
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        Product product = new Product();

        if (isAdd) product.setId(Integer.parseInt(requestMap.get("id")));
        else product.setStatus("true");

        product.setName(requestMap.get("name"));
        product.setCategory(category);
        product.setPrize(requestMap.get("prize"));
        product.setDescription(requestMap.get("description"));

        return product;
    }
}