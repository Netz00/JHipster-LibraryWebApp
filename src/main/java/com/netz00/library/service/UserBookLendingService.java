package com.netz00.library.service;

import com.netz00.library.domain.UserBookLending;
import com.netz00.library.repository.UserBookLendingRepository;
import com.netz00.library.service.dto.UserBookLendingDTO;
import com.netz00.library.service.mapper.UserBookLendingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserBookLending}.
 */
@Service
@Transactional
public class UserBookLendingService {

    private final Logger log = LoggerFactory.getLogger(UserBookLendingService.class);

    private final UserBookLendingRepository userBookLendingRepository;

    private final UserBookLendingMapper userBookLendingMapper;

    public UserBookLendingService(UserBookLendingRepository userBookLendingRepository, UserBookLendingMapper userBookLendingMapper) {
        this.userBookLendingRepository = userBookLendingRepository;
        this.userBookLendingMapper = userBookLendingMapper;
    }

    /**
     * Save a userBookLending.
     *
     * @param userBookLendingDTO the entity to save.
     * @return the persisted entity.
     */
    public UserBookLendingDTO save(UserBookLendingDTO userBookLendingDTO) {
        log.debug("Request to save UserBookLending : {}", userBookLendingDTO);
        UserBookLending userBookLending = userBookLendingMapper.toEntity(userBookLendingDTO);
        userBookLending = userBookLendingRepository.save(userBookLending);
        return userBookLendingMapper.toDto(userBookLending);
    }

    /**
     * Partially update a userBookLending.
     *
     * @param userBookLendingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserBookLendingDTO> partialUpdate(UserBookLendingDTO userBookLendingDTO) {
        log.debug("Request to partially update UserBookLending : {}", userBookLendingDTO);

        return userBookLendingRepository
            .findById(userBookLendingDTO.getId())
            .map(existingUserBookLending -> {
                userBookLendingMapper.partialUpdate(existingUserBookLending, userBookLendingDTO);

                return existingUserBookLending;
            })
            .map(userBookLendingRepository::save)
            .map(userBookLendingMapper::toDto);
    }

    /**
     * Get all the userBookLendings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserBookLendingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserBookLendings");
        return userBookLendingRepository.findAll(pageable).map(userBookLendingMapper::toDto);
    }

    /**
     * Get one userBookLending by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserBookLendingDTO> findOne(Long id) {
        log.debug("Request to get UserBookLending : {}", id);
        return userBookLendingRepository.findById(id).map(userBookLendingMapper::toDto);
    }

    /**
     * Delete the userBookLending by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserBookLending : {}", id);
        userBookLendingRepository.deleteById(id);
    }
}
