package com.netz00.library.service.mapper;

import com.netz00.library.domain.LibraryUser;
import com.netz00.library.service.dto.LibraryUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LibraryUser} and its DTO {@link LibraryUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface LibraryUserMapper extends EntityMapper<LibraryUserDTO, LibraryUser> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    LibraryUserDTO toDto(LibraryUser s);

    @Named("fullname")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullname", source = "fullname")
    LibraryUserDTO toDtoFullname(LibraryUser libraryUser);
}
