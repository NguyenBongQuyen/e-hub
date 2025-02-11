package com.ehub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "tbl_group")
public class GroupEntity extends AuditableEntity<Integer> {
    private String name;
    private String description;

    @OneToOne
    private RoleEntity role;

    @OneToMany(mappedBy = "group")
    private Set<GroupHasUserEntity> users = new HashSet<>();
}
