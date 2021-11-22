package com.anderfred.assignmenttask.controller;

import com.anderfred.assignmenttask.controller.generic.BaseController;
import com.anderfred.assignmenttask.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("report")
@Tag(name = "Report", description = "Report controller REST API")
public class ReportController extends BaseController {
    Logger log = LoggerFactory.getLogger(ReportController.class);
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
        logDebug(log, "Getting full report");
        try {
            String response = orderService.findReportData();
            if (response != null && !response.isEmpty()) {
                logDebug(log, "Got - " + response);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                logDebug(log, "Nothing found");
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            logError(log, "Error getting full report, " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
