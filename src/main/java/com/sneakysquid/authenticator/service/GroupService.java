package com.sneakysquid.authenticator.service;

import com.sneakysquid.authenticator.domain.dto.GroupDto;
import com.sneakysquid.authenticator.repository.GroupRepository;
import com.sneakysquid.authenticator.transform.GroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public GroupDto getByName(String name) {
        return groupMapper.toDto(groupRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found")));
    }
}
