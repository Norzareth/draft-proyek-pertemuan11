-- =====================================================
-- COMPLETE SAMPLE DATA FOR BD_PROJECT DATABASE
-- =====================================================

-- 1. Insert performa_cabang (id_performa_cabang, tingkat_performa, income)
INSERT INTO performa_cabang (id_performa_cabang, tingkat_performa, income) VALUES
(1, 85, 15000000),
(2, 92, 22000000),
(3, 78, 12000000),
(4, 95, 28000000),
(5, 88, 18000000);

-- 2. Insert cabang (cabang_id, lokasi, id_performa_cabang)
INSERT INTO cabang (cabang_id, lokasi, id_performa_cabang) VALUES
(1, 'Jakarta Pusat', 1),
(2, 'Bandung', 2),
(3, 'Surabaya', 3),
(4, 'Medan', 4),
(5, 'Semarang', 5);

-- 3. Insert penjual_sampingan (id_penjual, nama_penjual, kontak)
INSERT INTO penjual_sampingan (id_penjual, nama_penjual, kontak) VALUES
(1, 'Pak Ahmad', '081234567890'),
(2, 'Bu Siti', '081234567891'),
(3, 'Pak Budi', '081234567892'),
(4, 'Bu Rina', '081234567893'),
(5, 'Pak Joko', '081234567894');

-- 4. Insert role (id_role, jenis_user, jenis_akses) - max 10 karakter!
INSERT INTO role (id_role, jenis_user, jenis_akses) VALUES
(1, 'Customer', 'read'),
(2, 'AdminCab', 'write'),
(3, 'AdminPus', 'admin');

-- 5. Insert users (user_id, username, email, password, id_role) - password max 8, username max 20
INSERT INTO users (user_id, username, email, password, id_role) VALUES
-- Customers
(1, 'john_doe', 'john@example.com', 'pass1', 1),
(2, 'jane_smith', 'jane@example.com', 'pass2', 1),
(3, 'bob_wilson', 'bob@example.com', 'pass3', 1),
(4, 'alice_brown', 'alice@example.com', 'pass4', 1),
(5, 'mike_jones', 'mike@example.com', 'pass5', 1),
(6, 'sarah_davis', 'sarah@example.com', 'pass6', 1),
(7, 'david_miller', 'david@example.com', 'pass7', 1),
(8, 'lisa_garcia', 'lisa@example.com', 'pass8', 1),
-- Admin Cabang
(9, 'admin_jak', 'admin.jak@kat.com', 'admin1', 2),
(10, 'admin_ban', 'admin.ban@kat.com', 'admin1', 2),
(11, 'admin_sur', 'admin.sur@kat.com', 'admin1', 2),
-- Admin Pusat
(12, 'super_adm', 'admin@kat.com', 'super1', 3);

