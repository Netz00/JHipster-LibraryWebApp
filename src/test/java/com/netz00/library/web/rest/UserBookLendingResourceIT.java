package com.netz00.library.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.netz00.library.IntegrationTest;
import com.netz00.library.domain.Book;
import com.netz00.library.domain.LibraryUser;
import com.netz00.library.domain.UserBookLending;
import com.netz00.library.domain.enumeration.LendingStatus;
import com.netz00.library.repository.UserBookLendingRepository;
import com.netz00.library.service.dto.UserBookLendingDTO;
import com.netz00.library.service.mapper.UserBookLendingMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link UserBookLendingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserBookLendingResourceIT {

    private static final Instant DEFAULT_LOANTIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LOANTIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RETURNTIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RETURNTIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LendingStatus DEFAULT_STATUS = LendingStatus.LENDED;
    private static final LendingStatus UPDATED_STATUS = LendingStatus.RETURNED;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-book-lendings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserBookLendingRepository userBookLendingRepository;

    @Autowired
    private UserBookLendingMapper userBookLendingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserBookLendingMockMvc;

    private UserBookLending userBookLending;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserBookLending createEntity(EntityManager em) {
        UserBookLending userBookLending = new UserBookLending()
            .loantime(DEFAULT_LOANTIME)
            .returntime(DEFAULT_RETURNTIME)
            .status(DEFAULT_STATUS)
            .note(DEFAULT_NOTE);
        // Add required entity
        LibraryUser libraryUser;
        if (TestUtil.findAll(em, LibraryUser.class).isEmpty()) {
            libraryUser = LibraryUserResourceIT.createEntity(em);
            em.persist(libraryUser);
            em.flush();
        } else {
            libraryUser = TestUtil.findAll(em, LibraryUser.class).get(0);
        }
        userBookLending.setUser(libraryUser);
        // Add required entity
        Book book;
        if (TestUtil.findAll(em, Book.class).isEmpty()) {
            book = BookResourceIT.createEntity(em);
            em.persist(book);
            em.flush();
        } else {
            book = TestUtil.findAll(em, Book.class).get(0);
        }
        userBookLending.setBook(book);
        return userBookLending;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserBookLending createUpdatedEntity(EntityManager em) {
        UserBookLending userBookLending = new UserBookLending()
            .loantime(UPDATED_LOANTIME)
            .returntime(UPDATED_RETURNTIME)
            .status(UPDATED_STATUS)
            .note(UPDATED_NOTE);
        // Add required entity
        LibraryUser libraryUser;
        if (TestUtil.findAll(em, LibraryUser.class).isEmpty()) {
            libraryUser = LibraryUserResourceIT.createUpdatedEntity(em);
            em.persist(libraryUser);
            em.flush();
        } else {
            libraryUser = TestUtil.findAll(em, LibraryUser.class).get(0);
        }
        userBookLending.setUser(libraryUser);
        // Add required entity
        Book book;
        if (TestUtil.findAll(em, Book.class).isEmpty()) {
            book = BookResourceIT.createUpdatedEntity(em);
            em.persist(book);
            em.flush();
        } else {
            book = TestUtil.findAll(em, Book.class).get(0);
        }
        userBookLending.setBook(book);
        return userBookLending;
    }

    @BeforeEach
    public void initTest() {
        userBookLending = createEntity(em);
    }

    @Test
    @Transactional
    void createUserBookLending() throws Exception {
        int databaseSizeBeforeCreate = userBookLendingRepository.findAll().size();
        // Create the UserBookLending
        UserBookLendingDTO userBookLendingDTO = userBookLendingMapper.toDto(userBookLending);
        restUserBookLendingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userBookLendingDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserBookLending in the database
        List<UserBookLending> userBookLendingList = userBookLendingRepository.findAll();
        assertThat(userBookLendingList).hasSize(databaseSizeBeforeCreate + 1);
        UserBookLending testUserBookLending = userBookLendingList.get(userBookLendingList.size() - 1);
        assertThat(testUserBookLending.getLoantime()).isEqualTo(DEFAULT_LOANTIME);
        assertThat(testUserBookLending.getReturntime()).isEqualTo(DEFAULT_RETURNTIME);
        assertThat(testUserBookLending.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUserBookLending.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createUserBookLendingWithExistingId() throws Exception {
        // Create the UserBookLending with an existing ID
        userBookLending.setId(1L);
        UserBookLendingDTO userBookLendingDTO = userBookLendingMapper.toDto(userBookLending);

        int databaseSizeBeforeCreate = userBookLendingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserBookLendingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userBookLendingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserBookLending in the database
        List<UserBookLending> userBookLendingList = userBookLendingRepository.findAll();
        assertThat(userBookLendingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLoantimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = userBookLendingRepository.findAll().size();
        // set the field null
        userBookLending.setLoantime(null);

        // Create the UserBookLending, which fails.
        UserBookLendingDTO userBookLendingDTO = userBookLendingMapper.toDto(userBookLending);

        restUserBookLendingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userBookLendingDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserBookLending> userBookLendingList = userBookLendingRepository.findAll();
        assertThat(userBookLendingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = userBookLendingRepository.findAll().size();
        // set the field null
        userBookLending.setStatus(null);

        // Create the UserBookLending, which fails.
        UserBookLendingDTO userBookLendingDTO = userBookLendingMapper.toDto(userBookLending);

        restUserBookLendingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userBookLendingDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserBookLending> userBookLendingList = userBookLendingRepository.findAll();
        assertThat(userBookLendingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserBookLendings() throws Exception {
        // Initialize the database
        userBookLendingRepository.saveAndFlush(userBookLending);

        // Get all the userBookLendingList
        restUserBookLendingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userBookLending.getId().intValue())))
            .andExpect(jsonPath("$.[*].loantime").value(hasItem(DEFAULT_LOANTIME.toString())))
            .andExpect(jsonPath("$.[*].returntime").value(hasItem(DEFAULT_RETURNTIME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getUserBookLending() throws Exception {
        // Initialize the database
        userBookLendingRepository.saveAndFlush(userBookLending);

        // Get the userBookLending
        restUserBookLendingMockMvc
            .perform(get(ENTITY_API_URL_ID, userBookLending.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userBookLending.getId().intValue()))
            .andExpect(jsonPath("$.loantime").value(DEFAULT_LOANTIME.toString()))
            .andExpect(jsonPath("$.returntime").value(DEFAULT_RETURNTIME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingUserBookLending() throws Exception {
        // Get the userBookLending
        restUserBookLendingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserBookLending() throws Exception {
        // Initialize the database
        userBookLendingRepository.saveAndFlush(userBookLending);

        int databaseSizeBeforeUpdate = userBookLendingRepository.findAll().size();

        // Update the userBookLending
        UserBookLending updatedUserBookLending = userBookLendingRepository.findById(userBookLending.getId()).get();
        // Disconnect from session so that the updates on updatedUserBookLending are not directly saved in db
        em.detach(updatedUserBookLending);
        updatedUserBookLending.loantime(UPDATED_LOANTIME).returntime(UPDATED_RETURNTIME).status(UPDATED_STATUS).note(UPDATED_NOTE);
        UserBookLendingDTO userBookLendingDTO = userBookLendingMapper.toDto(updatedUserBookLending);

        restUserBookLendingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userBookLendingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userBookLendingDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserBookLending in the database
        List<UserBookLending> userBookLendingList = userBookLendingRepository.findAll();
        assertThat(userBookLendingList).hasSize(databaseSizeBeforeUpdate);
        UserBookLending testUserBookLending = userBookLendingList.get(userBookLendingList.size() - 1);
        assertThat(testUserBookLending.getLoantime()).isEqualTo(UPDATED_LOANTIME);
        assertThat(testUserBookLending.getReturntime()).isEqualTo(UPDATED_RETURNTIME);
        assertThat(testUserBookLending.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserBookLending.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingUserBookLending() throws Exception {
        int databaseSizeBeforeUpdate = userBookLendingRepository.findAll().size();
        userBookLending.setId(count.incrementAndGet());

        // Create the UserBookLending
        UserBookLendingDTO userBookLendingDTO = userBookLendingMapper.toDto(userBookLending);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserBookLendingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userBookLendingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userBookLendingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserBookLending in the database
        List<UserBookLending> userBookLendingList = userBookLendingRepository.findAll();
        assertThat(userBookLendingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserBookLending() throws Exception {
        int databaseSizeBeforeUpdate = userBookLendingRepository.findAll().size();
        userBookLending.setId(count.incrementAndGet());

        // Create the UserBookLending
        UserBookLendingDTO userBookLendingDTO = userBookLendingMapper.toDto(userBookLending);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserBookLendingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userBookLendingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserBookLending in the database
        List<UserBookLending> userBookLendingList = userBookLendingRepository.findAll();
        assertThat(userBookLendingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserBookLending() throws Exception {
        int databaseSizeBeforeUpdate = userBookLendingRepository.findAll().size();
        userBookLending.setId(count.incrementAndGet());

        // Create the UserBookLending
        UserBookLendingDTO userBookLendingDTO = userBookLendingMapper.toDto(userBookLending);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserBookLendingMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userBookLendingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserBookLending in the database
        List<UserBookLending> userBookLendingList = userBookLendingRepository.findAll();
        assertThat(userBookLendingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserBookLendingWithPatch() throws Exception {
        // Initialize the database
        userBookLendingRepository.saveAndFlush(userBookLending);

        int databaseSizeBeforeUpdate = userBookLendingRepository.findAll().size();

        // Update the userBookLending using partial update
        UserBookLending partialUpdatedUserBookLending = new UserBookLending();
        partialUpdatedUserBookLending.setId(userBookLending.getId());

        partialUpdatedUserBookLending.loantime(UPDATED_LOANTIME).returntime(UPDATED_RETURNTIME).status(UPDATED_STATUS).note(UPDATED_NOTE);

        restUserBookLendingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserBookLending.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserBookLending))
            )
            .andExpect(status().isOk());

        // Validate the UserBookLending in the database
        List<UserBookLending> userBookLendingList = userBookLendingRepository.findAll();
        assertThat(userBookLendingList).hasSize(databaseSizeBeforeUpdate);
        UserBookLending testUserBookLending = userBookLendingList.get(userBookLendingList.size() - 1);
        assertThat(testUserBookLending.getLoantime()).isEqualTo(UPDATED_LOANTIME);
        assertThat(testUserBookLending.getReturntime()).isEqualTo(UPDATED_RETURNTIME);
        assertThat(testUserBookLending.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserBookLending.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateUserBookLendingWithPatch() throws Exception {
        // Initialize the database
        userBookLendingRepository.saveAndFlush(userBookLending);

        int databaseSizeBeforeUpdate = userBookLendingRepository.findAll().size();

        // Update the userBookLending using partial update
        UserBookLending partialUpdatedUserBookLending = new UserBookLending();
        partialUpdatedUserBookLending.setId(userBookLending.getId());

        partialUpdatedUserBookLending.loantime(UPDATED_LOANTIME).returntime(UPDATED_RETURNTIME).status(UPDATED_STATUS).note(UPDATED_NOTE);

        restUserBookLendingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserBookLending.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserBookLending))
            )
            .andExpect(status().isOk());

        // Validate the UserBookLending in the database
        List<UserBookLending> userBookLendingList = userBookLendingRepository.findAll();
        assertThat(userBookLendingList).hasSize(databaseSizeBeforeUpdate);
        UserBookLending testUserBookLending = userBookLendingList.get(userBookLendingList.size() - 1);
        assertThat(testUserBookLending.getLoantime()).isEqualTo(UPDATED_LOANTIME);
        assertThat(testUserBookLending.getReturntime()).isEqualTo(UPDATED_RETURNTIME);
        assertThat(testUserBookLending.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserBookLending.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingUserBookLending() throws Exception {
        int databaseSizeBeforeUpdate = userBookLendingRepository.findAll().size();
        userBookLending.setId(count.incrementAndGet());

        // Create the UserBookLending
        UserBookLendingDTO userBookLendingDTO = userBookLendingMapper.toDto(userBookLending);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserBookLendingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userBookLendingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userBookLendingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserBookLending in the database
        List<UserBookLending> userBookLendingList = userBookLendingRepository.findAll();
        assertThat(userBookLendingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserBookLending() throws Exception {
        int databaseSizeBeforeUpdate = userBookLendingRepository.findAll().size();
        userBookLending.setId(count.incrementAndGet());

        // Create the UserBookLending
        UserBookLendingDTO userBookLendingDTO = userBookLendingMapper.toDto(userBookLending);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserBookLendingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userBookLendingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserBookLending in the database
        List<UserBookLending> userBookLendingList = userBookLendingRepository.findAll();
        assertThat(userBookLendingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserBookLending() throws Exception {
        int databaseSizeBeforeUpdate = userBookLendingRepository.findAll().size();
        userBookLending.setId(count.incrementAndGet());

        // Create the UserBookLending
        UserBookLendingDTO userBookLendingDTO = userBookLendingMapper.toDto(userBookLending);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserBookLendingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userBookLendingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserBookLending in the database
        List<UserBookLending> userBookLendingList = userBookLendingRepository.findAll();
        assertThat(userBookLendingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserBookLending() throws Exception {
        // Initialize the database
        userBookLendingRepository.saveAndFlush(userBookLending);

        int databaseSizeBeforeDelete = userBookLendingRepository.findAll().size();

        // Delete the userBookLending
        restUserBookLendingMockMvc
            .perform(delete(ENTITY_API_URL_ID, userBookLending.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserBookLending> userBookLendingList = userBookLendingRepository.findAll();
        assertThat(userBookLendingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
