package com.netz00.library.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.netz00.library.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserBookLendingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserBookLending.class);
        UserBookLending userBookLending1 = new UserBookLending();
        userBookLending1.setId(1L);
        UserBookLending userBookLending2 = new UserBookLending();
        userBookLending2.setId(userBookLending1.getId());
        assertThat(userBookLending1).isEqualTo(userBookLending2);
        userBookLending2.setId(2L);
        assertThat(userBookLending1).isNotEqualTo(userBookLending2);
        userBookLending1.setId(null);
        assertThat(userBookLending1).isNotEqualTo(userBookLending2);
    }
}
