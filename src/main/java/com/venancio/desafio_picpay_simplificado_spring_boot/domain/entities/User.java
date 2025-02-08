package com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, name = "cpf_cnpj")
    private String cpfCnpj;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryUser category;

    @OneToMany(mappedBy = "payer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Transaction> payerTransactions = new HashSet<>();

    @OneToMany(mappedBy = "payee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Transaction> payeeTransactions = new HashSet<>();;

    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    public User(String name,
                String cpfCnpj,
                String email,
                String password,
                CategoryUser category) {
        this.name = name;
        this.cpfCnpj = cpfCnpj;
        this.email = email;
        this.password = password;
        this.category = category;
    }

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
