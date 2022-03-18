package com.netz00.library.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GenreMapperTest {

    private GenreMapper genreMapper;

    @BeforeEach
    public void setUp() {
        genreMapper = new GenreMapperImpl();
    }
}
