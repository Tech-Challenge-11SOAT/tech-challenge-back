package br.com.postech.techchallange.adapter.out.persistence.entity.admin;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admin_user_role")
@Getter
@Setter
@NoArgsConstructor
public class AdminUserRoleEntity {

    @EmbeddedId
    private AdminUserRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idAdmin")
    @JoinColumn(name = "id_admin")
    private AdminUserEntity adminUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idRole")
    @JoinColumn(name = "id_role")
    private AdminRoleEntity role;
}
