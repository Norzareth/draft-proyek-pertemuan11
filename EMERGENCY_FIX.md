# 🚨 EMERGENCY FIX - Admin Controller Error

## 🔍 Masalah yang Terjadi
```
javafx.fxml.LoadException: 
Caused by: java.lang.IllegalAccessException: class javafx.fxml.FXMLLoader$ValueElement (in module javafx.fxml) cannot access class com.example.bdsqltester.scenes.admin.BranchAdminController
```

## ✅ SOLUSI CEPAT

### 1. **Update module-info.java** ✅ DONE
File `src/main/java/module-info.java` sudah diupdate dengan:
```java
exports com.example.bdsqltester.scenes.admin;
opens com.example.bdsqltester.scenes.admin to javafx.fxml;
```

### 2. **Simplified Controllers** ✅ DONE
- `BranchAdminController.java` - Versi yang lebih sederhana
- `CentralAdminController.java` - Versi yang lebih sederhana

### 3. **Cara Menjalankan Aplikasi**

#### Option A: Dengan IDE (Recommended)
1. Buka project di IntelliJ IDEA atau Eclipse
2. Clean dan rebuild project
3. Run `HelloApplication.java`

#### Option B: Dengan Maven (Jika tersedia)
```bash
mvn clean compile
mvn javafx:run
```

#### Option C: Manual (Jika JavaFX SDK terinstall)
```bash
run_without_maven.bat
```

## 🎯 **TEST SETELAH FIX**

### Admin Cabang Test
1. **Login**: `admin_jak` / `admin1`
2. **Role**: Pilih "Admin Cabang"
3. **Expected**: 
   - ✅ Dashboard load tanpa error
   - ✅ Tampil pesan "Admin Cabang Dashboard berhasil dimuat!"
   - ✅ 3 tab: Menu & Stok, Pesanan & Pengiriman, Laporan

### Admin Pusat Test
1. **Login**: `super_adm` / `super1`
2. **Role**: Pilih "Admin Pusat"
3. **Expected**:
   - ✅ Dashboard load tanpa error
   - ✅ Tampil pesan "Admin Pusat Dashboard berhasil dimuat!"
   - ✅ 5 tab: Performa Cabang, Manajemen Promosi, Manajemen Cabang, Manajemen Staff, Laporan Keseluruhan

## 🔧 **TROUBLESHOOTING LANJUTAN**

### Jika Masih Error:

#### 1. **Check Java Version**
```bash
java -version
# Harus Java 17 atau lebih baru
```

#### 2. **Check JAVA_HOME**
```bash
echo %JAVA_HOME%
# Harus mengarah ke Java 17+
```

#### 3. **Check Maven**
```bash
mvn -version
# Jika tidak ada, install Maven atau gunakan IDE
```

#### 4. **Check Database**
```bash
# Pastikan PostgreSQL running
# Pastikan database 'bd_project' ada
# Pastikan user 'squire' dengan password 'schoolstuff'
```

#### 5. **Check File Structure**
```
src/main/java/com/example/bdsqltester/scenes/admin/
├── BranchAdminController.java    ✅ Ada
└── CentralAdminController.java   ✅ Ada

src/main/resources/com/example/bdsqltester/
├── branch-admin-view.fxml        ✅ Ada
└── central-admin-view.fxml       ✅ Ada
```

## 🚀 **ALTERNATIVE SOLUTIONS**

### Solution 1: Use IDE
1. **IntelliJ IDEA**:
   - File → Open → pilih folder project
   - Build → Rebuild Project
   - Run → Run 'HelloApplication'

2. **Eclipse**:
   - File → Import → Existing Maven Projects
   - Clean Project
   - Run As → Java Application

### Solution 2: Manual Compile
```bash
# Compile semua Java files
javac -cp "lib/*" -d target/classes src/main/java/com/example/bdsqltester/**/*.java

# Run application
java -cp "target/classes;lib/*" com.example.bdsqltester.HelloApplication
```

### Solution 3: Check Dependencies
Pastikan file `lib/` berisi:
- `postgresql-42.7.1.jar`
- `HikariCP-5.0.1.jar`
- `slf4j-api-2.0.7.jar`

## 📋 **CHECKLIST VERIFICATION**

- [ ] `module-info.java` sudah diupdate
- [ ] `BranchAdminController.java` ada dan benar
- [ ] `CentralAdminController.java` ada dan benar
- [ ] FXML files mengacu ke controller yang benar
- [ ] Database connection berfungsi
- [ ] Java 17+ terinstall
- [ ] Project di-compile ulang

## 🎯 **EXPECTED BEHAVIOR**

### Setelah Fix Berhasil:
1. **Login Admin Cabang** → Dashboard load dengan 3 tab
2. **Login Admin Pusat** → Dashboard load dengan 5 tab
3. **Tidak ada error FXML LoadException**
4. **Semua fitur dasar berfungsi** (view data, basic operations)

### Fitur yang Bisa Ditest:
- ✅ View menu dengan stok
- ✅ View pesanan
- ✅ Generate laporan
- ✅ View performa cabang
- ✅ View promosi
- ✅ Basic CRUD operations

## 📞 **JIKA MASIH BERMASALAH**

1. **Check Console Output**: Lihat error message lengkap
2. **Check IDE Logs**: Lihat error di IDE
3. **Restart IDE**: Kadang perlu restart setelah perubahan
4. **Check File Permissions**: Pastikan file bisa diakses
5. **Check Antivirus**: Kadang antivirus memblokir file

---

**Status**: ✅ Emergency Fix Applied  
**Last Updated**: January 2024  
**Priority**: HIGH 