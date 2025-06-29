# Update Tab Performa Cabang

## Perubahan yang Dilakukan

### 1. Tampilan Langsung Data Performa
- **Sebelum:** Tab hanya menampilkan tombol "Lihat Performa Cabang"
- **Sesudah:** Tab langsung menampilkan tabel data performa cabang

### 2. Tabel Performa Cabang
- **Kolom Cabang:** Menampilkan lokasi cabang
- **Kolom Tingkat Performa:** Menampilkan tingkat performa (angka)
- **Kolom Pendapatan:** Menampilkan pendapatan dalam Rupiah

### 3. Fitur Refresh
- Tombol "Refresh Data" untuk memperbarui data performa
- Data otomatis dimuat saat tab dibuka

### 4. Layout yang Diperbaiki
- Header dengan judul "Data Performa Cabang"
- Tabel yang responsive dengan VBox.vgrow="ALWAYS"
- Tombol refresh di bagian bawah kanan

## Struktur Data

### Query SQL
```sql
SELECT c.lokasi, p.tingkat_performa, p.income
FROM cabang c
JOIN performa_cabang p ON c.id_performa_cabang = p.id_performa_cabang
```

### Class PerformaCabang
```java
public static class PerformaCabang {
    private final String lokasi;
    private final int performa;
    private final int income;
    // getters...
}
```

## Fitur yang Tersedia

### 1. Tampilan Otomatis
- Data performa langsung dimuat saat tab dibuka
- Tidak perlu menekan tombol tambahan

### 2. Refresh Data
- Tombol "Refresh Data" untuk memperbarui data
- Konfirmasi "Data berhasil diperbarui" setelah refresh

### 3. Error Handling
- Pesan error jika gagal memuat data performa
- Graceful handling untuk koneksi database

## Testing

Jalankan script `test_performance_tab.bat` untuk menguji:
1. Login sebagai admin pusat
2. Buka tab "Performa Cabang"
3. Verifikasi data performa ditampilkan langsung
4. Test tombol refresh
5. Verifikasi format data yang benar

## Kredensial Login
- Username: `admin_central`
- Password: `admin123` 