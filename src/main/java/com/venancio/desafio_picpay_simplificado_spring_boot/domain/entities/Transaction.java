package com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.TransferStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payer_id", nullable = false)
    private User payer;

    @ManyToOne
    @JoinColumn(name = "payee_id", nullable = false)
    private User payee;

    private BigDecimal value;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Define os valores de `createdAt` e `updatedAt` antes de persistir o objeto.
     */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Atualiza o valor de `updatedAt` antes de atualizar o objeto.
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
