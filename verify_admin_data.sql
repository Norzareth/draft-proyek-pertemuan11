-- =====================================================
-- VERIFICATION QUERIES FOR ADMIN DATA
-- =====================================================

-- Check admin users
SELECT 'ADMIN USERS' as info;
SELECT u.user_id, u.username, u.email, u.password, r.jenis_user
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE r.jenis_user IN ('AdminCab', 'AdminPus')
ORDER BY r.jenis_user, u.username;

-- Check roles
SELECT 'ROLES' as info;
SELECT * FROM role ORDER BY id_role;

-- Check cabang data
SELECT 'CABANG DATA' as info;
SELECT c.cabang_id, c.lokasi, pc.tingkat_performa, pc.income
FROM cabang c
JOIN performa_cabang pc ON c.id_performa_cabang = pc.id_performa_cabang
ORDER BY c.cabang_id;

-- Check staff data
SELECT 'STAFF DATA' as info;
SELECT s.id_staff, s.nama, s.job_desc, c.lokasi
FROM staff_katering s
JOIN cabang c ON s.cabang_id = c.cabang_id
ORDER BY c.lokasi, s.nama;

-- Check menu data per cabang
SELECT 'MENU DATA PER CABANG' as info;
SELECT c.lokasi, COUNT(dm.id_menu) as jumlah_menu
FROM cabang c
LEFT JOIN daftar_menu dm ON c.cabang_id = dm.cabang_id
GROUP BY c.cabang_id, c.lokasi
ORDER BY c.lokasi;

-- Check order data
SELECT 'ORDER DATA' as info;
SELECT COUNT(*) as total_orders FROM pemesanan;

-- Test admin login queries
SELECT 'TEST ADMIN LOGIN QUERIES' as info;

-- Test Admin Cabang login
SELECT 'Testing admin_jak login:' as test;
SELECT u.user_id, u.username, u.password, r.jenis_user
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE u.username = 'admin_jak' AND r.jenis_user = 'AdminCab';

-- Test Admin Pusat login
SELECT 'Testing super_adm login:' as test;
SELECT u.user_id, u.username, u.password, r.jenis_user
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE u.username = 'super_adm' AND r.jenis_user = 'AdminPus';

-- =====================================================
-- SUMMARY
-- =====================================================
SELECT 'SUMMARY' as info;
SELECT 'Total Admin Cabang' as type, COUNT(*) as count
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE r.jenis_user = 'AdminCab'
UNION ALL
SELECT 'Total Admin Pusat' as type, COUNT(*) as count
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE r.jenis_user = 'AdminPus'
UNION ALL
SELECT 'Total Cabang' as type, COUNT(*) as count
FROM cabang
UNION ALL
SELECT 'Total Staff' as type, COUNT(*) as count
FROM staff_katering; 