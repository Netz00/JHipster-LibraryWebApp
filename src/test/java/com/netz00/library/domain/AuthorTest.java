package com.netz00.library.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.netz00.library.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuthorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Author.class);
        Author author1 = new Author();
        author1.setId(1L);
        Author author2 = new Author();
        author2.setId(author1.getId());
        assertThat(author1).isEqualTo(author2);
        author2.setId(2L);
        assertThat(author1).isNotEqualTo(author2);
        author1.setId(null);
        assertThat(author1).isNotEqualTo(author2);
    }
}
