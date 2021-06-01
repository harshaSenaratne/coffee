package com.coffee.coffee.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
@Entity
//@Table(uniqueConstraints= @UniqueConstraint(columnNames = "username"))
public class User {

 @Id
 @GeneratedValue
 private long Id;

 @NotNull
 @Size(min=4,max = 255)
 private String username;
 
 @NotNull
 @Size(min=4,max = 255)
 private String displayName;
 
 @NotNull
 @Size(min=8,max = 255)
 //@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d).*$")
 private String password;
 

}
