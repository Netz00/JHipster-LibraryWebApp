package com.netz00.library.service.mapper;

import com.netz00.library.domain.UserBookLending;
import com.netz00.library.service.dto.UserBookLendingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserBookLending} and its DTO {@link UserBookLendingDTO}.
 */
@Mapper(componentModel = "spring", uses = { LibraryUserMapper.class, BookMapper.class })
public interface UserBookLendingMapper extends EntityMapper<UserBookLendingDTO, UserBookLending> {
    @Mapping(target = "user", source = "user", qualifiedByName = "fullname")
    @Mapping(target = "book", source = "book", qualifiedByName = "title")
    UserBookLendingDTO toDto(UserBookLending s);
}
