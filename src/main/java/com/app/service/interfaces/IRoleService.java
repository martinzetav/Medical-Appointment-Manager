package com.app.service.interfaces;

import com.app.model.Role;
import com.app.model.RoleEnum;

public interface IRoleService {
    Role findByRoleEnum(RoleEnum roleEnum);
}
