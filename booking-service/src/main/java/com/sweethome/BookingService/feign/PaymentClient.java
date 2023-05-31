package com.sweethome.BookingService.feign;


import com.sweethome.BookingService.DTO.PaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "PAYMENT-SERVICE",  url = "http://localhost:8083")
public interface PaymentClient {
    @PostMapping(value="/payment/transaction", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public int getPaymentConfirmation (PaymentDTO paymentDTO);

}
