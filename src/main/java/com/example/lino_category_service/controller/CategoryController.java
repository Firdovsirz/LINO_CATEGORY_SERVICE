package com.example.lino_category_service.controller;
import com.example.lino_category_service.DTO.CategoryRepository;
import com.example.lino_category_service.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Object getCategoryById(@PathVariable Integer id) {
        try {
            Optional<Category> existsCategory = categoryRepository.findById(Long.valueOf(id));
            if (existsCategory.isPresent()) {
                return existsCategory;
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError();
        }
    }

    @PostMapping()
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        try {
            Optional<Category> existsCategory = categoryRepository.findById(Long.valueOf(category.getId()));
            if (existsCategory.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Category with ID " + category.getId() + " already exists.");
            }

            Category savedCategory = categoryRepository.save(category);
            return ResponseEntity.ok(savedCategory);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody Category updatedCategory) {
        try {
            Optional<Category> existsCategory = categoryRepository.findById(Long.valueOf(id));
            if (existsCategory.isPresent()) {
                Category category = existsCategory.get();
                category.setName(updatedCategory.getName());
                category.setDescription(updatedCategory.getDescription());
                category.setType(updatedCategory.getType());

                Category savedCategory = categoryRepository.save(category);
                return ResponseEntity.ok(savedCategory);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Optional<Category>> deleteCategory(@PathVariable Integer id) {
        try {
            Optional<Category> existsCategory = categoryRepository.findById(Long.valueOf(id));
            if (existsCategory.isPresent()) {
                categoryRepository.deleteById(Long.valueOf(id));
                return ResponseEntity.ok(existsCategory);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}