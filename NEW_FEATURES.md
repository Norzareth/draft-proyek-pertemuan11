# Fitur Baru - Admin Cabang & Admin Pusat

## 🏪 Admin Cabang - Fitur Baru

### 1. Manajemen Menu & Stok Harian
- **Melihat Menu dengan Stok**: Tabel menu menampilkan stok hari ini dan status ketersediaan
- **Update Stok**: Tombol untuk memperbarui stok menu secara real-time
- **Set Stok**: Mengatur stok awal untuk menu tertentu
- **Status Stok**: 
  - 🟢 Tersedia (stok > 10)
  - 🟡 Terbatas (stok 1-10)
  - 🔴 Habis (stok = 0)

### 2. Pesanan & Pengiriman
- **Filter Pesanan**: Filter berdasarkan status dan tanggal
- **Proses Pesanan**: Update status pesanan dari "Belum Diproses" ke "Dalam Proses"
- **Jadwalkan Pengiriman**: Mengatur jadwal pengiriman untuk pesanan
- **Update Status**: Mengubah status pesanan dan pengiriman
- **Lihat Detail**: Melihat detail lengkap pesanan

### 3. Laporan Cabang
- **Laporan Penjualan**: Generate laporan berdasarkan periode
- **Analisis Stok**: Melihat pergerakan stok harian
- **Export Laporan**: Export ke PDF/Excel

## 🏢 Admin Pusat - Fitur Baru

### 1. Performa Cabang
- **Analisis Performa**: Melihat performa semua cabang dalam periode tertentu
- **Metrik Performa**:
  - Total pesanan per cabang
  - Total pendapatan per cabang
  - Rata-rata nilai pesanan
  - Skor performa (berdasarkan multiple metrics)
  - Pertumbuhan (%) dibanding periode sebelumnya
  - Status performa (Excellent, Good, Fair, Poor)
- **Detail Performa**: Melihat detail performa cabang tertentu
- **Grafik Performa**: Visualisasi performa cabang
- **Export Laporan**: Export laporan performa

### 2. Manajemen Promosi
- **Jenis Promosi**:
  - Promosi per Cabang
  - Promosi per Kategori Menu
  - Promosi Global
- **Fitur Promosi**:
  - Tambah promosi baru
  - Edit promosi existing
  - Hapus promosi
  - Aktifkan/Nonaktifkan promosi
  - Set persentase diskon
  - Set minimal pembelian
  - Set periode promosi
- **Status Promosi**: Aktif/Nonaktif

## 📊 Database Schema Baru

### Tabel Promosi
```sql
CREATE TABLE promosi (
    id_promosi SERIAL PRIMARY KEY,
    nama_promosi VARCHAR(100) NOT NULL,
    jenis_promosi VARCHAR(50) NOT NULL, -- 'Cabang', 'Kategori', 'Global'
    persentase_diskon INTEGER NOT NULL,
    minimal_pembelian INTEGER DEFAULT 0,
    tanggal_mulai DATE NOT NULL,
    tanggal_berakhir DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'Aktif',
    cabang_id INTEGER REFERENCES cabang(cabang_id),
    kategori_menu VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Tabel Stok Harian
```sql
CREATE TABLE stok_harian (
    id_stok SERIAL PRIMARY KEY,
    id_menu INTEGER REFERENCES daftar_menu(id_menu),
    cabang_id INTEGER REFERENCES cabang(cabang_id),
    tanggal DATE NOT NULL,
    stok_awal INTEGER DEFAULT 0,
    stok_terjual INTEGER DEFAULT 0,
    stok_tersisa INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'Tersedia',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(id_menu, cabang_id, tanggal)
);
```

### Tabel Jadwal Pengiriman
```sql
CREATE TABLE jadwal_pengiriman (
    id_jadwal SERIAL PRIMARY KEY,
    pengiriman_id INTEGER REFERENCES pengiriman(pengiriman_id),
    cabang_id INTEGER REFERENCES cabang(cabang_id),
    tanggal_kirim DATE NOT NULL,
    waktu_kirim TIME,
    status_jadwal VARCHAR(20) DEFAULT 'Terjadwal',
    catatan TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🚀 Cara Setup Fitur Baru

### 1. Setup Database
```bash
# Jalankan script untuk menambah tabel baru
psql -d bd_project -f add_promotion_tables.sql
```

### 2. Restart Aplikasi
```bash
# Jalankan aplikasi
run_application.bat
```

### 3. Test Fitur Baru

**Admin Cabang:**
1. Login dengan `admin_jak` / `admin1`
2. Pilih role "Admin Cabang"
3. Cek tab "Manajemen Menu & Stok"
4. Cek tab "Pesanan & Pengiriman"

**Admin Pusat:**
1. Login dengan `super_adm` / `super1`
2. Pilih role "Admin Pusat"
3. Cek tab "Performa Cabang"
4. Cek tab "Manajemen Promosi"

## 📋 Sample Data

### Promosi Sample
- Promo Jakarta Weekend: 15% diskon, min. pembelian Rp 50.000
- Diskon Makanan Utama: 10% diskon, min. pembelian Rp 30.000
- Promo Bandung Baru: 20% diskon, min. pembelian Rp 75.000
- Diskon Minuman: 5% diskon, min. pembelian Rp 15.000
- Promo Global Member: 10% diskon, min. pembelian Rp 100.000

### Stok Sample
- Nasi Goreng Spesial: 50 stok awal, 10 terjual, 40 tersisa
- Ayam Goreng Crispy: 20 stok awal, 15 terjual, 5 tersisa (Terbatas)
- Ikan Gurame Goreng: 15 stok awal, 15 terjual, 0 tersisa (Habis)

## 🔧 Troubleshooting

### ❌ Tabel Baru Tidak Muncul
1. Jalankan `add_promotion_tables.sql`
2. Restart aplikasi
3. Periksa console untuk error database

### ❌ Fitur Tidak Berfungsi
1. Pastikan login dengan role yang benar
2. Periksa koneksi database
3. Pastikan data sample sudah dimasukkan

### ❌ Stok Tidak Update
1. Periksa tabel `stok_harian`
2. Pastikan ada data untuk tanggal hari ini
3. Cek constraint UNIQUE pada tabel stok

## 🎯 Roadmap Fitur Selanjutnya

### Admin Cabang
- [ ] Notifikasi stok menipis
- [ ] Auto-reorder stok
- [ ] Laporan penjualan real-time
- [ ] Manajemen supplier

### Admin Pusat
- [ ] Dashboard analytics
- [ ] Prediksi penjualan
- [ ] Manajemen harga dinamis
- [ ] Integrasi dengan sistem eksternal 