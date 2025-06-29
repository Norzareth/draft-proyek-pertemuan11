# Panduan Manajemen Pesanan dengan Jadwal Pengiriman Terintegrasi

## Overview
Fitur manajemen pesanan telah diintegrasikan dengan jadwal pengiriman untuk memberikan workflow yang lebih efisien. Admin cabang sekarang dapat mengelola pesanan dan jadwal pengiriman dalam satu interface yang terpadu.

## Fitur Utama

### 1. Manajemen Pesanan Terintegrasi
- **View Pesanan**: Melihat semua pesanan dengan informasi pengiriman
- **Filter Status**: Filter pesanan berdasarkan status (Menunggu, Diproses, Siap, Selesai)
- **Update Status**: Mengubah status pesanan
- **Lihat Detail**: Melihat detail lengkap pesanan

### 2. Jadwal Pengiriman Terintegrasi
- **Jadwalkan Pengiriman**: Menjadwalkan pengiriman untuk pesanan yang siap
- **Status Pengiriman**: Melihat status pengiriman (Belum Dijadwalkan, Menunggu, Dijadwalkan, Dalam Pengiriman, Selesai)
- **Jadwal Kirim**: Menampilkan tanggal jadwal pengiriman
- **Estimasi Sampai**: Menampilkan estimasi waktu sampai

## Cara Penggunaan

### Login sebagai Admin Cabang
1. Jalankan aplikasi
2. Login dengan kredensial admin cabang:
   - Username: `branchadmin`
   - Password: `admin123`

### Mengelola Pesanan
1. **Buka Tab "Manajemen Pesanan"**
2. **Lihat Daftar Pesanan**:
   - ID Pesanan
   - Customer
   - Tanggal Pesanan
   - Status Pesanan
   - Status Pengiriman
   - Jadwal Kirim

### Menjadwalkan Pengiriman
1. **Pilih pesanan dengan status "Siap"**
2. **Klik tombol "Jadwalkan Kirim"**
3. **Isi form jadwal pengiriman**:
   - Jadwal Kirim (default: besok)
   - Estimasi Sampai (default: 2 hari lagi)
   - Status Pengiriman (default: Dijadwalkan)
4. **Klik "Simpan"**

### Update Status Pesanan
1. **Pilih pesanan yang ingin diupdate**
2. **Klik tombol "Update Status"**
3. **Pilih status baru**:
   - Menunggu
   - Diproses
   - Siap
   - Dikirim
   - Selesai
4. **Klik "Simpan"**

### Melihat Detail Pesanan
1. **Pilih pesanan**
2. **Klik tombol "Lihat Detail"**
3. **Lihat informasi lengkap pesanan**

## Workflow yang Disarankan

### Workflow Standar
1. **Pesanan Masuk** → Status: "Menunggu"
2. **Proses Pesanan** → Status: "Diproses"
3. **Pesanan Siap** → Status: "Siap"
4. **Jadwalkan Pengiriman** → Status Pengiriman: "Dijadwalkan"
5. **Kirim Pesanan** → Status: "Dikirim", Status Pengiriman: "Dalam Pengiriman"
6. **Pesanan Selesai** → Status: "Selesai", Status Pengiriman: "Selesai"

### Best Practices
- **Update status secara berkala** untuk tracking yang akurat
- **Jadwalkan pengiriman** segera setelah pesanan siap
- **Monitor estimasi sampai** untuk customer service yang baik
- **Refresh data** secara berkala untuk informasi terbaru

## Struktur Database

### Tabel yang Terlibat
- `pemesanan`: Data pesanan
- `pengiriman`: Data jadwal pengiriman
- `users`: Data customer
- `menu`: Data menu yang dipesan

### Relasi
- `pemesanan.pengiriman_id` → `pengiriman.pengiriman_id`
- `pemesanan.user_id` → `users.user_id`

## Troubleshooting

### Masalah Umum
1. **Pesanan tidak muncul**: Pastikan ada data pesanan di database
2. **Tidak bisa jadwalkan**: Pastikan status pesanan "Siap"
3. **Error database**: Periksa koneksi database dan struktur tabel

### Solusi
1. **Refresh data** menggunakan tombol "Refresh Pesanan"
2. **Periksa log** untuk error detail
3. **Restart aplikasi** jika diperlukan

## Testing

### Test Script
Gunakan script `test_integrated_delivery.bat` untuk testing:
```bash
test_integrated_delivery.bat
```

### Test Cases
1. **Login admin cabang**
2. **Lihat daftar pesanan**
3. **Filter pesanan berdasarkan status**
4. **Update status pesanan**
5. **Jadwalkan pengiriman**
6. **Lihat detail pesanan**

## Keuntungan Integrasi

### Efisiensi
- **Satu interface** untuk manajemen pesanan dan pengiriman
- **Workflow yang terpadu** mengurangi switching antar tab
- **Data yang konsisten** antara pesanan dan pengiriman

### User Experience
- **Interface yang intuitif** dengan semua fitur dalam satu tempat
- **Tracking yang mudah** untuk status pesanan dan pengiriman
- **Aksi yang cepat** tanpa perlu navigasi kompleks

### Maintenance
- **Kode yang lebih sederhana** dengan integrasi fitur
- **Database yang efisien** dengan relasi yang jelas
- **Testing yang mudah** dengan satu workflow 