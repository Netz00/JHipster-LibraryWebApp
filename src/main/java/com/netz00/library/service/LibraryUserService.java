package com.netz00.library.service;

import com.netz00.library.domain.LibraryUser;
import com.netz00.library.repository.LibraryUserRepository;
import com.netz00.library.service.dto.LibraryUserDTO;
import com.netz00.library.service.mapper.LibraryUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LibraryUser}.
 */
@Service
@Transactional
public class LibraryUserService {

    private final Logger log = LoggerFactory.getLogger(LibraryUserService.class);

    private final LibraryUserRepository libraryUserRepository;

    private final LibraryUserMapper libraryUserMapper;

    public LibraryUserService(LibraryUserRepository libraryUserRepository, LibraryUserMapper libraryUserMapper) {
        this.libraryUserRepository = libraryUserRepository;
        this.libraryUserMapper = libraryUserMapper;
    }

    /**
     * Save a libraryUser.
     *
     * @param libraryUserDTO the entity to save.
     * @return the persisted entity.
     */
    public LibraryUserDTO save(LibraryUserDTO libraryUserDTO) {
        log.debug("Request to save LibraryUser : {}", libraryUserDTO);
        LibraryUser libraryUser = libraryUserMapper.toEntity(libraryUserDTO);
        libraryUser = libraryUserRepository.save(libraryUser);
        return libraryUserMapper.toDto(libraryUser);
    }

    /**
     * Partially update a libraryUser.
     *
     * @param libraryUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LibraryUserDTO> partialUpdate(LibraryUserDTO libraryUserDTO) {
        log.debug("Request to partially update LibraryUser : {}", libraryUserDTO);

        return libraryUserRepository
            .findById(libraryUserDTO.getId())
            .map(existingLibraryUser -> {
                libraryUserMapper.partialUpdate(existingLibraryUser, libraryUserDTO);

                return existingLibraryUser;
            })
            .map(libraryUserRepository::save)
            .map(libraryUserMapper::toDto);
    }

    /**
     * Get all the libraryUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LibraryUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LibraryUsers");
        return libraryUserRepository.findAll(pageable).map(libraryUserMapper::toDto);
    }

    /**
     * Get one libraryUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LibraryUserDTO> findOne(Long id) {
        log.debug("Request to get LibraryUser : {}", id);
        return libraryUserRepository.findById(id).map(libraryUserMapper::toDto);
    }

    /**
     * Delete the libraryUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LibraryUser : {}", id);
        libraryUserRepository.deleteById(id);
    }
}
