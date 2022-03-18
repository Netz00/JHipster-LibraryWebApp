package com.netz00.library.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserBookLendingMapperTest {

    private UserBookLendingMapper userBookLendingMapper;

    @BeforeEach
    public void setUp() {
        userBookLendingMapper = new UserBookLendingMapperImpl();
    }
}
