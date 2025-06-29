# Instruksi Login Admin

## Setup Database (PENTING!)

Sebelum mencoba login admin, pastikan database sudah disetup dengan benar:

### 1. Setup Database Structure
```bash
# Jalankan script untuk membuat tabel
psql -d bd_project -f sqlnya.sql

# Masukkan data sample
psql -d bd_project -f complete_sample_data.sql

# Setup data admin
psql -d bd_project -f setup_admin_data.sql
```

### 2. Test Database Connection
```bash
# Jalankan test koneksi
./test_connection.bat

# Atau manual test
javac -cp ".;postgresql-42.7.1.jar" TestDatabaseConnection.java
java -cp ".;postgresql-42.7.1.jar" TestDatabaseConnection
```

### 3. Verify Admin Data
```bash
# Jalankan query verifikasi
psql -d bd_project -f verify_admin_data.sql
```

## Admin Cabang (Branch Admin)

### Kredensial Login:
- **Username:** admin_jak, admin_ban, admin_sur
- **Email:** admin.jak@kat.com, admin.ban@kat.com, admin.sur@kat.com
- **Password:** admin1
- **Role:** Admin Cabang

### Fitur yang Tersedia:
1. **Manajemen Menu**
   - Melihat daftar menu per cabang
   - Menambah menu baru
   - Mengedit menu yang ada
   - Menghapus menu

2. **Manajemen Pesanan**
   - Melihat semua pesanan
   - Filter pesanan berdasarkan status
   - Update status pesanan
   - Lihat detail pesanan

3. **Laporan Cabang**
   - Generate laporan penjualan
   - Export laporan ke PDF/Excel
   - Analisis performa cabang

## Admin Pusat (Central Admin)

### Kredensial Login:
- **Username:** super_adm
- **Email:** admin@kat.com
- **Password:** super1
- **Role:** Admin Pusat

### Fitur yang Tersedia:
1. **Manajemen Cabang**
   - Melihat semua cabang
   - Menambah cabang baru
   - Mengedit informasi cabang
   - Menghapus cabang
   - Lihat detail performa cabang

2. **Manajemen Staff**
   - Melihat semua staff
   - Filter staff berdasarkan cabang
   - Menambah staff baru
   - Mengedit informasi staff
   - Menghapus staff

3. **Laporan Keseluruhan**
   - Laporan penjualan semua cabang
   - Laporan performa cabang
   - Laporan staff
   - Laporan keuangan
   - Export laporan ke PDF/Excel
   - Kirim laporan via email

4. **Analisis Performa**
   - Analisis pendapatan per cabang
   - Analisis jumlah pesanan
   - Analisis rata-rata nilai pesanan
   - Analisis performa staff
   - Grafik performa
   - Rekomendasi perbaikan

## Cara Login:

1. Jalankan aplikasi dengan `mvn clean javafx:run`
2. Masukkan username atau email admin
3. Masukkan password
4. **PENTING:** Pilih role yang sesuai (Admin Cabang atau Admin Pusat)
5. Klik tombol "Login"

## Troubleshooting

### ❌ Login Gagal - "Username, password, atau role tidak sesuai"

**Kemungkinan Penyebab:**
1. Database belum disetup dengan benar
2. Data admin tidak ada di database
3. Role yang dipilih tidak sesuai
4. Koneksi database bermasalah

**Solusi:**
1. **Jalankan setup database:**
   ```bash
   ./setup_database.bat
   ```

2. **Test koneksi database:**
   ```bash
   ./test_connection.bat
   ```

3. **Verifikasi data admin:**
   ```bash
   psql -d bd_project -f verify_admin_data.sql
   ```

4. **Periksa console output** untuk pesan error detail

### ❌ Database Connection Error

**Kemungkinan Penyebab:**
1. PostgreSQL tidak berjalan
2. Database `bd_project` tidak ada
3. Username/password database salah
4. Port 5432 tidak tersedia

**Solusi:**
1. **Start PostgreSQL service**
2. **Buat database jika belum ada:**
   ```sql
   CREATE DATABASE bd_project;
   ```
3. **Periksa kredensial di `DataSourceManager.java`:**
   ```java
   config.setJdbcUrl("jdbc:postgresql://localhost:5432/bd_project");
   config.setUsername("squire");
   config.setPassword("schoolstuff");
   ```

### ❌ Role Mapping Error

**Kemungkinan Penyebab:**
1. Role di database berbeda dengan UI
2. Mapping role tidak benar

**Solusi:**
- Database role: `AdminCab`, `AdminPus`
- UI role: `Admin Cabang`, `Admin Pusat`
- Mapping sudah diperbaiki di `LoginController.java`

## Verifikasi Data Admin

Jalankan query berikut di database untuk memastikan data admin ada:

```sql
-- Cek role admin
SELECT * FROM role WHERE jenis_user IN ('AdminCab', 'AdminPus');

-- Cek user admin
SELECT u.user_id, u.username, u.email, u.password, r.jenis_user
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE r.jenis_user IN ('AdminCab', 'AdminPus')
ORDER BY r.jenis_user, u.username;
```

## Catatan:
- Admin Cabang hanya dapat mengakses data cabang tertentu
- Admin Pusat dapat mengakses semua data dan cabang
- Semua fitur CRUD (Create, Read, Update, Delete) sudah tersedia
- Fitur export dan analisis lanjutan akan diimplementasikan sesuai kebutuhan
- Pastikan PostgreSQL berjalan di port 5432
- Database harus bernama `bd_project`
- User database: `squire` dengan password `schoolstuff` 