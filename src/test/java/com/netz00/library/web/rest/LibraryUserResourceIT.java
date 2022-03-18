package com.netz00.library.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.netz00.library.IntegrationTest;
import com.netz00.library.domain.LibraryUser;
import com.netz00.library.repository.LibraryUserRepository;
import com.netz00.library.service.dto.LibraryUserDTO;
import com.netz00.library.service.mapper.LibraryUserMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link LibraryUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LibraryUserResourceIT {

    private static final String DEFAULT_FULLNAME = "AAAAAAAAAA";
    private static final String UPDATED_FULLNAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTHDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTHDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MEMEBERDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MEMEBERDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_EMAIL = "ju$4@):\"tr.',vH,";
    private static final String UPDATED_EMAIL = "prrt@T%N$.6iAYT\\";

    private static final String DEFAULT_MOBILE = "+0 7144876716";
    private static final String UPDATED_MOBILE = "4488181402";

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/library-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LibraryUserRepository libraryUserRepository;

    @Autowired
    private LibraryUserMapper libraryUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLibraryUserMockMvc;

    private LibraryUser libraryUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LibraryUser createEntity(EntityManager em) {
        LibraryUser libraryUser = new LibraryUser()
            .fullname(DEFAULT_FULLNAME)
            .birthdate(DEFAULT_BIRTHDATE)
            .memeberdate(DEFAULT_MEMEBERDATE)
            .email(DEFAULT_EMAIL)
            .mobile(DEFAULT_MOBILE)
            .adress(DEFAULT_ADRESS)
            .note(DEFAULT_NOTE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return libraryUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LibraryUser createUpdatedEntity(EntityManager em) {
        LibraryUser libraryUser = new LibraryUser()
            .fullname(UPDATED_FULLNAME)
            .birthdate(UPDATED_BIRTHDATE)
            .memeberdate(UPDATED_MEMEBERDATE)
            .email(UPDATED_EMAIL)
            .mobile(UPDATED_MOBILE)
            .adress(UPDATED_ADRESS)
            .note(UPDATED_NOTE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return libraryUser;
    }

    @BeforeEach
    public void initTest() {
        libraryUser = createEntity(em);
    }

    @Test
    @Transactional
    void createLibraryUser() throws Exception {
        int databaseSizeBeforeCreate = libraryUserRepository.findAll().size();
        // Create the LibraryUser
        LibraryUserDTO libraryUserDTO = libraryUserMapper.toDto(libraryUser);
        restLibraryUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libraryUserDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LibraryUser in the database
        List<LibraryUser> libraryUserList = libraryUserRepository.findAll();
        assertThat(libraryUserList).hasSize(databaseSizeBeforeCreate + 1);
        LibraryUser testLibraryUser = libraryUserList.get(libraryUserList.size() - 1);
        assertThat(testLibraryUser.getFullname()).isEqualTo(DEFAULT_FULLNAME);
        assertThat(testLibraryUser.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
        assertThat(testLibraryUser.getMemeberdate()).isEqualTo(DEFAULT_MEMEBERDATE);
        assertThat(testLibraryUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testLibraryUser.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testLibraryUser.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testLibraryUser.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testLibraryUser.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testLibraryUser.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createLibraryUserWithExistingId() throws Exception {
        // Create the LibraryUser with an existing ID
        libraryUser.setId(1L);
        LibraryUserDTO libraryUserDTO = libraryUserMapper.toDto(libraryUser);

        int databaseSizeBeforeCreate = libraryUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLibraryUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libraryUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LibraryUser in the database
        List<LibraryUser> libraryUserList = libraryUserRepository.findAll();
        assertThat(libraryUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFullnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = libraryUserRepository.findAll().size();
        // set the field null
        libraryUser.setFullname(null);

        // Create the LibraryUser, which fails.
        LibraryUserDTO libraryUserDTO = libraryUserMapper.toDto(libraryUser);

        restLibraryUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libraryUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<LibraryUser> libraryUserList = libraryUserRepository.findAll();
        assertThat(libraryUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBirthdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = libraryUserRepository.findAll().size();
        // set the field null
        libraryUser.setBirthdate(null);

        // Create the LibraryUser, which fails.
        LibraryUserDTO libraryUserDTO = libraryUserMapper.toDto(libraryUser);

        restLibraryUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libraryUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<LibraryUser> libraryUserList = libraryUserRepository.findAll();
        assertThat(libraryUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMemeberdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = libraryUserRepository.findAll().size();
        // set the field null
        libraryUser.setMemeberdate(null);

        // Create the LibraryUser, which fails.
        LibraryUserDTO libraryUserDTO = libraryUserMapper.toDto(libraryUser);

        restLibraryUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libraryUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<LibraryUser> libraryUserList = libraryUserRepository.findAll();
        assertThat(libraryUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = libraryUserRepository.findAll().size();
        // set the field null
        libraryUser.setEmail(null);

        // Create the LibraryUser, which fails.
        LibraryUserDTO libraryUserDTO = libraryUserMapper.toDto(libraryUser);

        restLibraryUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libraryUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<LibraryUser> libraryUserList = libraryUserRepository.findAll();
        assertThat(libraryUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLibraryUsers() throws Exception {
        // Initialize the database
        libraryUserRepository.saveAndFlush(libraryUser);

        // Get all the libraryUserList
        restLibraryUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(libraryUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullname").value(hasItem(DEFAULT_FULLNAME)))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
            .andExpect(jsonPath("$.[*].memeberdate").value(hasItem(DEFAULT_MEMEBERDATE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getLibraryUser() throws Exception {
        // Initialize the database
        libraryUserRepository.saveAndFlush(libraryUser);

        // Get the libraryUser
        restLibraryUserMockMvc
            .perform(get(ENTITY_API_URL_ID, libraryUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(libraryUser.getId().intValue()))
            .andExpect(jsonPath("$.fullname").value(DEFAULT_FULLNAME))
            .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()))
            .andExpect(jsonPath("$.memeberdate").value(DEFAULT_MEMEBERDATE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingLibraryUser() throws Exception {
        // Get the libraryUser
        restLibraryUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLibraryUser() throws Exception {
        // Initialize the database
        libraryUserRepository.saveAndFlush(libraryUser);

        int databaseSizeBeforeUpdate = libraryUserRepository.findAll().size();

        // Update the libraryUser
        LibraryUser updatedLibraryUser = libraryUserRepository.findById(libraryUser.getId()).get();
        // Disconnect from session so that the updates on updatedLibraryUser are not directly saved in db
        em.detach(updatedLibraryUser);
        updatedLibraryUser
            .fullname(UPDATED_FULLNAME)
            .birthdate(UPDATED_BIRTHDATE)
            .memeberdate(UPDATED_MEMEBERDATE)
            .email(UPDATED_EMAIL)
            .mobile(UPDATED_MOBILE)
            .adress(UPDATED_ADRESS)
            .note(UPDATED_NOTE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        LibraryUserDTO libraryUserDTO = libraryUserMapper.toDto(updatedLibraryUser);

        restLibraryUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, libraryUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(libraryUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the LibraryUser in the database
        List<LibraryUser> libraryUserList = libraryUserRepository.findAll();
        assertThat(libraryUserList).hasSize(databaseSizeBeforeUpdate);
        LibraryUser testLibraryUser = libraryUserList.get(libraryUserList.size() - 1);
        assertThat(testLibraryUser.getFullname()).isEqualTo(UPDATED_FULLNAME);
        assertThat(testLibraryUser.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testLibraryUser.getMemeberdate()).isEqualTo(UPDATED_MEMEBERDATE);
        assertThat(testLibraryUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testLibraryUser.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testLibraryUser.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testLibraryUser.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testLibraryUser.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testLibraryUser.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingLibraryUser() throws Exception {
        int databaseSizeBeforeUpdate = libraryUserRepository.findAll().size();
        libraryUser.setId(count.incrementAndGet());

        // Create the LibraryUser
        LibraryUserDTO libraryUserDTO = libraryUserMapper.toDto(libraryUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibraryUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, libraryUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(libraryUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LibraryUser in the database
        List<LibraryUser> libraryUserList = libraryUserRepository.findAll();
        assertThat(libraryUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLibraryUser() throws Exception {
        int databaseSizeBeforeUpdate = libraryUserRepository.findAll().size();
        libraryUser.setId(count.incrementAndGet());

        // Create the LibraryUser
        LibraryUserDTO libraryUserDTO = libraryUserMapper.toDto(libraryUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibraryUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(libraryUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LibraryUser in the database
        List<LibraryUser> libraryUserList = libraryUserRepository.findAll();
        assertThat(libraryUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLibraryUser() throws Exception {
        int databaseSizeBeforeUpdate = libraryUserRepository.findAll().size();
        libraryUser.setId(count.incrementAndGet());

        // Create the LibraryUser
        LibraryUserDTO libraryUserDTO = libraryUserMapper.toDto(libraryUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibraryUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libraryUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LibraryUser in the database
        List<LibraryUser> libraryUserList = libraryUserRepository.findAll();
        assertThat(libraryUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLibraryUserWithPatch() throws Exception {
        // Initialize the database
        libraryUserRepository.saveAndFlush(libraryUser);

        int databaseSizeBeforeUpdate = libraryUserRepository.findAll().size();

        // Update the libraryUser using partial update
        LibraryUser partialUpdatedLibraryUser = new LibraryUser();
        partialUpdatedLibraryUser.setId(libraryUser.getId());

        partialUpdatedLibraryUser
            .fullname(UPDATED_FULLNAME)
            .birthdate(UPDATED_BIRTHDATE)
            .memeberdate(UPDATED_MEMEBERDATE)
            .note(UPDATED_NOTE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restLibraryUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLibraryUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLibraryUser))
            )
            .andExpect(status().isOk());

        // Validate the LibraryUser in the database
        List<LibraryUser> libraryUserList = libraryUserRepository.findAll();
        assertThat(libraryUserList).hasSize(databaseSizeBeforeUpdate);
        LibraryUser testLibraryUser = libraryUserList.get(libraryUserList.size() - 1);
        assertThat(testLibraryUser.getFullname()).isEqualTo(UPDATED_FULLNAME);
        assertThat(testLibraryUser.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testLibraryUser.getMemeberdate()).isEqualTo(UPDATED_MEMEBERDATE);
        assertThat(testLibraryUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testLibraryUser.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testLibraryUser.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testLibraryUser.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testLibraryUser.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testLibraryUser.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateLibraryUserWithPatch() throws Exception {
        // Initialize the database
        libraryUserRepository.saveAndFlush(libraryUser);

        int databaseSizeBeforeUpdate = libraryUserRepository.findAll().size();

        // Update the libraryUser using partial update
        LibraryUser partialUpdatedLibraryUser = new LibraryUser();
        partialUpdatedLibraryUser.setId(libraryUser.getId());

        partialUpdatedLibraryUser
            .fullname(UPDATED_FULLNAME)
            .birthdate(UPDATED_BIRTHDATE)
            .memeberdate(UPDATED_MEMEBERDATE)
            .email(UPDATED_EMAIL)
            .mobile(UPDATED_MOBILE)
            .adress(UPDATED_ADRESS)
            .note(UPDATED_NOTE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restLibraryUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLibraryUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLibraryUser))
            )
            .andExpect(status().isOk());

        // Validate the LibraryUser in the database
        List<LibraryUser> libraryUserList = libraryUserRepository.findAll();
        assertThat(libraryUserList).hasSize(databaseSizeBeforeUpdate);
        LibraryUser testLibraryUser = libraryUserList.get(libraryUserList.size() - 1);
        assertThat(testLibraryUser.getFullname()).isEqualTo(UPDATED_FULLNAME);
        assertThat(testLibraryUser.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testLibraryUser.getMemeberdate()).isEqualTo(UPDATED_MEMEBERDATE);
        assertThat(testLibraryUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testLibraryUser.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testLibraryUser.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testLibraryUser.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testLibraryUser.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testLibraryUser.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingLibraryUser() throws Exception {
        int databaseSizeBeforeUpdate = libraryUserRepository.findAll().size();
        libraryUser.setId(count.incrementAndGet());

        // Create the LibraryUser
        LibraryUserDTO libraryUserDTO = libraryUserMapper.toDto(libraryUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibraryUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, libraryUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(libraryUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LibraryUser in the database
        List<LibraryUser> libraryUserList = libraryUserRepository.findAll();
        assertThat(libraryUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLibraryUser() throws Exception {
        int databaseSizeBeforeUpdate = libraryUserRepository.findAll().size();
        libraryUser.setId(count.incrementAndGet());

        // Create the LibraryUser
        LibraryUserDTO libraryUserDTO = libraryUserMapper.toDto(libraryUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibraryUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(libraryUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LibraryUser in the database
        List<LibraryUser> libraryUserList = libraryUserRepository.findAll();
        assertThat(libraryUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLibraryUser() throws Exception {
        int databaseSizeBeforeUpdate = libraryUserRepository.findAll().size();
        libraryUser.setId(count.incrementAndGet());

        // Create the LibraryUser
        LibraryUserDTO libraryUserDTO = libraryUserMapper.toDto(libraryUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibraryUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(libraryUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LibraryUser in the database
        List<LibraryUser> libraryUserList = libraryUserRepository.findAll();
        assertThat(libraryUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLibraryUser() throws Exception {
        // Initialize the database
        libraryUserRepository.saveAndFlush(libraryUser);

        int databaseSizeBeforeDelete = libraryUserRepository.findAll().size();

        // Delete the libraryUser
        restLibraryUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, libraryUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LibraryUser> libraryUserList = libraryUserRepository.findAll();
        assertThat(libraryUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
