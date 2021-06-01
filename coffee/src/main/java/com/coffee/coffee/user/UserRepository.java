package com.coffee.coffee.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    
//User findByUsernameContaining(String username,String displayName);    

}
