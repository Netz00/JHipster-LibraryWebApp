package com.netz00.library.web.rest;

import com.netz00.library.repository.UserBookLendingRepository;
import com.netz00.library.service.UserBookLendingService;
import com.netz00.library.service.dto.UserBookLendingDTO;
import com.netz00.library.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.netz00.library.domain.UserBookLending}.
 */
@RestController
@RequestMapping("/api")
public class UserBookLendingResource {

    private final Logger log = LoggerFactory.getLogger(UserBookLendingResource.class);

    private static final String ENTITY_NAME = "userBookLending";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserBookLendingService userBookLendingService;

    private final UserBookLendingRepository userBookLendingRepository;

    public UserBookLendingResource(UserBookLendingService userBookLendingService, UserBookLendingRepository userBookLendingRepository) {
        this.userBookLendingService = userBookLendingService;
        this.userBookLendingRepository = userBookLendingRepository;
    }

    /**
     * {@code POST  /user-book-lendings} : Create a new userBookLending.
     *
     * @param userBookLendingDTO the userBookLendingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userBookLendingDTO, or with status {@code 400 (Bad Request)} if the userBookLending has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-book-lendings")
    public ResponseEntity<UserBookLendingDTO> createUserBookLending(@Valid @RequestBody UserBookLendingDTO userBookLendingDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserBookLending : {}", userBookLendingDTO);
        if (userBookLendingDTO.getId() != null) {
            throw new BadRequestAlertException("A new userBookLending cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserBookLendingDTO result = userBookLendingService.save(userBookLendingDTO);
        return ResponseEntity
            .created(new URI("/api/user-book-lendings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-book-lendings/:id} : Updates an existing userBookLending.
     *
     * @param id the id of the userBookLendingDTO to save.
     * @param userBookLendingDTO the userBookLendingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userBookLendingDTO,
     * or with status {@code 400 (Bad Request)} if the userBookLendingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userBookLendingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-book-lendings/{id}")
    public ResponseEntity<UserBookLendingDTO> updateUserBookLending(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserBookLendingDTO userBookLendingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserBookLending : {}, {}", id, userBookLendingDTO);
        if (userBookLendingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userBookLendingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userBookLendingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserBookLendingDTO result = userBookLendingService.save(userBookLendingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userBookLendingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-book-lendings/:id} : Partial updates given fields of an existing userBookLending, field will ignore if it is null
     *
     * @param id the id of the userBookLendingDTO to save.
     * @param userBookLendingDTO the userBookLendingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userBookLendingDTO,
     * or with status {@code 400 (Bad Request)} if the userBookLendingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userBookLendingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userBookLendingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-book-lendings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserBookLendingDTO> partialUpdateUserBookLending(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserBookLendingDTO userBookLendingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserBookLending partially : {}, {}", id, userBookLendingDTO);
        if (userBookLendingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userBookLendingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userBookLendingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserBookLendingDTO> result = userBookLendingService.partialUpdate(userBookLendingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userBookLendingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-book-lendings} : get all the userBookLendings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userBookLendings in body.
     */
    @GetMapping("/user-book-lendings")
    public ResponseEntity<List<UserBookLendingDTO>> getAllUserBookLendings(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of UserBookLendings");
        Page<UserBookLendingDTO> page = userBookLendingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-book-lendings/:id} : get the "id" userBookLending.
     *
     * @param id the id of the userBookLendingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userBookLendingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-book-lendings/{id}")
    public ResponseEntity<UserBookLendingDTO> getUserBookLending(@PathVariable Long id) {
        log.debug("REST request to get UserBookLending : {}", id);
        Optional<UserBookLendingDTO> userBookLendingDTO = userBookLendingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userBookLendingDTO);
    }

    /**
     * {@code DELETE  /user-book-lendings/:id} : delete the "id" userBookLending.
     *
     * @param id the id of the userBookLendingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-book-lendings/{id}")
    public ResponseEntity<Void> deleteUserBookLending(@PathVariable Long id) {
        log.debug("REST request to delete UserBookLending : {}", id);
        userBookLendingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
