package com.sneakysquid.authenticator.transform;

import com.sneakysquid.authenticator.domain.User;
import com.sneakysquid.authenticator.domain.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authorities", source = "authorities", qualifiedByName = "mapAuthorities")
    UserDto toDto(User entity);

    @Mapping(target = "authorities", ignore = true)
    User toEntity(UserDto dto);

    List<UserDto> toDtoList(List<User> entities);

    List<User> toEntityList(List<UserDto> dtos);

    @Named("mapAuthorities")
    default List<String> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) {
            return null;
        }
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
