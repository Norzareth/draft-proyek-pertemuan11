-- =====================================================
-- SETUP ADMIN DATA FOR BD_PROJECT DATABASE
-- =====================================================

-- 1. Clear existing admin data (if any)
DELETE FROM users WHERE id_role IN (SELECT id_role FROM role WHERE jenis_user IN ('AdminCab', 'AdminPus'));
DELETE FROM role WHERE jenis_user IN ('AdminCab', 'AdminPus');

-- 2. Insert roles for admin
INSERT INTO role (id_role, jenis_user, jenis_akses) VALUES
(2, 'AdminCab', 'write'),
(3, 'AdminPus', 'admin')
ON CONFLICT (id_role) DO UPDATE SET 
    jenis_user = EXCLUDED.jenis_user,
    jenis_akses = EXCLUDED.jenis_akses;

-- 3. Insert admin users
INSERT INTO users (user_id, username, email, password, id_role) VALUES
-- Admin Cabang
(9, 'admin_jak', 'admin.jak@kat.com', 'admin1', 2),
(10, 'admin_ban', 'admin.ban@kat.com', 'admin1', 2),
(11, 'admin_sur', 'admin.sur@kat.com', 'admin1', 2),
-- Admin Pusat
(12, 'super_adm', 'admin@kat.com', 'super1', 3)
ON CONFLICT (user_id) DO UPDATE SET 
    username = EXCLUDED.username,
    email = EXCLUDED.email,
    password = EXCLUDED.password,
    id_role = EXCLUDED.id_role;

-- 4. Verify admin data
SELECT 'VERIFICATION - ADMIN USERS' as info;
SELECT u.user_id, u.username, u.email, u.password, r.jenis_user
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE r.jenis_user IN ('AdminCab', 'AdminPus')
ORDER BY r.jenis_user, u.username;

-- 5. Test login queries
SELECT 'TEST LOGIN QUERIES' as info;

-- Test Admin Cabang login
SELECT 'Testing admin_jak (Admin Cabang):' as test;
SELECT u.user_id, u.username, u.password, r.jenis_user
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE u.username = 'admin_jak' AND r.jenis_user = 'AdminCab';

-- Test Admin Pusat login
SELECT 'Testing super_adm (Admin Pusat):' as test;
SELECT u.user_id, u.username, u.password, r.jenis_user
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE u.username = 'super_adm' AND r.jenis_user = 'AdminPus';

-- 6. Summary
SELECT 'SUMMARY' as info;
SELECT 'Total Admin Cabang' as type, COUNT(*) as count
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE r.jenis_user = 'AdminCab'
UNION ALL
SELECT 'Total Admin Pusat' as type, COUNT(*) as count
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE r.jenis_user = 'AdminPus';

-- 7. Connection test
SELECT 'DATABASE CONNECTION TEST' as info;
SELECT 'Database connection successful' as status, 
       current_database() as database_name,
       current_user as current_user; 