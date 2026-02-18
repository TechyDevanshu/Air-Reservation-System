package com.example.report_service.Controller;

import com.example.report_service.Dto.BookingReport;
import com.example.report_service.Service.ReportService;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/date")
    public List<BookingReport> reportByDate
            (@RequestParam
             @NotNull(message = "Journey Date is required")
             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
             LocalDate date) {
        return reportService.getReportByJourneyDate(date);
    }

    @GetMapping("/destination")
    public List<BookingReport> reportByDestination
            (@RequestParam
             @NotBlank(message = "Destination is required")
             String destination) {

        return reportService.getReportByDestination(destination);
    }
}
