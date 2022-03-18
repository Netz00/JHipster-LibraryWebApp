package com.netz00.library.service.mapper;

import com.netz00.library.domain.Genre;
import com.netz00.library.service.dto.GenreDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Genre} and its DTO {@link GenreDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GenreMapper extends EntityMapper<GenreDTO, Genre> {
    @Named("nameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    Set<GenreDTO> toDtoNameSet(Set<Genre> genre);
}
