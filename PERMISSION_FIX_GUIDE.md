# Panduan Perbaikan Permission Tabel Diskon

## Masalah
Error yang muncul:
```
ERROR: permission denied for schema public
ERROR: permission denied for table diskon
```

## Penyebab
User `squire` tidak memiliki permission yang cukup untuk:
1. Membuat tabel di schema public
2. Mengakses tabel diskon
3. Melakukan INSERT/UPDATE/DELETE pada tabel diskon

## Solusi

### Langkah 1: Jalankan Script Perbaikan Permission
Jalankan script sebagai PostgreSQL superuser:
```bash
fix_discount_permissions.bat
```

**Catatan:** Script ini menggunakan user `postgres` (superuser) untuk memberikan permission ke user `squire`.

### Langkah 2: Verifikasi Permission
Script akan:
1. Memberikan USAGE dan CREATE permission pada schema public
2. Membuat tabel diskon jika belum ada
3. Memberikan ALL PRIVILEGES pada tabel diskon
4. Memberikan permission pada sequence
5. Menambahkan sample data
6. Menampilkan verifikasi permission

### Langkah 3: Test Aplikasi
Jalankan script test:
```bash
test_discount_after_fix.bat
```

## Struktur Tabel yang Dibuat

```sql
CREATE TABLE diskon (
    id_diskon SERIAL PRIMARY KEY,
    persentase_diskon INTEGER NOT NULL,
    syarat_diskon TEXT NOT NULL,
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
\i fix_discount_permissions.sql
```

### Verifikasi Manual:
```sql
-- Check if table exists
SELECT * FROM diskon LIMIT 1;

-- Check permissions
SELECT 
    table_name,
    privilege_type,
    grantee
FROM information_schema.table_privileges 
WHERE table_name = 'diskon' AND grantee = 'squire';
```

## Testing Checklist

Setelah menjalankan script perbaikan:

- [ ] Script berhasil dijalankan tanpa error
- [ ] Tabel diskon ada di database
- [ ] User squire memiliki permission yang cukup
- [ ] Aplikasi bisa login sebagai admin pusat
- [ ] Menu data dimuat tanpa error
- [ ] Bisa menambah diskon baru
- [ ] Bisa mengedit diskon yang ada
- [ ] Bisa menghapus diskon
- [ ] Tidak ada error permission denied

## Error Messages yang Diperbaiki

### Sebelum:
- "permission denied for schema public"
- "permission denied for table diskon"

### Sesudah:
- Error yang lebih informatif dengan instruksi perbaikan
- Graceful handling untuk permission issues
- User-friendly error messages

## Hasil
Setelah perbaikan:
- Admin pusat bisa mengelola diskon tanpa error
- Tabel diskon tersedia dengan permission yang benar
- Fitur manajemen diskon berfungsi penuh
- Error handling yang lebih baik 