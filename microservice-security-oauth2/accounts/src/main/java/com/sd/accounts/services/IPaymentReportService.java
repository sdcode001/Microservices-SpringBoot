package com.sd.accounts.services;

import java.util.concurrent.CompletionStage;

public interface IPaymentReportService {
    CompletionStage<String> getInventoryReport();
    CompletionStage<String> getPaymentReport();
}
