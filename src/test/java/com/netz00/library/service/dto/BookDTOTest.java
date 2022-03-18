package com.netz00.library.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.netz00.library.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookDTO.class);
        BookDTO bookDTO1 = new BookDTO();
        bookDTO1.setId(1L);
        BookDTO bookDTO2 = new BookDTO();
        assertThat(bookDTO1).isNotEqualTo(bookDTO2);
        bookDTO2.setId(bookDTO1.getId());
        assertThat(bookDTO1).isEqualTo(bookDTO2);
        bookDTO2.setId(2L);
        assertThat(bookDTO1).isNotEqualTo(bookDTO2);
        bookDTO1.setId(null);
        assertThat(bookDTO1).isNotEqualTo(bookDTO2);
    }
}
