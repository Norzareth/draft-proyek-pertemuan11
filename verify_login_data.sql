-- =====================================================
-- VERIFY LOGIN DATA FOR BD_PROJECT
-- =====================================================

-- Check if database exists and has data
SELECT 'Database check' as info;

-- Check all tables exist
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public' 
ORDER BY table_name;

-- Check role table
SELECT '=== ROLE TABLE ===' as info;
SELECT * FROM role ORDER BY id_role;

-- Check users table
SELECT '=== USERS TABLE ===' as info;
SELECT user_id, username, email, password, id_role 
FROM users 
ORDER BY user_id;

-- Check user-role mapping
SELECT '=== USER-ROLE MAPPING ===' as info;
SELECT 
    u.user_id,
    u.username,
    u.email,
    u.password,
    r.jenis_user as role_type,
    r.jenis_akses as access_type
FROM users u
JOIN role r ON u.id_role = r.id_role
ORDER BY r.jenis_user, u.user_id;

-- Test specific admin logins
SELECT '=== ADMIN LOGIN TEST ===' as info;

-- Admin Cabang
SELECT 'Admin Cabang - admin_jak' as test_case;
SELECT 
    u.username,
    u.password,
    r.jenis_user,
    CASE 
        WHEN u.username = 'admin_jak' AND u.password = 'admin1' AND r.jenis_user = 'AdminCab' 
        THEN 'LOGIN SUCCESS'
        ELSE 'LOGIN FAILED'
    END as login_status
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE u.username = 'admin_jak';

SELECT 'Admin Cabang - admin_ban' as test_case;
SELECT 
    u.username,
    u.password,
    r.jenis_user,
    CASE 
        WHEN u.username = 'admin_ban' AND u.password = 'admin1' AND r.jenis_user = 'AdminCab' 
        THEN 'LOGIN SUCCESS'
        ELSE 'LOGIN FAILED'
    END as login_status
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE u.username = 'admin_ban';

SELECT 'Admin Cabang - admin_sur' as test_case;
SELECT 
    u.username,
    u.password,
    r.jenis_user,
    CASE 
        WHEN u.username = 'admin_sur' AND u.password = 'admin1' AND r.jenis_user = 'AdminCab' 
        THEN 'LOGIN SUCCESS'
        ELSE 'LOGIN FAILED'
    END as login_status
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE u.username = 'admin_sur';

-- Admin Pusat
SELECT 'Admin Pusat - super_adm' as test_case;
SELECT 
    u.username,
    u.password,
    r.jenis_user,
    CASE 
        WHEN u.username = 'super_adm' AND u.password = 'super1' AND r.jenis_user = 'AdminPus' 
        THEN 'LOGIN SUCCESS'
        ELSE 'LOGIN FAILED'
    END as login_status
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE u.username = 'super_adm';

-- Test customer logins
SELECT '=== CUSTOMER LOGIN TEST ===' as info;

SELECT 'Customer - john_doe' as test_case;
SELECT 
    u.username,
    u.password,
    r.jenis_user,
    CASE 
        WHEN u.username = 'john_doe' AND u.password = 'pass1' AND r.jenis_user = 'Customer' 
        THEN 'LOGIN SUCCESS'
        ELSE 'LOGIN FAILED'
    END as login_status
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE u.username = 'john_doe';

-- Summary of all users by role
SELECT '=== SUMMARY BY ROLE ===' as info;
SELECT 
    r.jenis_user as role_type,
    COUNT(*) as user_count,
    STRING_AGG(u.username, ', ') as usernames
FROM users u
JOIN role r ON u.id_role = r.id_role
GROUP BY r.jenis_user
ORDER BY r.jenis_user;

-- Check for any potential issues
SELECT '=== POTENTIAL ISSUES ===' as info;

-- Check for duplicate usernames
SELECT 'Duplicate usernames:' as check_type;
SELECT username, COUNT(*) as count
FROM users
GROUP BY username
HAVING COUNT(*) > 1;

-- Check for empty passwords
SELECT 'Empty passwords:' as check_type;
SELECT username, password
FROM users
WHERE password IS NULL OR password = '';

-- Check for users without roles
SELECT 'Users without roles:' as check_type;
SELECT u.user_id, u.username
FROM users u
LEFT JOIN role r ON u.id_role = r.id_role
WHERE r.id_role IS NULL;

-- Check role name lengths (should be <= 10 for varchar(10))
SELECT 'Role names with length > 10:' as check_type;
SELECT id_role, jenis_user, LENGTH(jenis_user) as name_length
FROM role
WHERE LENGTH(jenis_user) > 10;

-- Verify login data in database
SELECT 
    u.user_id,
    u.username,
    u.email,
    u.password,
    r.jenis_user,
    r.jenis_akses
FROM users u
JOIN role r ON u.id_role = r.id_role
ORDER BY u.user_id;

-- Check if we have the required test users
SELECT 
    'Customer Test' as test_type,
    COUNT(*) as count
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE r.jenis_user = 'Customer' AND u.username = 'customer';

SELECT 
    'Branch Admin Test' as test_type,
    COUNT(*) as count
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE r.jenis_user = 'Admin Cabang' AND u.username = 'branchadmin';

SELECT 
    'Central Admin Test' as test_type,
    COUNT(*) as count
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE r.jenis_user = 'Admin Pusat' AND u.username = 'centraladmin';