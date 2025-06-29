-- =====================================================
-- ADD PROMOTION AND STOCK TABLES
-- =====================================================

-- 1. Create promosi table
CREATE TABLE IF NOT EXISTS promosi (
    id_promosi SERIAL PRIMARY KEY,
    nama_promosi VARCHAR(100) NOT NULL,
    jenis_promosi VARCHAR(50) NOT NULL, -- 'Cabang', 'Kategori', 'Global'
    persentase_diskon INTEGER NOT NULL,
    minimal_pembelian INTEGER DEFAULT 0,
    tanggal_mulai DATE NOT NULL,
    tanggal_berakhir DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'Aktif', -- 'Aktif', 'Nonaktif'
    cabang_id INTEGER REFERENCES cabang(cabang_id),
    kategori_menu VARCHAR(50), -- for kategori-based promotions
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Create stok_harian table
CREATE TABLE IF NOT EXISTS stok_harian (
    id_stok SERIAL PRIMARY KEY,
    id_menu INTEGER REFERENCES daftar_menu(id_menu),
    cabang_id INTEGER REFERENCES cabang(cabang_id),
    tanggal DATE NOT NULL,
    stok_awal INTEGER DEFAULT 0,
    stok_terjual INTEGER DEFAULT 0,
    stok_tersisa INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'Tersedia', -- 'Tersedia', 'Habis', 'Terbatas'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(id_menu, cabang_id, tanggal)
);

-- 3. Create jadwal_pengiriman table
CREATE TABLE IF NOT EXISTS jadwal_pengiriman (
    id_jadwal SERIAL PRIMARY KEY,
    pengiriman_id INTEGER REFERENCES pengiriman(pengiriman_id),
    cabang_id INTEGER REFERENCES cabang(cabang_id),
    tanggal_kirim DATE NOT NULL,
    waktu_kirim TIME,
    status_jadwal VARCHAR(20) DEFAULT 'Terjadwal', -- 'Terjadwal', 'Dalam Proses', 'Selesai'
    catatan TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Insert sample promotion data
INSERT INTO promosi (nama_promosi, jenis_promosi, persentase_diskon, minimal_pembelian, tanggal_mulai, tanggal_berakhir, cabang_id, kategori_menu) VALUES
('Promo Jakarta Weekend', 'Cabang', 15, 50000, '2024-01-01', '2024-12-31', 1, NULL),
('Diskon Makanan Utama', 'Kategori', 10, 30000, '2024-01-01', '2024-12-31', NULL, 'Makanan Utama'),
('Promo Bandung Baru', 'Cabang', 20, 75000, '2024-01-01', '2024-12-31', 2, NULL),
('Diskon Minuman', 'Kategori', 5, 15000, '2024-01-01', '2024-12-31', NULL, 'Minuman'),
('Promo Global Member', 'Global', 10, 100000, '2024-01-01', '2024-12-31', NULL, NULL);

-- 5. Insert sample stock data
INSERT INTO stok_harian (id_menu, cabang_id, tanggal, stok_awal, stok_terjual, stok_tersisa, status) VALUES
(1, 1, CURRENT_DATE, 50, 10, 40, 'Tersedia'),
(2, 1, CURRENT_DATE, 30, 5, 25, 'Tersedia'),
(3, 1, CURRENT_DATE, 20, 15, 5, 'Terbatas'),
(4, 1, CURRENT_DATE, 15, 15, 0, 'Habis'),
(5, 2, CURRENT_DATE, 40, 8, 32, 'Tersedia'),
(6, 2, CURRENT_DATE, 25, 12, 13, 'Terbatas'),
(7, 2, CURRENT_DATE, 35, 20, 15, 'Tersedia'),
(8, 2, CURRENT_DATE, 20, 18, 2, 'Terbatas'),
(9, 3, CURRENT_DATE, 45, 10, 35, 'Tersedia'),
(10, 3, CURRENT_DATE, 30, 25, 5, 'Terbatas');

-- 6. Insert sample delivery schedule
INSERT INTO jadwal_pengiriman (pengiriman_id, cabang_id, tanggal_kirim, waktu_kirim, status_jadwal, catatan) VALUES
(1, 1, '2024-01-15', '08:00:00', 'Terjadwal', 'Pengiriman pagi'),
(2, 1, '2024-01-16', '14:00:00', 'Dalam Proses', 'Pengiriman siang'),
(3, 2, '2024-01-14', '09:00:00', 'Selesai', 'Pengiriman selesai'),
(4, 2, '2024-01-17', '10:00:00', 'Terjadwal', 'Pengiriman pagi'),
(5, 3, '2024-01-18', '15:00:00', 'Terjadwal', 'Pengiriman sore');

-- 7. Create indexes for better performance
CREATE INDEX idx_promosi_cabang ON promosi(cabang_id);
CREATE INDEX idx_promosi_kategori ON promosi(kategori_menu);
CREATE INDEX idx_promosi_status ON promosi(status);
CREATE INDEX idx_stok_menu_cabang ON stok_harian(id_menu, cabang_id);
CREATE INDEX idx_stok_tanggal ON stok_harian(tanggal);
CREATE INDEX idx_jadwal_pengiriman ON jadwal_pengiriman(pengiriman_id, cabang_id);

-- 8. Verification queries
SELECT 'PROMOSI DATA' as info;
SELECT * FROM promosi ORDER BY id_promosi;

SELECT 'STOK HARIAN DATA' as info;
SELECT sh.*, dm.nama_menu, c.lokasi
FROM stok_harian sh
JOIN daftar_menu dm ON sh.id_menu = dm.id_menu
JOIN cabang c ON sh.cabang_id = c.cabang_id
WHERE sh.tanggal = CURRENT_DATE
ORDER BY c.lokasi, dm.nama_menu;

SELECT 'JADWAL PENGIRIMAN DATA' as info;
SELECT jp.*, c.lokasi
FROM jadwal_pengiriman jp
JOIN cabang c ON jp.cabang_id = c.cabang_id
ORDER BY jp.tanggal_kirim, jp.waktu_kirim; 