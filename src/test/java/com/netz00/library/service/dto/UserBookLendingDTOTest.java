package com.netz00.library.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.netz00.library.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserBookLendingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserBookLendingDTO.class);
        UserBookLendingDTO userBookLendingDTO1 = new UserBookLendingDTO();
        userBookLendingDTO1.setId(1L);
        UserBookLendingDTO userBookLendingDTO2 = new UserBookLendingDTO();
        assertThat(userBookLendingDTO1).isNotEqualTo(userBookLendingDTO2);
        userBookLendingDTO2.setId(userBookLendingDTO1.getId());
        assertThat(userBookLendingDTO1).isEqualTo(userBookLendingDTO2);
        userBookLendingDTO2.setId(2L);
        assertThat(userBookLendingDTO1).isNotEqualTo(userBookLendingDTO2);
        userBookLendingDTO1.setId(null);
        assertThat(userBookLendingDTO1).isNotEqualTo(userBookLendingDTO2);
    }
}
