package com.app.service;

import com.app.model.Role;
import com.app.model.RoleEnum;
import com.app.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{

    private final IRoleRepository roleRepository;

    @Override
    public Role findByRoleEnum(RoleEnum roleEnum) {
        return roleRepository.findByRoleEnum(roleEnum);
    }
}
