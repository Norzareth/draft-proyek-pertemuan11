# Troubleshooting Admin Controller Error

## 🔍 Error yang Terjadi

```
javafx.fxml.LoadException: 
Caused by: java.lang.IllegalAccessException: class javafx.fxml.FXMLLoader$ValueElement (in module javafx.fxml) cannot access class com.example.bdsqltester.scenes.admin.BranchAdminController (in module com.example.bdsqltester) because module com.example.bdsqltester does not export com.example.bdsqltester.scenes.admin to module javafx.fxml
```

## 🎯 Penyebab Error

Error ini terjadi karena **Java Module System** tidak mengizinkan FXML loader mengakses controller yang berada di package `com.example.bdsqltester.scenes.admin` karena package tersebut tidak di-export dan di-open di `module-info.java`.

## ✅ Solusi yang Telah Diterapkan

### 1. Update module-info.java
```java
module com.example.bdsqltester {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.zaxxer.hikari;
    requires java.sql;
    requires org.slf4j;

    opens com.example.bdsqltester to javafx.fxml;
    exports com.example.bdsqltester;
    exports com.example.bdsqltester.datasources;
    opens com.example.bdsqltester.datasources to javafx.fxml;
    exports com.example.bdsqltester.scenes;
    opens com.example.bdsqltester.scenes to javafx.fxml;

    exports com.example.bdsqltester.scenes.user;
    opens com.example.bdsqltester.scenes.user to javafx.fxml;

    // ✅ TAMBAHAN INI
    exports com.example.bdsqltester.scenes.admin;
    opens com.example.bdsqltester.scenes.admin to javafx.fxml;

    exports com.example.bdsqltester.dtos;
    opens com.example.bdsqltester.dtos to javafx.base;
}
```

### 2. Rebuild Project
```bash
# Clean dan compile ulang
mvn clean compile

# Atau gunakan script yang sudah dibuat
fix_admin_error.bat
```

## 🚀 Cara Test Setelah Fix

### Test Admin Cabang
1. **Login**: `admin_jak` / `admin1`
2. **Role**: Pilih "Admin Cabang"
3. **Expected**: Aplikasi akan load tanpa error
4. **Features yang bisa ditest**:
   - Tab "Manajemen Menu & Stok"
   - Tab "Pesanan & Pengiriman"
   - Tab "Laporan"

### Test Admin Pusat
1. **Login**: `super_adm` / `super1`
2. **Role**: Pilih "Admin Pusat"
3. **Expected**: Aplikasi akan load tanpa error
4. **Features yang bisa ditest**:
   - Tab "Performa Cabang"
   - Tab "Manajemen Promosi"
   - Tab "Manajemen Cabang"
   - Tab "Manajemen Staff"
   - Tab "Laporan Keseluruhan"

## 🔧 Jika Masih Error

### 1. Check Java Version
```bash
java -version
# Harus Java 17 atau lebih baru
```

### 2. Check Maven
```bash
mvn -version
# Harus Maven 3.6+
```

### 3. Clean dan Rebuild
```bash
# Clean target folder
mvn clean

# Compile ulang
mvn compile

# Run aplikasi
mvn javafx:run
```

### 4. Check File Structure
Pastikan file controller ada di lokasi yang benar:
```
src/main/java/com/example/bdsqltester/scenes/admin/
├── BranchAdminController.java
└── CentralAdminController.java
```

### 5. Check FXML Files
Pastikan FXML files mengacu ke controller yang benar:
```xml
<!-- branch-admin-view.fxml -->
<VBox fx:controller="com.example.bdsqltester.scenes.admin.BranchAdminController">

<!-- central-admin-view.fxml -->
<VBox fx:controller="com.example.bdsqltester.scenes.admin.CentralAdminController">
```

## 📋 Checklist Troubleshooting

- [ ] `module-info.java` sudah diupdate dengan export admin package
- [ ] Project sudah di-clean dan recompile
- [ ] Java version 17+
- [ ] Maven version 3.6+
- [ ] Controller files ada di lokasi yang benar
- [ ] FXML files mengacu ke controller yang benar
- [ ] Database connection berfungsi
- [ ] Admin credentials sudah benar

## 🎯 Expected Behavior Setelah Fix

### Admin Cabang Dashboard
- ✅ Load tanpa error
- ✅ Tampil 3 tab: Menu & Stok, Pesanan & Pengiriman, Laporan
- ✅ Bisa melihat menu dengan stok
- ✅ Bisa update stok
- ✅ Bisa proses pesanan
- ✅ Bisa jadwalkan pengiriman

### Admin Pusat Dashboard
- ✅ Load tanpa error
- ✅ Tampil 5 tab: Performa Cabang, Manajemen Promosi, Manajemen Cabang, Manajemen Staff, Laporan Keseluruhan
- ✅ Bisa analisis performa cabang
- ✅ Bisa tambah/edit promosi
- ✅ Bisa generate laporan

## 📞 Jika Masih Bermasalah

1. **Check Console Output**: Lihat error message lengkap
2. **Check Database**: Pastikan tabel baru sudah dibuat
3. **Check Dependencies**: Pastikan semua dependencies terinstall
4. **Restart IDE**: Kadang IDE perlu di-restart setelah perubahan module-info

---

**Status**: ✅ Fixed  
**Last Updated**: January 2024  
**Tested**: Admin Cabang & Admin Pusat login 