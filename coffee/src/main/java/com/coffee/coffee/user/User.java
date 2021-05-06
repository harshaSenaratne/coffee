package com.coffee.coffee.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class User {

 @Id
 @GeneratedValue
 private long Id;

 private String username;
 
 private String displayName;
 
 private String password;
 

}
