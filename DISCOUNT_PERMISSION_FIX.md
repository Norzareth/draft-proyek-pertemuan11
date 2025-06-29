# Fix Permission Denied untuk Tabel Diskon

## Masalah
Error "permission denied for table diskon" terjadi karena:
1. User `squire` tidak memiliki permission untuk mengakses tabel diskon
2. Tabel diskon mungkin memiliki struktur yang salah
3. Permission tidak diberikan dengan benar

## Solusi

### Langkah 1: Jalankan Script Perbaikan Permission
Jalankan script sebagai PostgreSQL superuser:
```bash
fix_discount_permissions_correct.bat
```

**Catatan:** Script ini menggunakan user `postgres` (superuser) untuk memberikan permission ke user `squire`.

### Langkah 2: Verifikasi Perbaikan
Script akan:
1. Memberikan USAGE dan CREATE permission pada schema public
2. Drop tabel diskon yang ada (jika ada struktur yang salah)
3. Membuat tabel diskon dengan struktur yang benar:
   - `id_diskon` (SERIAL PRIMARY KEY)
   - `harga_baru` (INTEGER NOT NULL)
   - `deskripsi_diskon` (TEXT NOT NULL)
   - `id_menu` (INTEGER REFERENCES daftar_menu)
4. Memberikan ALL PRIVILEGES pada tabel diskon
5. Memberikan permission pada sequence
6. Menambahkan sample data
7. Menampilkan verifikasi permission dan struktur

### Langkah 3: Test Aplikasi
Jalankan script test:
```bash
test_discount_permissions_fixed.bat
```

## Struktur Tabel yang Dibuat

```sql
CREATE TABLE diskon (
    id_diskon SERIAL PRIMARY KEY,
    harga_baru INTEGER NOT NULL,
    deskripsi_diskon TEXT NOT NULL,
    id_menu INTEGER REFERENCES daftar_menu(id_menu) ON DELETE CASCADE
);
```

## Permission yang Diberikan

```sql
-- Schema permissions
GRANT USAGE ON SCHEMA public TO squire;
GRANT CREATE ON SCHEMA public TO squire;

-- Table permissions
GRANT ALL PRIVILEGES ON TABLE diskon TO squire;

-- Sequence permissions
GRANT USAGE, SELECT ON SEQUENCE diskon_id_diskon_seq TO squire;
```

## Sample Data
Script akan menambahkan sample data:
- Menu ID 1: Harga baru 8000 - "Promo 20% off untuk menu favorit"
- Menu ID 2: Harga baru 8500 - "Diskon 15% untuk pembelian pertama"

## Troubleshooting

### Jika Script Gagal:
1. **Password postgres salah**: Ganti password di script atau gunakan pgAdmin
2. **Database tidak ada**: Pastikan database `bd_project` sudah dibuat
3. **PostgreSQL tidak berjalan**: Start PostgreSQL service

### Alternatif Manual:
Jika script batch gagal, jalankan manual di pgAdmin atau psql:

```sql
-- Connect as postgres user
\c bd_project

-- Run the permission fix script
\i fix_discount_permissions_correct.sql
```

### Verifikasi Manual:
```sql
-- Check if table exists with correct structure
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'diskon' 
ORDER BY ordinal_position;

-- Check permissions
SELECT 
    table_name,
    privilege_type,
    grantee
FROM information_schema.table_privileges 
WHERE table_name = 'diskon' AND grantee = 'squire';

-- Check sample data
SELECT d.*, m.nama_menu 
FROM diskon d 
JOIN daftar_menu m ON d.id_menu = m.id_menu;
```

## Testing Checklist

Setelah menjalankan script perbaikan:

- [ ] Script berhasil dijalankan tanpa error
- [ ] Tabel diskon ada dengan struktur yang benar
- [ ] User squire memiliki permission yang cukup
- [ ] Aplikasi bisa login sebagai admin pusat
- [ ] Menu data dimuat tanpa error
- [ ] Bisa menambah diskon baru tanpa permission error
- [ ] Form clears setelah berhasil simpan
- [ ] Data tersimpan dengan benar di database

## Error Messages yang Diperbaiki

### Sebelum:
- "permission denied for table diskon"
- "column does not exist"

### Sesudah:
- Tidak ada error permission
- Fitur diskon berfungsi dengan baik
- Data tersimpan dengan benar

## Hasil
Setelah perbaikan:
- Admin pusat bisa mengelola diskon tanpa error permission
- Tabel diskon tersedia dengan struktur yang benar
- Fitur manajemen diskon berfungsi penuh
- Data tersimpan dengan benar di database 