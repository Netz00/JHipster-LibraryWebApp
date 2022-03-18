package com.netz00.library.web.rest;

import com.netz00.library.repository.LibraryUserRepository;
import com.netz00.library.service.LibraryUserService;
import com.netz00.library.service.dto.LibraryUserDTO;
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
 * REST controller for managing {@link com.netz00.library.domain.LibraryUser}.
 */
@RestController
@RequestMapping("/api")
public class LibraryUserResource {

    private final Logger log = LoggerFactory.getLogger(LibraryUserResource.class);

    private static final String ENTITY_NAME = "libraryUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LibraryUserService libraryUserService;

    private final LibraryUserRepository libraryUserRepository;

    public LibraryUserResource(LibraryUserService libraryUserService, LibraryUserRepository libraryUserRepository) {
        this.libraryUserService = libraryUserService;
        this.libraryUserRepository = libraryUserRepository;
    }

    /**
     * {@code POST  /library-users} : Create a new libraryUser.
     *
     * @param libraryUserDTO the libraryUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new libraryUserDTO, or with status {@code 400 (Bad Request)} if the libraryUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/library-users")
    public ResponseEntity<LibraryUserDTO> createLibraryUser(@Valid @RequestBody LibraryUserDTO libraryUserDTO) throws URISyntaxException {
        log.debug("REST request to save LibraryUser : {}", libraryUserDTO);
        if (libraryUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new libraryUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LibraryUserDTO result = libraryUserService.save(libraryUserDTO);
        return ResponseEntity
            .created(new URI("/api/library-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /library-users/:id} : Updates an existing libraryUser.
     *
     * @param id the id of the libraryUserDTO to save.
     * @param libraryUserDTO the libraryUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated libraryUserDTO,
     * or with status {@code 400 (Bad Request)} if the libraryUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the libraryUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/library-users/{id}")
    public ResponseEntity<LibraryUserDTO> updateLibraryUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LibraryUserDTO libraryUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LibraryUser : {}, {}", id, libraryUserDTO);
        if (libraryUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, libraryUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!libraryUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LibraryUserDTO result = libraryUserService.save(libraryUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, libraryUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /library-users/:id} : Partial updates given fields of an existing libraryUser, field will ignore if it is null
     *
     * @param id the id of the libraryUserDTO to save.
     * @param libraryUserDTO the libraryUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated libraryUserDTO,
     * or with status {@code 400 (Bad Request)} if the libraryUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the libraryUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the libraryUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/library-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LibraryUserDTO> partialUpdateLibraryUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LibraryUserDTO libraryUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LibraryUser partially : {}, {}", id, libraryUserDTO);
        if (libraryUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, libraryUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!libraryUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LibraryUserDTO> result = libraryUserService.partialUpdate(libraryUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, libraryUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /library-users} : get all the libraryUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of libraryUsers in body.
     */
    @GetMapping("/library-users")
    public ResponseEntity<List<LibraryUserDTO>> getAllLibraryUsers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LibraryUsers");
        Page<LibraryUserDTO> page = libraryUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /library-users/:id} : get the "id" libraryUser.
     *
     * @param id the id of the libraryUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the libraryUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/library-users/{id}")
    public ResponseEntity<LibraryUserDTO> getLibraryUser(@PathVariable Long id) {
        log.debug("REST request to get LibraryUser : {}", id);
        Optional<LibraryUserDTO> libraryUserDTO = libraryUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(libraryUserDTO);
    }

    /**
     * {@code DELETE  /library-users/:id} : delete the "id" libraryUser.
     *
     * @param id the id of the libraryUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/library-users/{id}")
    public ResponseEntity<Void> deleteLibraryUser(@PathVariable Long id) {
        log.debug("REST request to delete LibraryUser : {}", id);
        libraryUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
