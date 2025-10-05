package com.cdac.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "order")
public class Transaction extends BaseEntity {

    @Column(name = "payment_id", length = 128, unique = true, nullable = false)
    private String paymentId;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod; // e.g., "CARD", "UPI", "NET_BANKING"

    @Column(name = "status", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @CreationTimestamp
    @Column(name = "transaction_date", nullable = false, updatable = false)
    private LocalDateTime transactionDate;

    // Link to order (owning side is Transaction here)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    // optional: raw response from gateway for audit/debug
    @Lob
    @Column(name = "response_data")
    private String responseData;
}
