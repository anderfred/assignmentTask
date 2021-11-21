package com.anderfred.assignmenttask.controller;

import com.anderfred.assignmenttask.model.Order;
import com.anderfred.assignmenttask.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("order")
@Tag(name = "Order", description = "Order entity CRUD REST API")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/")
    @Operation(summary = "Gets all orders", tags = "orders")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all the orders",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Order.class)))
                    })
    })
    public List<Order> getAllOrders() {
        return orderService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by id", tags = "orders")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get order by id",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Order.class))
                    })
    })
    public ResponseEntity<Order> getOrderById(@PathVariable("id") long id) {
        Optional<Order> order = orderService.findById(id);
        return order.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/")
    @Operation(summary = "Create new order", tags = "orders")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Create order",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Order.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error")
    })
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        try {
            Order _order = orderService
                    .save(new Order(order.getOrderItems(), order.getTotalAmount()));
            return new ResponseEntity<>(_order, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an order", tags = "orders")
    @ApiResponse(
            responseCode = "200",
            description = "Update an existed order",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Order.class))
            })
    public ResponseEntity<Order> updateOrder(@PathVariable("id") long id, @RequestBody Order order) {
        Optional<Order> orderData = orderService.findById(id);
        if (orderData.isPresent()) {
            Order _order = orderData.get();
            _order.setOrderItems(order.getOrderItems());
            return new ResponseEntity<>(orderService.save(_order), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order by id with all of its order items", tags = "orders")
    @ApiResponse(
            responseCode = "204",
            description = "Order deleted")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("id") long id) {
        try {
            orderService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/")
    @Operation(summary = "Delete all orders as well as all order items", tags = "orders")
    @ApiResponse(
            responseCode = "204",
            description = "Orders deleted")
    @ApiResponse(
            responseCode = "500",
            description = "Internal server error")
    public ResponseEntity<HttpStatus> deleteAllOrders() {
        try {
            orderService.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Get list of orders by partial product name", tags = "orders")
    @ApiResponse(
            responseCode = "200",
            description = "Search by product name",
            content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Order.class)))
            })
    public ResponseEntity<Set<Order>> search(@RequestParam("name") String name) {
        Set<Order> result =  orderService.findByProductNameCustom(name, Pageable.ofSize(10));
        return (result != null && !result.isEmpty()) ? new ResponseEntity<>(result, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
