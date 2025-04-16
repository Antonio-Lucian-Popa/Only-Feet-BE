package com.asusoftware.onlyFeet.payment.service;

import com.asusoftware.onlyFeet.payment.model.Payment;
import com.asusoftware.onlyFeet.payment.model.PaymentStatus;
import com.asusoftware.onlyFeet.payment.model.PaymentType;
import com.asusoftware.onlyFeet.payment.model.dto.PaymentCreateRequestDto;
import com.asusoftware.onlyFeet.payment.model.dto.PaymentResponseDto;
import com.asusoftware.onlyFeet.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentResponseDto createPayment(PaymentCreateRequestDto request) {
        Payment payment = Payment.builder()
                .id(UUID.randomUUID())
                .userId(request.getUserId())
                .contentId(request.getContentId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .type(request.getType().name())
                .status(PaymentStatus.PENDING.name())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return mapToDto(paymentRepository.save(payment));
    }

    public PaymentResponseDto markAsSucceeded(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        payment.setStatus(PaymentStatus.SUCCEEDED.name());
        payment.setUpdatedAt(LocalDateTime.now());
        return mapToDto(paymentRepository.save(payment));
    }

    public PaymentResponseDto markAsFailed(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        payment.setStatus(PaymentStatus.FAILED.name());
        payment.setUpdatedAt(LocalDateTime.now());
        return mapToDto(paymentRepository.save(payment));
    }

    private PaymentResponseDto mapToDto(Payment p) {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(p.getId());
        dto.setUserId(p.getUserId());
        dto.setContentId(p.getContentId());
        dto.setAmount(p.getAmount());
        dto.setPaymentMethod(p.getPaymentMethod());
        dto.setType(Enum.valueOf(PaymentType.class, p.getType()));
        dto.setStatus(Enum.valueOf(PaymentStatus.class, p.getStatus()));
        dto.setCreatedAt(p.getCreatedAt());
        return dto;
    }
}
