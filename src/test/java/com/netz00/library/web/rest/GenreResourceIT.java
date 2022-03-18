package com.netz00.library.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.netz00.library.IntegrationTest;
import com.netz00.library.domain.Genre;
import com.netz00.library.repository.GenreRepository;
import com.netz00.library.service.dto.GenreDTO;
import com.netz00.library.service.mapper.GenreMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GenreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GenreResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/genres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreMapper genreMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGenreMockMvc;

    private Genre genre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Genre createEntity(EntityManager em) {
        Genre genre = new Genre().name(DEFAULT_NAME).note(DEFAULT_NOTE);
        return genre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Genre createUpdatedEntity(EntityManager em) {
        Genre genre = new Genre().name(UPDATED_NAME).note(UPDATED_NOTE);
        return genre;
    }

    @BeforeEach
    public void initTest() {
        genre = createEntity(em);
    }

    @Test
    @Transactional
    void createGenre() throws Exception {
        int databaseSizeBeforeCreate = genreRepository.findAll().size();
        // Create the Genre
        GenreDTO genreDTO = genreMapper.toDto(genre);
        restGenreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genreDTO)))
            .andExpect(status().isCreated());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeCreate + 1);
        Genre testGenre = genreList.get(genreList.size() - 1);
        assertThat(testGenre.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGenre.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createGenreWithExistingId() throws Exception {
        // Create the Genre with an existing ID
        genre.setId(1L);
        GenreDTO genreDTO = genreMapper.toDto(genre);

        int databaseSizeBeforeCreate = genreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGenreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = genreRepository.findAll().size();
        // set the field null
        genre.setName(null);

        // Create the Genre, which fails.
        GenreDTO genreDTO = genreMapper.toDto(genre);

        restGenreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genreDTO)))
            .andExpect(status().isBadRequest());

        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGenres() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList
        restGenreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genre.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get the genre
        restGenreMockMvc
            .perform(get(ENTITY_API_URL_ID, genre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(genre.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingGenre() throws Exception {
        // Get the genre
        restGenreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        int databaseSizeBeforeUpdate = genreRepository.findAll().size();

        // Update the genre
        Genre updatedGenre = genreRepository.findById(genre.getId()).get();
        // Disconnect from session so that the updates on updatedGenre are not directly saved in db
        em.detach(updatedGenre);
        updatedGenre.name(UPDATED_NAME).note(UPDATED_NOTE);
        GenreDTO genreDTO = genreMapper.toDto(updatedGenre);

        restGenreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genreDTO))
            )
            .andExpect(status().isOk());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
        Genre testGenre = genreList.get(genreList.size() - 1);
        assertThat(testGenre.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGenre.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // Create the Genre
        GenreDTO genreDTO = genreMapper.toDto(genre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // Create the Genre
        GenreDTO genreDTO = genreMapper.toDto(genre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // Create the Genre
        GenreDTO genreDTO = genreMapper.toDto(genre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGenreWithPatch() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        int databaseSizeBeforeUpdate = genreRepository.findAll().size();

        // Update the genre using partial update
        Genre partialUpdatedGenre = new Genre();
        partialUpdatedGenre.setId(genre.getId());

        partialUpdatedGenre.name(UPDATED_NAME).note(UPDATED_NOTE);

        restGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenre))
            )
            .andExpect(status().isOk());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
        Genre testGenre = genreList.get(genreList.size() - 1);
        assertThat(testGenre.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGenre.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateGenreWithPatch() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        int databaseSizeBeforeUpdate = genreRepository.findAll().size();

        // Update the genre using partial update
        Genre partialUpdatedGenre = new Genre();
        partialUpdatedGenre.setId(genre.getId());

        partialUpdatedGenre.name(UPDATED_NAME).note(UPDATED_NOTE);

        restGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenre))
            )
            .andExpect(status().isOk());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
        Genre testGenre = genreList.get(genreList.size() - 1);
        assertThat(testGenre.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGenre.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // Create the Genre
        GenreDTO genreDTO = genreMapper.toDto(genre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, genreDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // Create the Genre
        GenreDTO genreDTO = genreMapper.toDto(genre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // Create the Genre
        GenreDTO genreDTO = genreMapper.toDto(genre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(genreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        int databaseSizeBeforeDelete = genreRepository.findAll().size();

        // Delete the genre
        restGenreMockMvc
            .perform(delete(ENTITY_API_URL_ID, genre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
