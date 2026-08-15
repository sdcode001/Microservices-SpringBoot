package com.sd.accounts.services.clients;

import com.sd.accounts.dtos.LoansDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Feign Client interface for the "loans" microservice.
 *
 * 1. SERVICE DISCOVERY LOOKUP (Finding the Targets)
 *    - The identifier "loans" inside @FeignClient is a logical service name, not a real URL.
 *    - When this method is triggered, Spring Cloud queries your Service Registry (like Eureka).
 *    - The registry returns a list of all currently healthy, active network locations (IPs and Ports) for "loans".
 *
 * 2. SPRING CLOUD LOAD BALANCING (Choosing the Best Instance)
 *    - The list of available instances is handed over to **Spring Cloud Load Balancer**.
 *    - It applies a load-balancing strategy (by default, **Round-Robin** algorithm).
 *    - It selects one specific instance from the pool and replaces the word "loans" with that concrete host:port.
 *
 * 3. INSTANCE DETAILS CACHING (Optimizing Performance)
 *    - Querying the central Service Registry for every single HTTP request creates severe network overhead.
 *    - To fix this, the Discovery Client maintains a **local, in-memory cache** of these instance mappings.
 *    - **Cache Maintenance:** A background thread regularly pulls updates from the registry (typically every 30 seconds).
 *    - If an instance crashes or stops sending heartbeats, the registry marks it down, and the local cache drops it during the next sync cycle.
 *
 * 4. ROUTING & SERVICE SEPARATION
 *    - **Target Endpoint:** Once the physical URL is ready, Feign builds the HTTP GET request to "/api/loans", injects the query parameter, and executes the network call.
 *    - **Loans Microservice Note:** Feign clients are strictly bound to one logical service. Because this client is declared for "loans", it cannot directly route to a loans service. To hit a loans endpoint, you must declare a companion interface annotated with `@FeignClient("loans")`.
 */
@FeignClient("loans")
public interface LoansFeignClient {

    @GetMapping(value = "/api/loans", consumes = "application/json")
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam String mobileNumber);
}