-- 6. Insert daftar_menu (id_menu, nama_menu, jenis, id_penjual, harga, cabang_id)
INSERT INTO daftar_menu (id_menu, nama_menu, jenis, id_penjual, harga, cabang_id) VALUES
-- Makanan Utama
(1, 'Nasi Goreng Spesial', 'Makanan Utama', 1, 25000, 1),
(2, 'Mie Goreng Ayam', 'Makanan Utama', 1, 22000, 1),
(3, 'Ayam Goreng Crispy', 'Makanan Utama', 2, 30000, 1),
(4, 'Ikan Gurame Goreng', 'Makanan Utama', 2, 35000, 1),
(5, 'Sate Ayam (10 tusuk)', 'Makanan Utama', 3, 28000, 2),
(6, 'Sate Kambing (10 tusuk)', 'Makanan Utama', 3, 45000, 2),
(7, 'Rendang Daging', 'Makanan Utama', 4, 40000, 2),
(8, 'Gulai Kambing', 'Makanan Utama', 4, 38000, 2),
(9, 'Soto Ayam', 'Makanan Utama', 5, 20000, 3),
(10, 'Bakso Sapi', 'Makanan Utama', 5, 25000, 3),
-- Minuman
(11, 'Es Teh Manis', 'Minuman', 1, 5000, 1),
(12, 'Es Jeruk', 'Minuman', 1, 8000, 1),
(13, 'Es Kopi', 'Minuman', 2, 12000, 1),
(14, 'Jus Alpukat', 'Minuman', 2, 15000, 2),
(15, 'Es Cendol', 'Minuman', 3, 10000, 2),
(16, 'Es Kelapa Muda', 'Minuman', 3, 12000, 2),
(17, 'Teh Tarik', 'Minuman', 4, 8000, 3),
(18, 'Kopi Tubruk', 'Minuman', 4, 10000, 3),
-- Snack
(19, 'Kentang Goreng', 'Snack', 1, 15000, 1),
(20, 'Pisang Goreng (5 pcs)', 'Snack', 1, 12000, 1),
(21, 'Tahu Crispy', 'Snack', 2, 10000, 2),
(22, 'Tempe Goreng', 'Snack', 2, 8000, 2),
(23, 'Bakwan Jagung', 'Snack', 3, 12000, 3),
(24, 'Martabak Telur', 'Snack', 3, 18000, 3),
-- Dessert
(25, 'Es Krim Vanilla', 'Dessert', 1, 8000, 1),
(26, 'Pudding Coklat', 'Dessert', 2, 10000, 2),
(27, 'Kue Lapis', 'Dessert', 3, 12000, 3),
(28, 'Bubur Sumsum', 'Dessert', 4, 8000, 4);

-- 7. Insert staff_katering (id_staff, nama, job_desc, cabang_id)
INSERT INTO staff_katering (id_staff, nama, job_desc, cabang_id) VALUES
(1, 'Ahmad Supriadi', 'Chef Utama', 1),
(2, 'Siti Nurhaliza', 'Chef Pastry', 1),
(3, 'Budi Santoso', 'Chef Sushi', 2),
(4, 'Rina Marlina', 'Chef Western', 2),
(5, 'Joko Widodo', 'Chef Traditional', 3),
(6, 'Sri Mulyani', 'Manager Cabang', 1),
(7, 'Prabowo Subianto', 'Manager Cabang', 2),
(8, 'Ganjar Pranowo', 'Manager Cabang', 3),
(9, 'Anies Baswedan', 'Manager Cabang', 4),
(10, 'Ridwan Kamil', 'Manager Cabang', 5);

-- 8. Insert pengiriman (pengiriman_id, status_pengiriman, jadwal_kirim, estimasi_sampai)
INSERT INTO pengiriman (pengiriman_id, status_pengiriman, jadwal_kirim, estimasi_sampai) VALUES
(1, 'Belum Dikirim', '2024-01-15', '2024-01-17'),
(2, 'Dalam Pengiriman', '2024-01-16', '2024-01-18'),
(3, 'Selesai Dikirim', '2024-01-14', '2024-01-16'),
(4, 'Belum Dikirim', '2024-01-17', '2024-01-19'),
(5, 'Dalam Pengiriman', '2024-01-18', '2024-01-20'),
(6, 'Selesai Dikirim', '2024-01-13', '2024-01-15'),
(7, 'Belum Dikirim', '2024-01-19', '2024-01-21'),
(8, 'Dalam Pengiriman', '2024-01-20', '2024-01-22');

-- 9. Insert pemesanan (pesanan_id, tanggal_pesanan, status, user_id, pengiriman_id)
INSERT INTO pemesanan (pesanan_id, tanggal_pesanan, status, user_id, pengiriman_id) VALUES
(1, '2024-01-14', 'Selesai', 1, 3),
(2, '2024-01-15', 'Dalam Proses', 2, 1),
(3, '2024-01-16', 'Dalam Proses', 3, 2),
(4, '2024-01-17', 'Belum Diproses', 4, 4),
(5, '2024-01-13', 'Selesai', 5, 6),
(6, '2024-01-18', 'Belum Diproses', 6, 5),
(7, '2024-01-19', 'Belum Diproses', 7, 7),
(8, '2024-01-20', 'Belum Diproses', 8, 8);

