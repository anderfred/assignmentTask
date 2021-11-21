package com.anderfred.assignmenttask.controller;

import com.anderfred.assignmenttask.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("report")
@Tag(name = "Report", description = "Report controller REST API")
public class ReportController {
    private final OrderService orderService;

    @Autowired
    public ReportController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/")
    @Operation(summary = "Get every day income report ", tags = "reports")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get report",
                    content = {
                            @Content(
                                    mediaType = "application/json")
                    })
    })
    public ResponseEntity<String> getFullReport() {
        try {
            String response = orderService.findReportData();
            if (response != null && !response.isEmpty()) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
