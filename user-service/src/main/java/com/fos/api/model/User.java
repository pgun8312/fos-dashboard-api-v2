package com.fos.api.model;

import com.fos.api.common.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "User Sub is required")
    @Column(unique = true)
    private String userSub;
    @NotNull(message = "User name is required")
    private String userName;
    @NotNull(message = "name is required")
    private String name;
    @NotNull(message = "phone number is required")
    private String phone;
    @NotNull(message = "email is required")
    @Email(message = "incorrect email format")
    private String email;
    private Constants.UserStatus status;
    private Constants.UserRole role;
    private Long createdDate;
    private Long modifiedDate;
    @OneToOne(mappedBy = "user")
    private UserProfile userProfile;
}
