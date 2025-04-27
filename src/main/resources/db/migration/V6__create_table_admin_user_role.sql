CREATE TABLE admin_user_role (
    id_admin BIGINT NOT NULL,
    id_role BIGINT NOT NULL,
    PRIMARY KEY (id_admin, id_role),
    CONSTRAINT admin_user_role_admin_fk FOREIGN KEY (id_admin) REFERENCES admin_user(id_admin),
    CONSTRAINT admin_user_role_role_fk FOREIGN KEY (id_role) REFERENCES admin_role(id)
);
