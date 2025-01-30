package com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CategoryUserNameEnumTest {

    @Test
    void testEnumValues() {
        CategoryUserNameEnum common = CategoryUserNameEnum.common;
        CategoryUserNameEnum store = CategoryUserNameEnum.store;

        assertEquals("common", common.name());
        assertEquals("store", store.name());

        assertSame(CategoryUserNameEnum.common, CategoryUserNameEnum.valueOf("common"));
        assertSame(CategoryUserNameEnum.store, CategoryUserNameEnum.valueOf("store"));
    }
}