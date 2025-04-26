CREATE TABLE admin_user_role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_admin INT NOT NULL,
    id_role INT NOT NULL,
    FOREIGN KEY (id_admin) REFERENCES admin_user(id_admin),
    FOREIGN KEY (id_role) REFERENCES admin_role(id_role)
);