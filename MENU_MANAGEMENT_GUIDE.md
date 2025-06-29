# Panduan Manajemen Menu - Admin Cabang

## Fitur yang Tersedia

### 1. Melihat Daftar Menu
- **Tabel Menu**: Menampilkan semua menu dengan informasi lengkap
- **Kolom yang ditampilkan**:
  - ID Menu
  - Nama Menu
  - Jenis (Makanan Utama, Minuman, Snack, Dessert)
  - Harga (diformat dengan Rp)
  - Penjual
  - Cabang
  - Aksi (Edit/Hapus)

### 2. Filter Menu
- **Filter berdasarkan jenis**: Semua Jenis, Makanan Utama, Minuman, Snack, Dessert
- **Cara menggunakan**: Pilih jenis dari dropdown filter

### 3. Menambah Menu Baru
- **Tombol**: "Tambah Menu Baru" (hijau)
- **Field yang harus diisi**:
  - Nama Menu (text)
  - Jenis (dropdown: Makanan Utama, Minuman, Snack, Dessert)
  - Harga (angka)
  - Penjual (dropdown dari database)
  - Cabang (dropdown dari database)

### 4. Mengedit Menu
- **Tombol**: "Edit" (kuning) di kolom Aksi
- **Fitur**: Mengubah semua field menu yang ada
- **Validasi**: Semua field harus diisi

### 5. Menghapus Menu
- **Tombol**: "Hapus" (merah) di kolom Aksi
- **Konfirmasi**: Dialog konfirmasi sebelum menghapus

### 6. Refresh Data
- **Tombol**: "Refresh Data" (biru)
- **Fungsi**: Memuat ulang data dari database

## Cara Menggunakan

### Login sebagai Admin Cabang
```
Username: admin_jak
Password: admin1
```

### Langkah-langkah Menambah Menu Baru
1. Klik tombol "Tambah Menu Baru"
2. Isi semua field yang diperlukan
3. Klik "Simpan"
4. Menu akan muncul di tabel

### Langkah-langkah Mengedit Menu
1. Klik tombol "Edit" pada menu yang ingin diedit
2. Ubah field yang diinginkan
3. Klik "Simpan"
4. Data akan diperbarui di tabel

### Langkah-langkah Menghapus Menu
1. Klik tombol "Hapus" pada menu yang ingin dihapus
2. Konfirmasi penghapusan
3. Menu akan dihapus dari database dan tabel

## Struktur Database

### Tabel yang Terlibat
- `daftar_menu`: Data menu utama
- `penjual_sampingan`: Data penjual
- `cabang`: Data cabang

### Relasi
- Menu → Penjual (id_penjual)
- Menu → Cabang (cabang_id)

## Troubleshooting

### Masalah Umum

#### 1. Data tidak muncul
- **Solusi**: Klik tombol "Refresh Data"
- **Penyebab**: Koneksi database terputus

#### 2. Error saat menambah/edit menu
- **Solusi**: Pastikan semua field diisi
- **Penyebab**: Field kosong atau format harga salah

#### 3. Menu tidak bisa dihapus
- **Solusi**: Pastikan menu tidak digunakan di pesanan
- **Penyebab**: Foreign key constraint

#### 4. Dropdown penjual/cabang kosong
- **Solusi**: Restart aplikasi
- **Penyebab**: Koneksi database bermasalah

### Error Messages
- **"Gagal memuat data menu"**: Koneksi database bermasalah
- **"Semua field harus diisi"**: Ada field yang kosong
- **"Harga harus berupa angka"**: Format harga salah
- **"Gagal menambahkan/memperbarui/menghapus menu"**: Error database

## Tips Penggunaan

1. **Refresh secara berkala** untuk memastikan data terbaru
2. **Gunakan filter** untuk mencari menu tertentu
3. **Periksa harga** sebelum menyimpan
4. **Backup data** sebelum melakukan perubahan besar
5. **Test fitur** di menu yang tidak penting terlebih dahulu

## Keamanan

- **Validasi input**: Semua input divalidasi
- **Konfirmasi hapus**: Mencegah penghapusan tidak sengaja
- **Error handling**: Pesan error yang informatif
- **Database constraints**: Mencegah data tidak valid 