package com.example.lino_category_service.controller;
import com.example.lino_category_service.DTO.ApiResponse;
import com.example.lino_category_service.repository.CategoryRepository;
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
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            return ResponseEntity.ok(ApiResponse.success(categories));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "INTERNAL_SERVER_ERROR", "Failed to retrieve categories"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        try {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(category.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.not_found("Category with ID " + id + " not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "INTERNAL_SERVER_ERROR", "Failed to retrieve category"));
        }
    }

    @PostMapping()
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        try {
            // Validate required fields
            if (category.getName() == null || category.getName().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.badRequest("Category name is required"));
            }
            
            if (category.getType() != 0 && category.getType() != 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.badRequest("Category type must be 0 (course) or 1 (quiz)"));
            }

            // Check if category with same name already exists
            List<Category> existingCategories = categoryRepository.findAll();
            boolean nameExists = existingCategories.stream()
                    .anyMatch(existing -> existing.getName().equalsIgnoreCase(category.getName()));
            
            if (nameExists) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(409, "CONFLICT", "Category with name '" + category.getName() + "' already exists"));
            }

            Category savedCategory = categoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.created(savedCategory));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "INTERNAL_SERVER_ERROR", "Failed to create category"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category updatedCategory) {
        try {
            // Validate required fields
            if (updatedCategory.getName() == null || updatedCategory.getName().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.badRequest("Category name is required"));
            }
            
            if (updatedCategory.getType() != 0 && updatedCategory.getType() != 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.badRequest("Category type must be 0 (course) or 1 (quiz)"));
            }

            Optional<Category> existingCategory = categoryRepository.findById(id);
            if (existingCategory.isPresent()) {
                Category category = existingCategory.get();
                category.setName(updatedCategory.getName());
                category.setDescription(updatedCategory.getDescription());
                category.setType(updatedCategory.getType());

                Category savedCategory = categoryRepository.save(category);
                return ResponseEntity.ok(ApiResponse.success(savedCategory));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.not_found("Category with ID " + id + " not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "INTERNAL_SERVER_ERROR", "Failed to update category"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            Optional<Category> existingCategory = categoryRepository.findById(id);
            if (existingCategory.isPresent()) {
                Category categoryToDelete = existingCategory.get();
                categoryRepository.deleteById(id);
                return ResponseEntity.ok(ApiResponse.success(categoryToDelete));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.not_found("Category with ID " + id + " not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "INTERNAL_SERVER_ERROR", "Failed to delete category"));
        }
    }
}