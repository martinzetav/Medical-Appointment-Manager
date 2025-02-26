package com.app;

import com.app.model.Permission;
import com.app.model.Role;
import com.app.model.RoleEnum;
import com.app.model.UserEntity;
import com.app.repository.IRoleRepository;
import com.app.repository.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class MedicalAppointmentManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalAppointmentManagerApplication.class, args);
	}

	// REEMPLAZAR POR FLYWAY
	@Bean
	CommandLineRunner init(IRoleRepository roleRepository) {
		return args -> {

			/* CREATE PERMISSION */
			Permission createPermission = Permission.builder()
					.name("CREATE")
					.build();

			Permission readPermission = Permission.builder()
					.name("READ")
					.build();

			Permission updatePermission = Permission.builder()
					.name("UPDATE")
					.build();

			Permission deletePermission = Permission.builder()
					.name("DELETE")
					.build();


			/* CREATE ROLES */
			Role roleAdmin = Role.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission))
					.build();

			Role roleUser = Role.builder()
					.roleEnum(RoleEnum.USER)
					.permissionList(Set.of(createPermission, readPermission))
					.build();

			roleRepository.saveAll(List.of(roleAdmin, roleUser));


//			/* CREATE USERS */
//			UserEntity userMartin = UserEntity.builder()
//					.username("martin")
//					.password("$2a$10$2MCdfZqx3u3tm19MzkR5p.qaHGE9PgxcP2LQ/l.XdHWtE7XNp6I26")
//					.isEnabled(true)
//					.accountNoExpired(true)
//					.accountNoLocked(true)
//					.credentialNoExpired(true)
//					.role(roleAdmin)
//					.build();
//
//			UserEntity userDaniel = UserEntity.builder()
//					.username("daniel")
//					.password("$2a$10$2MCdfZqx3u3tm19MzkR5p.qaHGE9PgxcP2LQ/l.XdHWtE7XNp6I26")
//					.isEnabled(true)
//					.accountNoExpired(true)
//					.accountNoLocked(true)
//					.credentialNoExpired(true)
//					.role(roleUser)
//					.build();
//
//			userRepository.saveAll(List.of(userMartin, userDaniel));
		};


	}
}