-- 10. Insert pesanan_menu (pesanan_id, id_menu)
INSERT INTO pesanan_menu (pesanan_id, id_menu) VALUES
(1, 1), -- Order 1: Nasi Goreng Spesial
(1, 11), -- Order 1: Es Teh Manis
(2, 3), -- Order 2: Ayam Goreng Crispy
(2, 12), -- Order 2: Es Jeruk
(3, 5), -- Order 3: Sate Ayam
(3, 14), -- Order 3: Jus Alpukat
(4, 7), -- Order 4: Rendang Daging
(4, 15), -- Order 4: Es Cendol
(5, 9), -- Order 5: Soto Ayam
(5, 17), -- Order 5: Teh Tarik
(6, 2), -- Order 6: Mie Goreng Ayam
(6, 13), -- Order 6: Es Kopi
(7, 4), -- Order 7: Ikan Gurame Goreng
(7, 16), -- Order 7: Es Kelapa Muda
(8, 6), -- Order 8: Sate Kambing
(8, 18); -- Order 8: Kopi Tubruk

-- 11. Insert rating (id_rating, comment, nilai_rating, user_id, pesanan_id)
INSERT INTO rating (id_rating, comment, nilai_rating, user_id, pesanan_id) VALUES
(1, 'Makanan enak, pengiriman cepat!', 5, 1, 1),
(2, 'Ayam crispy nya renyah banget', 4, 2, 2),
(3, 'Sate ayam empuk dan bumbu meresap', 5, 3, 3),
(4, 'Rendang daging lembut dan gurih', 4, 4, 4),
(5, 'Soto ayam kuahnya enak', 5, 5, 5);

-- 12. Insert diskon (id_diskon, persentase_diskon, syarat_diskon, pesanan_id)
INSERT INTO diskon (id_diskon, persentase_diskon, syarat_diskon, pesanan_id) VALUES
(1, 10, 'Minimal pembelian Rp 50.000', 1),
(2, 15, 'Member baru', 2),
(3, 5, 'Pembelian di atas Rp 100.000', 3),
(4, 20, 'Promo weekend', 4),
(5, 10, 'Diskon member', 5);

-- =====================================================
-- VERIFICATION QUERIES
-- =====================================================

-- Check all data
SELECT 'Roles' as table_name, COUNT(*) as count FROM role
UNION ALL
SELECT 'Performa Cabang', COUNT(*) FROM performa_cabang
UNION ALL
SELECT 'Cabang', COUNT(*) FROM cabang
UNION ALL
SELECT 'Penjual Sampingan', COUNT(*) FROM penjual_sampingan
UNION ALL
SELECT 'Users', COUNT(*) FROM users
UNION ALL
SELECT 'Daftar Menu', COUNT(*) FROM daftar_menu
UNION ALL
SELECT 'Staff Katering', COUNT(*) FROM staff_katering
UNION ALL
SELECT 'Pengiriman', COUNT(*) FROM pengiriman
UNION ALL
SELECT 'Pemesanan', COUNT(*) FROM pemesanan
UNION ALL
SELECT 'Pesanan Menu', COUNT(*) FROM pesanan_menu
UNION ALL
SELECT 'Rating', COUNT(*) FROM rating
UNION ALL
SELECT 'Diskon', COUNT(*) FROM diskon;

-- Show customer login details
SELECT 'CUSTOMER LOGIN DETAILS' as info;
SELECT u.user_id, u.username, u.email, u.password, r.jenis_user
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE r.jenis_user = 'Customer'
ORDER BY u.user_id;

-- Show admin login details
SELECT 'ADMIN LOGIN DETAILS' as info;
SELECT u.user_id, u.username, u.email, u.password, r.jenis_user
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE r.jenis_user IN ('AdminCab', 'AdminPus')
ORDER BY r.jenis_user, u.user_id;

-- Show menu by category
SELECT 'MENU BY CATEGORY' as info;
SELECT jenis, COUNT(*) as jumlah_menu, 
       'Rp ' || MIN(harga) || ' - Rp ' || MAX(harga) as range_harga
FROM daftar_menu
GROUP BY jenis
ORDER BY jenis; 