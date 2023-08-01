package com.cafe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(
        name = "User.findByEmailId",
        query = "select u from User u where u.email=:email"
)
@NamedQuery(
        name = "User.getAllUser",
        query = "select new com.cafe.wrapper.UserWrapper(u.id, u.name, u.email, u.contactNumber, u.status) from User u where u.role='user'"
)
@NamedQuery(
        name = "User.updateUserStatus",
        query = "update User u set u.status=:status where u.id=:id"
)
@NamedQuery(
        name = "User.getAllAdmin",
        query = "select u.email from User u where u.role='admin'"
)

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "_user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    private String name;
    private String contactNumber;
    private String email;
    private String password;
    private String status;
    private String role;
}
