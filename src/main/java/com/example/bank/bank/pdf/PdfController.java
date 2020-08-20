package com.example.bank.bank.pdf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
public class PdfController {

    @RequestMapping(value = "/raport")
    public String handleForexRequest(Model model) {
        model.addAttribute("report", getReport());
        return "reportView";
    }

    public Report getReport() {
        //dummy report
        Report report = new Report();
        report.setName("Raport");
        report.setDate(LocalDateTime.now());
        return report;
    }
}