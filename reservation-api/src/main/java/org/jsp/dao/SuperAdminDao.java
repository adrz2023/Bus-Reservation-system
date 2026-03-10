package org.jsp.dao;

import org.jsp.model.SuperAdmin;
import org.jsp.repository.SuperAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public class SuperAdminDao {

    @Autowired
    private SuperAdminRepository superAdminRepository;

    public SuperAdmin save (SuperAdmin superAdmin){
        return superAdminRepository.save(superAdmin);
    }

    public Optional<SuperAdmin> verify (String email, String password){
        return superAdminRepository.findByEmailAndPassword(email,password);
    }
}
