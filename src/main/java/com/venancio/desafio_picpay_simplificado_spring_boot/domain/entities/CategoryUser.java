package com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa uma categoria de usuário no sistema.
 * Cada categoria é identificada por um nome único e está associada a vários usuários.
 *
 * @author Venâncio
 */
@Entity
@Table(name = "tb_category_users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUser implements Serializable {

    /**
     * Identificador único da categoria de usuário.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Nome da categoria de usuário, representado por um enum.
     * Este campo é obrigatório e deve ser único.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private CategoryUserNameEnum name;

    /**
     * Conjunto de usuários associados à categoria.
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();

    /**
     * Data e hora em que a categoria foi criada.
     */
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Data e hora da última atualização da categoria.
     */
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Construtor com o nome da categoria.
     *
     * @param name O nome da categoria de usuário.
     */
    public CategoryUser(CategoryUserNameEnum name) {
        this.name = name;
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
