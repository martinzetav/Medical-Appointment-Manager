package com.app.service;

import com.app.dto.AuthLoginRequestDTO;
import com.app.dto.AuthResponseDTO;
import com.app.dto.RegisterUserDTO;
import com.app.model.Role;
import com.app.model.RoleEnum;
import com.app.model.UserEntity;
import com.app.repository.IUserRepository;
import com.app.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final IUserRepository userRepository;
    private final IRoleService roleService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("The user " + username + " does not exist."));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(userEntity.getRole().getRoleEnum().name())));

        userEntity.getRole().getPermissionList()
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                authorityList
                );
    }

    public AuthResponseDTO loginUser(AuthLoginRequestDTO userRequest) {
        String username = userRequest.username();
        String password = userRequest.password();

        Authentication authentication = this.authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        AuthResponseDTO authResponseDTO = new AuthResponseDTO(username, "User loged successfully.", accessToken, true);
        return authResponseDTO;
    }

    public Authentication authenticate(String username, String password){
        UserDetails userDetails = this.loadUserByUsername(username);

        if(userDetails == null){
            throw new BadCredentialsException("Invalid username or password.");
        }

        if(!this.passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid username or password.");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public RegisterUserDTO signUpUser(RegisterUserDTO userRequest) {
        Role role = this.roleService.findByRoleEnum(RoleEnum.USER); // rol por defecto

        UserEntity user = UserEntity.builder()
                .username(userRequest.username())
                .email(userRequest.email())
                .password(this.passwordEncoder.encode(userRequest.password()))
                .role(role)
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .build();

        UserEntity userCreated = this.userRepository.save(user);
        RegisterUserDTO responseDTO = new RegisterUserDTO(
                userCreated.getUsername(),
                userCreated.getEmail(),
                this.passwordEncoder.encode(userCreated.getPassword())
        );

        return responseDTO;
    }
}
