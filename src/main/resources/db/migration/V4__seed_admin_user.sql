-- =============================================================
-- V4 - Seed default ADMIN user
-- password: admin123  (BCrypt strength 10)
-- =============================================================

INSERT INTO users (username, password, full_name, email, status)
SELECT 'admin',
       '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.',
       'System Administrator',
       'hoangtanh21102002@gmail.com',
       1
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO user_roles (user_id, role_id)
SELECT u.user_id, r.role_id
FROM users u, roles r
WHERE u.username = 'admin'
  AND r.role_name = 'ADMIN'
  AND NOT EXISTS (
      SELECT 1 FROM user_roles ur
      WHERE ur.user_id = u.user_id AND ur.role_id = r.role_id
  );
