package com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TransferStatusTest {

    @Test
    void testEnumValues() {
        TransferStatus pending = TransferStatus.pending;
        TransferStatus finalized = TransferStatus.finalized;

        assertEquals("pending", pending.name());
        assertEquals("finalized", finalized.name());

        assertSame(TransferStatus.pending, TransferStatus.valueOf("pending"));
        assertSame(TransferStatus.finalized, TransferStatus.valueOf("finalized"));
    }
}
