# BD Project - Sistem Katering

## Deskripsi
Sistem katering dengan fitur login multi-role untuk Customer, Admin Cabang, dan Admin Pusat.

## Struktur Database

### Tabel Users dan Role
```sql
create table users (
	user_id serial primary key,
	password varchar(8) not null,
	email varchar(20) not null,
	username varchar (20) not null,
	id_role int references role(id_role)
);

create table role (
	id_role serial primary key,
	jenis_user varchar (10) not null,
	jenis_akses varchar (10) not null
);
```

### Tabel Rating dan Pemesanan
```sql
create table rating (
	id_rating serial primary key,
	comment varchar (200),
	nilai_rating int,
	user_id int references users (user_id),
	pesanan_id int references pemesanan(pesanan_id)	
);

create table pemesanan (
	pesanan_id serial primary key,
	tanggal_pesanan date not null,
	status varchar (20) not null,
	user_id int references users (user_id),
	pengiriman_id int references pengiriman(pengiriman_id)
);
```

### Tabel Pengiriman dan Diskon
```sql
create table pengiriman (
	pengiriman_id serial primary key,
	status_pengiriman varchar (20) not null,
	jadwal_kirim date not null,
	estimasi_sampai date not null
);

create table diskon (
	id_diskon serial primary key,
	persentase_diskon int,
	syarat_diskon varchar (50),
	pesanan_id int references pemesanan (pesanan_id)
);
```

### Tabel Menu dan Penjual
```sql
create table pesanan_menu (
	pesanan_id int references pemesanan(pesanan_id),
	id_menu int references daftar_menu(id_menu)
);

create table penjual_sampingan (
	id_penjual serial primary key,
	nama_penjual varchar(30) not null,
	kontak varchar(30) not null
);

create table daftar_menu (
	id_menu serial primary key,
	nama_menu varchar (50),
	jenis varchar (20),
	id_penjual int references penjual_sampingan(id_penjual),
	harga int,
	cabang_id int references cabang(cabang_id)
);
```

### Tabel Cabang dan Staff
```sql
create table cabang(
	cabang_id serial primary key,
	lokasi varchar(20),
	id_performa_cabang int references performa_cabang(id_performa_cabang)
);

create table performa_cabang (
	id_performa_cabang serial primary key,
	tingkat_performa int,
	income int
);

create table staff_katering (
	id_staff serial primary key,
	nama varchar(20),
	job_desc varchar(100),
	cabang_id int references cabang(cabang_id)
);
```

## Sistem Login Multi-Role

### 1. Customer
- **Fitur:** Melihat menu, memesan makanan, melihat riwayat pesanan, memberikan rating
- **File:** `user-view.fxml`, `UserController.java`

### 2. Admin Cabang
- **Fitur:** 
  - Manajemen menu (CRUD)
  - Manajemen pesanan
  - Laporan cabang
  - Export laporan
- **File:** `branch-admin-view.fxml`, `BranchAdminController.java`
- **Kredensial:**
  - admin_jak / admin1 (Jakarta)
  - admin_ban / admin1 (Bandung)
  - admin_sur / admin1 (Surabaya)

### 3. Admin Pusat
- **Fitur:**
  - Manajemen cabang (CRUD)
  - Manajemen staff (CRUD)
  - Laporan keseluruhan
  - Analisis performa
  - Export dan email laporan
- **File:** `central-admin-view.fxml`, `CentralAdminController.java`
- **Kredensial:** super_adm / super1

## Cara Menjalankan

1. **Setup Database:**
   ```bash
   # Jalankan script SQL untuk membuat tabel
   psql -d your_database -f sqlnya.sql
   
   # Masukkan data sample
   psql -d your_database -f complete_sample_data.sql
   ```

2. **Jalankan Aplikasi:**
   ```bash
   # Menggunakan Maven
   mvn clean javafx:run
   
   # Atau menggunakan Maven wrapper
   ./mvnw clean javafx:run
   ```

3. **Testing Login:**
   ```bash
   # Test customer login
   ./test_login.bat
   
   # Test admin login
   ./test_admin_login.bat
   ```

## File Penting

### Views (FXML)
- `login-view.fxml` - Halaman login untuk semua role
- `user-view.fxml` - Dashboard customer
- `branch-admin-view.fxml` - Dashboard admin cabang
- `central-admin-view.fxml` - Dashboard admin pusat

### Controllers
- `LoginController.java` - Controller untuk login
- `UserController.java` - Controller untuk customer
- `BranchAdminController.java` - Controller untuk admin cabang
- `CentralAdminController.java` - Controller untuk admin pusat

### Data
- `complete_sample_data.sql` - Data sample lengkap
- `verify_login_data.sql` - Query verifikasi data customer
- `verify_admin_data.sql` - Query verifikasi data admin

### Dokumentasi
- `LOGIN_INSTRUCTIONS.md` - Instruksi login customer
- `ADMIN_LOGIN_INSTRUCTIONS.md` - Instruksi login admin

## Fitur Utama

### Customer Features
- ✅ Login dengan username/email
- ✅ Melihat menu dengan filter cabang dan kategori
- ✅ Memesan makanan dengan tanggal pengiriman
- ✅ Melihat riwayat pesanan
- ✅ Memberikan rating dan komentar

### Admin Cabang Features
- ✅ Login dengan role Admin Cabang
- ✅ Manajemen menu (tambah, edit, hapus)
- ✅ Manajemen pesanan (lihat, update status)
- ✅ Laporan penjualan cabang
- ✅ Export laporan (akan diimplementasikan)

### Admin Pusat Features
- ✅ Login dengan role Admin Pusat
- ✅ Manajemen cabang (tambah, edit, hapus)
- ✅ Manajemen staff (tambah, edit, hapus)
- ✅ Laporan keseluruhan sistem
- ✅ Analisis performa cabang
- ✅ Export dan email laporan (akan diimplementasikan)

## Teknologi
- **JavaFX** - UI Framework
- **PostgreSQL** - Database
- **Maven** - Build Tool
- **JDBC** - Database Connection

## Struktur Project
```
src/
├── main/
│   ├── java/
│   │   └── com/example/bdsqltester/
│   │       ├── scenes/
│   │       │   ├── admin/
│   │       │   │   ├── BranchAdminController.java
│   │       │   │   └── CentralAdminController.java
│   │       │   ├── user/
│   │       │   │   └── UserController.java
│   │       │   └── LoginController.java
│   │       └── datasources/
│   │           └── DataSourceManager.java
│   └── resources/
│       └── com/example/bdsqltester/
│           ├── login-view.fxml
│           ├── user-view.fxml
│           ├── branch-admin-view.fxml
│           └── central-admin-view.fxml
```





