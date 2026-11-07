package com.sd.accounts.controllers;

import com.sd.accounts.services.IPaymentReportService;
import com.sd.accounts.services.PaymentReportService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * BulkHead pattern: Thread Pool BulkHead
 *
 * This pattern is used to isolate and limit the impact of failures or high loads in one
 * component from spreading to other components by allocating limited resources which can
 * be used for specific services. So that resource exhaustion can be reduced.
 *
 * Here endpoints delegates the tasks to service methods that uses dedicated Thread pools.
 * */

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class PaymentController {
    private final IPaymentReportService paymentReportService;

    public PaymentController(IPaymentReportService paymentReportService){
        this.paymentReportService = paymentReportService;
    }

    //Higher latency task means more threads needed
    //ThreadPool: inventoryReportThreadPool
    @GetMapping("/inventory-report")
    public CompletionStage<String> fetchInventoryReport(){
        System.out.println("Controller received request on Tomcat thread: " + Thread.currentThread().getName());
        return this.paymentReportService.getInventoryReport();
    }


    //Lower latency task means fewer threads needed
    //ThreadPool: paymentReportThreadPool
    @GetMapping("/payment-report")
    public CompletionStage<String> fetchPaymentReport(){
        System.out.println("Controller received request on Tomcat thread: " + Thread.currentThread().getName());
        return this.paymentReportService.getPaymentReport();
    }


}
