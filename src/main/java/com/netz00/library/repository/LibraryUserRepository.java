package com.netz00.library.repository;

import com.netz00.library.domain.LibraryUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LibraryUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LibraryUserRepository extends JpaRepository<LibraryUser, Long> {}
