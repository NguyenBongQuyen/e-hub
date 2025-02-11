package com.ehub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_role")
public class RoleEntity extends AuditableEntity<Integer> {
    private String name;
    private String description;

    @OneToMany(mappedBy = "role")
    private Set<RoleHasPermissionEntity> permissions = new HashSet<>();

    @OneToMany(mappedBy = "role")
    private Set<UserHasRoleEntity> users = new HashSet<>();
}
