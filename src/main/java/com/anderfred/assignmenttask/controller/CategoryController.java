package com.anderfred.assignmenttask.controller;

import com.anderfred.assignmenttask.controller.generic.BaseController;
import com.anderfred.assignmenttask.model.Category;
import com.anderfred.assignmenttask.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("category")
@Tag(name = "Category", description = "Category entity CRUD REST API")
public class CategoryController extends BaseController {
    Logger log = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    @Operation(summary = "Gets all categories", tags = "categories")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all the categories",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Category.class)))
                    })
    })
    public List<Category> getAllCategories() {
        logDebug(log, "getAllCategories");
        List<Category> categories = categoryService.getAll();
        logDebug(log, "Found : " + categories);
        return categories;
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get category by id", tags = "categories")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get category by id",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Category.class))
                    })
    })
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") long id) {
        logDebug(log, String.format("getCategoryById:%d", id));
        Optional<Category> category = categoryService.findById(id);
        category.ifPresentOrElse(v->logDebug(log, String.format("Found category :%s", v.toString())), ()->logDebug(log, "nothing found"));
        return category.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/")
    @Operation(summary = "Create new category", tags = "categories")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Create category",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Category.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error")
    })
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        try {
            Category _category = categoryService
                    .save(new Category(category.getName(), category.getProducts()));
            return new ResponseEntity<>(_category, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existed category", tags = "categories")
    @ApiResponse(
            responseCode = "200",
            description = "Update an existed category",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))
            })
    public ResponseEntity<Category> updateCategory(@PathVariable("id") long id, @RequestBody Category category) {
        Optional<Category> categoryData = categoryService.findById(id);
        if (categoryData.isPresent()) {
            Category _category = categoryData.get();
            _category.setName(category.getName());
            _category.setProducts(category.getProducts());
            return new ResponseEntity<>(categoryService.save(_category), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category by id", tags = "categories")
    @ApiResponse(
            responseCode = "204",
            description = "Category deleted")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable("id") long id) {
        try {
            categoryService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/")
    @Operation(summary = "Delete all categories", tags = "categories")
    @ApiResponse(
            responseCode = "204",
            description = "Categories deleted")
    @ApiResponse(
            responseCode = "500",
            description = "Internal server error")
    public ResponseEntity<HttpStatus> deleteAllCategories() {
        try {
            categoryService.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
