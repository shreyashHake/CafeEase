package com.cafe.serviceIml;

import com.cafe.constants.CafeConstants;
import com.cafe.dao.CategoryDao;
import com.cafe.model.Category;
import com.cafe.service.CategoryService;
import com.cafe.util.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryDao categoryDao;

    @Override
    public ResponseEntity<String> addCategory(Map<String, String> requestMap) {
        try {
            categoryDao.save(getFormatedCategory(requestMap));
            return CafeUtils.getResoponseEntity("Category added successfully", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResoponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Category getFormatedCategory(Map<String, String> requesstMap) {
        Category category = new Category();
        category.setName(requesstMap.get("name"));
        return category;
    }
}
