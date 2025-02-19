package com.sneakysquid.authenticator.transform;

import com.sneakysquid.authenticator.domain.Group;
import com.sneakysquid.authenticator.domain.dto.GroupDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupDto toDto(Group entity);
    Group toEntity(GroupDto dto);
}
