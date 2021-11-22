package com.anderfred.assignmenttask.controller;

import com.anderfred.assignmenttask.controller.generic.BaseController;
import com.anderfred.assignmenttask.model.Product;
import com.anderfred.assignmenttask.service.ProductService;
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
@RequestMapping("product")
@Tag(name = "Product", description = "Product entity CRUD REST API")
public class ProductController extends BaseController {
    Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    @Operation(summary = "Gets all products", tags = "products")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all the products",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Product.class)))
                    })
    })
    public ResponseEntity<List<Product>> getAllProducts() {
        logDebug(log, "GetAllProducts");
        try {
            List<Product> products = productService.getAll();
            if (products.isEmpty()) {
                logDebug(log, "Nothing found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            logDebug(log, "Found : " + products);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            logError(log, "Error getting all products, " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id", tags = "products")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get product by id",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Product.class))
                    })
    })
    public ResponseEntity<Product> getProductById(@PathVariable("id") long id) {
        logDebug(log, "GetProductById : " + id);
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) logDebug(log, "Found : " + product.get());
        else logDebug(log, "Nothing found by id : " + id);
        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/")
    @Operation(summary = "Create new product", tags = "products")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Create category",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Product.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error")
    })
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        logDebug(log, "CreatingProduct : " + product);
        try {
            Product _product = productService.save(new Product(product.getPrice(), product.getName(), product.getSku()));
            logDebug(log, "Success, " + _product);
            return new ResponseEntity<>(_product, HttpStatus.CREATED);
        } catch (Exception e) {
            logError(log, "Error creating new product, " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existed product", tags = "products")
    @ApiResponse(
            responseCode = "200",
            description = "Update an existed product",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))
            })
    public ResponseEntity<Product> updateProduct(@PathVariable("id") long id, @RequestBody Product product) {
        logDebug(log, "Updating product id : " + id + ", " + product);
        Optional<Product> productData = productService.findById(id);
        if (productData.isPresent()) {
            Product _product = productData.get();
            _product.setPrice(product.getPrice());
            _product.setName(product.getName());
            _product.setSku(product.getSku());
            logDebug(log, "Success");
            return new ResponseEntity<>(productService.save(_product), HttpStatus.OK);
        } else {
            logDebug(log, "Not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product by id", tags = "products")
    @ApiResponse(
            responseCode = "204",
            description = "Product deleted")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") long id) {
        logDebug(log, "DeletingProduct id : " + id);
        try {
            productService.deleteById(id);
            logDebug(log, "Success");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logError(log, "Error deleting product by id : " + id + ", " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/")
    @Operation(summary = "Delete all products", tags = "products")
    @ApiResponse(
            responseCode = "204",
            description = "Products deleted")
    @ApiResponse(
            responseCode = "500",
            description = "Internal server error")
    public ResponseEntity<HttpStatus> deleteAllProducts() {
        logDebug(log, "Deleting all products");
        try {
            productService.deleteAll();
            logDebug(log, "Success");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logError(log, "Error wile deleting all products," + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
