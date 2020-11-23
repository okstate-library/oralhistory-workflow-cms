package com.okstatelibrary.oralhistoryworkflowcms.repository;

import org.springframework.data.repository.CrudRepository;

import com.okstatelibrary.oralhistoryworkflowcms.security.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    Role findByName(String name);
}