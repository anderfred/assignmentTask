package com.anderfred.assignmenttask.controller;

import com.anderfred.assignmenttask.controller.generic.BaseController;
import com.anderfred.assignmenttask.model.Order;
import com.anderfred.assignmenttask.service.OrderService;
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
public class OrderController extends BaseController {
    Logger log = LoggerFactory.getLogger(OrderController.class);

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
        logDebug(log, "GetAllOrders");
        List<Order> orders = orderService.getAll();
        logDebug(log, "Found : " + orders);
        return orders;
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
        logDebug(log, String.format("GetOrderById:%d", id));
        Optional<Order> order = orderService.findById(id);
        order.ifPresentOrElse(v -> logDebug(log, String.format("Found order :%s", v.toString())), () -> logDebug(log, "Nothing found"));
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
        logDebug(log, "CreateOrder:" + order);
        try {
            Order _order = orderService
                    .save(new Order(order.getOrderItems(), order.getTotalAmount()));
            logDebug(log, "Created successfully:" + _order);
            return new ResponseEntity<>(_order, HttpStatus.CREATED);
        } catch (Exception e) {
            logError(log, "Error while creating order, " + e.getMessage());
            e.printStackTrace();
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
        logDebug(log, "UpdateOrder id:" + id + ", " + order);
        Optional<Order> orderData = orderService.findById(id);
        if (orderData.isPresent()) {
            Order _order = orderData.get();
            _order.setOrderItems(order.getOrderItems());
            logDebug(log, "Order updated successfully :" + _order);
            return new ResponseEntity<>(orderService.save(_order), HttpStatus.OK);
        } else {
            logDebug(log, "Order not found :" + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order by id with all of its order items", tags = "orders")
    @ApiResponse(
            responseCode = "204",
            description = "Order deleted")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("id") long id) {
        logDebug(log, "DeleteOrderById:" + id);
        try {
            orderService.deleteById(id);
            logDebug(log, "Order deleted");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logError(log, "Order : " + id + ", deleted");
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
        logDebug(log, "DeleteAllOrders");
        try {
            orderService.deleteAll();
            logDebug(log, "All orders deleted");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logError(log, "Error, while deleting all orders, " + e.getMessage());
            e.printStackTrace();
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
        logDebug(log, "Elastic search by product name part : " + name);
        Set<Order> result = orderService.findByProductNameCustom(name, Pageable.ofSize(10));
        if (result != null && !result.isEmpty()) logDebug(log, "Found :" + result);
        else logDebug(log, "Nothing found");
        return (result != null && !result.isEmpty()) ? new ResponseEntity<>(result, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
