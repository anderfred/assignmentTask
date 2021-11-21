package com.anderfred.assignmenttask.controller;

import com.anderfred.assignmenttask.model.OrderItem;
import com.anderfred.assignmenttask.model.Product;
import com.anderfred.assignmenttask.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("order_item")
@Tag(name = "Order Item", description = "Order Item entity CRUD REST API")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderService) {
        this.orderItemService = orderService;
    }

    @GetMapping("/")
    @Operation(summary = "Gets all Order Items", tags = "OrderItems")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all the order items",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = OrderItem.class)))
                    })
    })
    public List<OrderItem> getAllOrderItems() {
        return orderItemService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order item by id", tags = "OrderItems")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get order Item by id",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderItem.class))
                    })
    })
    public ResponseEntity<OrderItem> getOrderById(@PathVariable("id") long id) {
        Optional<OrderItem> order = orderItemService.findById(id);
        return order.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/")
    @Operation(summary = "Create new order item", tags = "OrderItems")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Create new order item",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderItem.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error")
    })
    public ResponseEntity<OrderItem> createOrderItem(@RequestBody OrderItem order) {
        try {
            OrderItem _order = orderItemService
                    .save(new OrderItem(order.getQuantity(), order.getProduct()));
            return new ResponseEntity<>(_order, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existed order item", tags = "OrderItems")
    @ApiResponse(
            responseCode = "200",
            description = "Update an existed order item",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderItem.class))
            })
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable("id") long id, @RequestBody OrderItem order) {
        Optional<OrderItem> orderData = orderItemService.findById(id);
        if (orderData.isPresent()) {
            OrderItem _order = orderData.get();
            _order.setQuantity(order.getQuantity());
            _order.setProduct(order.getProduct());
            return new ResponseEntity<>(orderItemService.save(_order), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order item by id", tags = "OrderItem")
    @ApiResponse(
            responseCode = "204",
            description = "Order item deleted")
    public ResponseEntity<HttpStatus> deleteOrderItem(@PathVariable("id") long id) {
        try {
            orderItemService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/")
    @Operation(summary = "Delete all order items", tags = "OrderItem")
    @ApiResponse(
            responseCode = "204",
            description = "All order items deleted")
    @ApiResponse(
            responseCode = "500",
            description = "Internal server error")
    public ResponseEntity<HttpStatus> deleteAllOrderItems() {
        try {
            orderItemService.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
