package io.cloudmobility.tiago.domain.model;


import java.util.List;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import io.cloudmobility.tiago.security.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class User {

    @NotNull
    @Column
    protected String name;

    @Transient
    private List<UserRole> roles;
}
