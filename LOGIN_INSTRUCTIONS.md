# 🔐 LOGIN TROUBLESHOOTING GUIDE

## Masalah: Admin tidak bisa login

### Penyebab Masalah
Masalah terjadi karena ketidakcocokan antara nama role di UI dan di database:
- **UI menampilkan:** "Admin Cabang", "Admin Pusat"
- **Database menyimpan:** "AdminCab", "AdminPus"

### ✅ Solusi yang Sudah Diterapkan

1. **LoginController.java sudah diperbaiki** dengan menambahkan fungsi mapping:
   ```java
   private String mapUIRoleToDBRole(String uiRole) {
       switch (uiRole) {
           case "Customer":
               return "Customer";
           case "Admin Cabang":
               return "AdminCab";
           case "Admin Pusat":
               return "AdminPus";
           default:
               return uiRole;
       }
   }
   ```

### 🧪 Cara Test Login

#### 1. Jalankan SQL Verification
```bash
psql -U postgres -d bd_project -f verify_login_data.sql
```

#### 2. Test dengan Java Application
```bash
# Compile dan run aplikasi
mvn clean compile
mvn javafx:run
```

#### 3. Gunakan Credentials yang Benar

**Admin Cabang:**
- Username: `admin_jak`, Password: `admin1`, Role: `Admin Cabang`
- Username: `admin_ban`, Password: `admin1`, Role: `Admin Cabang`
- Username: `admin_sur`, Password: `admin1`, Role: `Admin Cabang`

**Admin Pusat:**
- Username: `super_adm`, Password: `super1`, Role: `Admin Pusat`

**Customer:**
- Username: `john_doe`, Password: `pass1`, Role: `Customer`
- Username: `jane_smith`, Password: `pass2`, Role: `Customer`

### 🔍 Debug Steps

#### Step 1: Pastikan Data Ada di Database
```sql
-- Cek apakah data users ada
SELECT u.username, u.password, r.jenis_user
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE r.jenis_user IN ('AdminCab', 'AdminPus')
ORDER BY r.jenis_user, u.user_id;
```

#### Step 2: Test Login Manual
```sql
-- Test login admin_jak
SELECT 
    u.username,
    u.password,
    r.jenis_user,
    CASE 
        WHEN u.username = 'admin_jak' AND u.password = 'admin1' AND r.jenis_user = 'AdminCab' 
        THEN 'LOGIN SUCCESS'
        ELSE 'LOGIN FAILED'
    END as login_status
FROM users u
JOIN role r ON u.id_role = r.id_role
WHERE u.username = 'admin_jak';
```

#### Step 3: Cek Console Output
Saat login gagal, cek console untuk melihat:
- Expected role vs actual role
- Password match status
- Database connection status

### 🚨 Jika Masih Gagal

#### 1. Pastikan Database Terisi
```bash
# Jalankan sample data jika belum
psql -U postgres -d bd_project -f complete_sample_data.sql
```

#### 2. Cek Database Connection
```java
// Test di DatabaseHelper.java
if (DatabaseHelper.testConnection()) {
    System.out.println("Database OK");
} else {
    System.out.println("Database connection failed");
}
```

#### 3. Verifikasi Role Names
```sql
-- Pastikan role names sesuai
SELECT * FROM role ORDER BY id_role;
```

### 📋 Checklist Login

- [ ] Database `bd_project` sudah dibuat
- [ ] Schema `sqlnya.sql` sudah dijalankan
- [ ] Sample data `complete_sample_data.sql` sudah dijalankan
- [ ] Database connection berhasil
- [ ] Username dan password sesuai
- [ ] Role yang dipilih sesuai dengan data di database
- [ ] LoginController.java sudah diupdate

### 🎯 Expected Result

Setelah perbaikan, login admin seharusnya berhasil dengan:
- **Admin Cabang:** Username `admin_jak`, Password `admin1`, Role `Admin Cabang`
- **Admin Pusat:** Username `super_adm`, Password `super1`, Role `Admin Pusat`

### 📞 Jika Masih Bermasalah

1. Jalankan `verify_login_data.sql` dan share hasilnya
2. Cek console output saat login gagal
3. Pastikan semua file sudah diupdate dan di-compile ulang 