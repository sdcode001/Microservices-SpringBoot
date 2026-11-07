package com.sd.accounts.services;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


/**
 * BulkHead pattern: Thread Pool BulkHead
 *
 * This pattern is used to isolate and limit the impact of failures or high loads in one
 * component from spreading to other components by allocating limited resources which can
 * be used for specific services. So that resource exhaustion can be reduced.
 *
 * Thread Pool Bulkhead, you will isolate your slow or heavy tasks into dedicated thread pools
 * This guarantees that even if a specific downstream dependency stalls completely, it will only
 * exhaust its own small thread pool, leaving the main container threads (like Tomcat) free to
 * serve other API endpoints.
 * Configure each Thread Pool in application.yaml
 * */

@Service
public class PaymentReportService implements IPaymentReportService{

    @Override
    @Bulkhead(
            name = "inventoryReportThreadPool",
            type = Bulkhead.Type.THREADPOOL,
            fallbackMethod = "bulkheadFallback"
    )
    public CompletionStage<String> getInventoryReport() {
        // This code block automatically executes inside the 'inventoryReportThreadPool' thread
        System.out.println("Request processing inside inventoryReportThreadPool Thread: "+Thread.currentThread().getName());
        simulateSlowNetworkCall();
        return CompletableFuture.completedStage("Successfully fetched inventory report");
    }


    @Override
    @Bulkhead(
            name = "paymentReportThreadPool",
            type = Bulkhead.Type.THREADPOOL,
            fallbackMethod = "bulkheadFallback"
    )
    public CompletionStage<String> getPaymentReport() {
        // This code block automatically executes inside the 'paymentReportThreadPool' thread
        System.out.println("Request processing inside paymentReportThreadPool Thread: "+Thread.currentThread().getName());
        return CompletableFuture.completedStage("Successfully fetched inventory report");
    }


    public CompletionStage<String> bulkheadFallback(Throwable ex) {
        return CompletableFuture.completedFuture(
                "Bulkhead Fallback: An unexpected error occurred: " + ex.getMessage()
        );
    }

    private void simulateSlowNetworkCall() {
        try {
            Thread.sleep(3000); // Simulating a 3-second delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
