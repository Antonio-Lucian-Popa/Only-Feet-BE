package com.asusoftware.onlyFeet.payment.controller;

import com.asusoftware.onlyFeet.payment.model.dto.PaymentCreateRequestDto;
import com.asusoftware.onlyFeet.payment.model.dto.PaymentResponseDto;
import com.asusoftware.onlyFeet.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> create(@RequestBody PaymentCreateRequestDto request) {
        return ResponseEntity.ok(paymentService.createPayment(request));
    }

    @PutMapping("/{id}/success")
    public ResponseEntity<PaymentResponseDto> markSuccess(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.markAsSucceeded(id));
    }

    @PutMapping("/{id}/fail")
    public ResponseEntity<PaymentResponseDto> markFailed(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.markAsFailed(id));
    }
}