package com.app.service.impl;

import com.app.model.Role;
import com.app.model.RoleEnum;
import com.app.repository.IRoleRepository;
import com.app.service.interfaces.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final IRoleRepository roleRepository;

    @Override
    public Role findByRoleEnum(RoleEnum roleEnum) {
        return roleRepository.findByRoleEnum(roleEnum);
    }
}
