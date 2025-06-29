# Fix Discount Table Permission Issue

## Masalah
Setelah login sebagai admin pusat, muncul error:
```
Gagal memuat daftar menu
Error: permission denied for table diskon
```

## Penyebab
Tabel `diskon` belum ada atau user `squire` tidak memiliki permission untuk mengakses tabel tersebut.

## Solusi

### Langkah 1: Jalankan Script Perbaikan
Jalankan salah satu script berikut:

**Opsi A (dengan password prompt):**
```bash
fix_discount_table.bat
```

**Opsi B (tanpa password prompt):**
```bash
fix_discount_table_no_pwd.bat
```

### Langkah 2: Verifikasi Perbaikan
Script akan:
1. Memeriksa apakah tabel `diskon` sudah ada
2. Membuat tabel jika belum ada
3. Memberikan permission yang diperlukan ke user `squire`
4. Menambahkan sample data diskon
5. Menampilkan data diskon yang ada

### Langkah 3: Test Aplikasi
Jalankan script test:
```bash
test_central_admin_fixed.bat
```

## Struktur Tabel Diskon

### Schema
```sql
CREATE TABLE diskon (
    id_diskon SERIAL PRIMARY KEY,
    persentase_diskon INTEGER NOT NULL,
    syarat_diskon TEXT NOT NULL,
    id_menu INTEGER REFERENCES daftar_menu(id_menu) ON DELETE CASCADE
);
```

### Permission
```sql
GRANT ALL PRIVILEGES ON TABLE diskon TO squire;
GRANT USAGE, SELECT ON SEQUENCE diskon_id_diskon_seq TO squire;
```

## Sample Data
Script akan menambahkan sample data:
- Menu ID 1: Diskon 20% - "Promo 20% off untuk menu favorit"
- Menu ID 2: Diskon 15% - "Diskon 15% untuk pembelian pertama"

## Troubleshooting

### Jika masih error:
1. Pastikan PostgreSQL berjalan
2. Pastikan kredensial database benar (username: squire, password: schoolstuff)
3. Pastikan database `bd_project` ada
4. Coba jalankan script SQL manual di pgAdmin atau psql

### Jika tabel sudah ada tapi masih error:
1. Periksa permission user `squire`
2. Jalankan: `GRANT ALL PRIVILEGES ON TABLE diskon TO squire;`
3. Jalankan: `GRANT USAGE, SELECT ON SEQUENCE diskon_id_diskon_seq TO squire;`

## Testing
Setelah perbaikan, test fitur berikut:
1. Login admin pusat
2. Buka tab "Manajemen Menu"
3. Verifikasi data menu dan diskon dimuat
4. Test fitur manajemen diskon
5. Buka tab "Performa Cabang"
6. Verifikasi data performa dimuat 