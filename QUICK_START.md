# Quick Start Guide - BD Project

## 🚀 Cara Cepat Menjalankan Aplikasi

### 1. Setup Database
```bash
# Buat database PostgreSQL
CREATE DATABASE bd_project;

# Jalankan script setup
psql -d bd_project -f sqlnya.sql
psql -d bd_project -f complete_sample_data.sql
psql -d bd_project -f setup_admin_data.sql
```

### 2. Jalankan Aplikasi
```bash
# Windows
run_application.bat

# Atau manual
mvn clean javafx:run
```

### 3. Login Admin

**Admin Cabang:**
- Username: `admin_jak` / Password: `admin1` / Role: `Admin Cabang`
- Username: `admin_ban` / Password: `admin1` / Role: `Admin Cabang`
- Username: `admin_sur` / Password: `admin1` / Role: `Admin Cabang`

**Admin Pusat:**
- Username: `super_adm` / Password: `super1` / Role: `Admin Pusat`

## 🔧 Troubleshooting

### ❌ Login Gagal
1. **Jalankan setup database:**
   ```bash
   setup_database.bat
   ```

2. **Test koneksi database:**
   ```bash
   test_connection.bat
   ```

3. **Periksa PostgreSQL:**
   - Pastikan PostgreSQL berjalan di port 5432
   - Database: `bd_project`
   - User: `squire` / Password: `schoolstuff`

### ❌ Java/Maven Error
1. **Install Java 17+** dari https://adoptium.net/
2. **Install Maven** atau gunakan Maven wrapper
3. **Set JAVA_HOME** environment variable

### ❌ Database Connection Error
1. **Start PostgreSQL service**
2. **Buat database:**
   ```sql
   CREATE DATABASE bd_project;
   ```
3. **Periksa kredensial** di `DataSourceManager.java`

## 📁 File Penting

- `run_application.bat` - Jalankan aplikasi
- `setup_database.bat` - Setup database
- `test_connection.bat` - Test koneksi
- `ADMIN_LOGIN_INSTRUCTIONS.md` - Instruksi lengkap
- `verify_admin_data.sql` - Verifikasi data admin

## 🎯 Fitur Admin

**Admin Cabang:**
- Manajemen menu per cabang
- Manajemen pesanan
- Laporan cabang

**Admin Pusat:**
- Manajemen cabang & staff
- Laporan keseluruhan
- Analisis performa

## 📞 Support

Jika masih ada masalah:
1. Periksa console output untuk error detail
2. Jalankan `test_connection.bat` untuk verifikasi database
3. Pastikan semua file SQL sudah dijalankan
4. Periksa kredensial database di `DataSourceManager.java` 