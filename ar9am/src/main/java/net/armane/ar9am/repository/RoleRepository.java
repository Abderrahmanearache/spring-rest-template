package net.armane.ar9am.repository;

import net.armane.ar9am.Security.account.Role;
import net.armane.ar9am.Security.account.RoleValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

     Role  findByRole(RoleValue roleValue);

}
