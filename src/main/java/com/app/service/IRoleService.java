package com.app.service;

import com.app.model.Role;
import com.app.model.RoleEnum;

import java.util.Optional;

public interface IRoleService {
    Role findByRoleEnum(RoleEnum roleEnum);
}
