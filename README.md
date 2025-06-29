# BD Project - Sistem Manajemen Katering

Sistem manajemen katering berbasis JavaFX dengan PostgreSQL untuk mengelola menu, pesanan, dan administrasi cabang.

## 🚀 Fitur Utama

### 👤 Customer
- Melihat daftar menu
- Memesan makanan
- Melihat riwayat pesanan
- Memberikan review

### 🏪 Admin Cabang
- **Manajemen Menu & Stok Harian**
  - Melihat menu dengan informasi stok real-time
  - Mengatur stok awal dan update stok harian
  - Status stok: Tersedia, Terbatas, Habis
  - Monitoring pergerakan stok

- **Pesanan & Pengiriman**
  - Melihat dan memproses pesanan masuk
  - Filter pesanan berdasarkan status dan tanggal
  - Update status pesanan (Belum Diproses → Dalam Proses → Selesai)
  - Jadwalkan pengiriman dengan tanggal dan waktu
  - Tracking status pengiriman

- **Laporan Cabang**
  - Generate laporan penjualan per periode
  - Analisis stok dan pergerakan inventory
  - Export laporan ke PDF/Excel

### 🏢 Admin Pusat
- **Performa Cabang**
  - Analisis performa semua cabang
  - Metrik: total pesanan, pendapatan, rata-rata pesanan
  - Skor performa dan pertumbuhan (%)
  - Status performa: Excellent, Good, Fair, Poor
  - Grafik dan visualisasi performa

- **Manajemen Promosi**
  - Promosi per cabang, kategori menu, atau global
  - Set persentase diskon dan minimal pembelian
  - Periode promosi (tanggal mulai-berakhir)
  - Aktifkan/nonaktifkan promosi
  - Monitoring efektivitas promosi

- **Manajemen Cabang & Staff**
  - Overview semua cabang
  - Informasi performa dan income cabang
  - Manajemen staff per cabang
  - Job description dan assignment

- **Laporan Keseluruhan**
  - Laporan penjualan komprehensif
  - Laporan performa cabang
  - Laporan staff dan keuangan
  - Export dan email laporan

## 🛠️ Teknologi

- **Frontend**: JavaFX 21
- **Backend**: Java 17
- **Database**: PostgreSQL 15+
- **Build Tool**: Maven
- **UI Framework**: FXML + CSS

## 📋 Prerequisites

- Java 17 atau lebih baru
- Maven 3.6+
- PostgreSQL 15+
- Git

## 🚀 Quick Start

### 1. Clone Repository
```bash
git clone <repository-url>
cd prokettBDyangItu
```

### 2. Setup Database
```bash
# Buat database PostgreSQL
createdb bd_project

# Jalankan script setup lengkap
setup_complete.bat

# Atau manual:
psql -d bd_project -f sqlnya.sql
psql -d bd_project -f complete_sample_data.sql
psql -d bd_project -f setup_admin_data.sql
psql -d bd_project -f add_promotion_tables.sql
```

### 3. Test Database Connection
```bash
test_connection.bat
```

### 4. Run Application
```bash
run_application.bat
```

## 👥 Login Credentials

### Customer
- `john_doe` / `pass1`
- `jane_smith` / `pass2`
- `bob_wilson` / `pass3`

### Admin Cabang
- `admin_jak` / `admin1` (Jakarta)
- `admin_ban` / `admin1` (Bandung)
- `admin_sur` / `admin1` (Surabaya)

### Admin Pusat
- `super_adm` / `super1` (Super Admin)

## 📊 Database Schema

### Tabel Utama
- `users` - Data pengguna (customer, admin)
- `cabang` - Data cabang katering
- `daftar_menu` - Menu makanan
- `pemesanan` - Data pesanan
- `pengiriman` - Data pengiriman
- `review` - Review customer

### Tabel Fitur Baru
- `promosi` - Data promosi dan diskon
- `stok_harian` - Stok menu per hari
- `jadwal_pengiriman` - Jadwal pengiriman
- `performa_cabang` - Performa cabang
- `staff_katering` - Data staff

## 🎯 Fitur Baru (v2.0)

### Admin Cabang Enhancements
- ✅ Manajemen stok harian real-time
- ✅ Proses pesanan dan update status
- ✅ Jadwal pengiriman otomatis
- ✅ Laporan cabang komprehensif
- ✅ Monitoring inventory

### Admin Pusat Enhancements
- ✅ Analisis performa multi-cabang
- ✅ Manajemen promosi fleksibel
- ✅ Dashboard analytics
- ✅ Laporan keseluruhan
- ✅ Manajemen cabang & staff

## 📁 Project Structure

```
prokettBDyangItu/
├── src/main/java/com/example/bdsqltester/
│   ├── scenes/
│   │   ├── admin/
│   │   │   ├── BranchAdminController.java    # Admin Cabang
│   │   │   └── CentralAdminController.java   # Admin Pusat
│   │   ├── user/
│   │   │   └── UserController.java           # Customer
│   │   └── LoginController.java              # Login
│   ├── datasources/
│   │   └── DataSourceManager.java            # Database connection
│   └── HelloApplication.java                 # Main app
├── src/main/resources/
│   └── com/example/bdsqltester/
│       ├── branch-admin-view.fxml            # Admin Cabang UI
│       ├── central-admin-view.fxml           # Admin Pusat UI
│       ├── user-view.fxml                    # Customer UI
│       └── login-view.fxml                   # Login UI
├── sqlnya.sql                               # Database schema
├── complete_sample_data.sql                 # Sample data
├── setup_admin_data.sql                     # Admin data
├── add_promotion_tables.sql                 # New feature tables
└── *.bat                                    # Setup scripts
```

## 🔧 Configuration

### Database Connection
Edit `src/main/java/com/example/bdsqltester/datasources/DataSourceManager.java`:

```java
private static final String DB_URL = "jdbc:postgresql://localhost:5432/bd_project";
private static final String DB_USER = "squire";
private static final String DB_PASSWORD = "schoolstuff";
```

### Application Properties
- Database URL: `jdbc:postgresql://localhost:5432/bd_project`
- Username: `squire`
- Password: `schoolstuff`
- Port: `5432`

## 🧪 Testing

### Automated Tests
```bash
# Test database connection
test_connection.bat

# Test new features
test_new_features.bat

# Test admin login
test_admin_login.bat
```

### Manual Testing
1. **Customer Flow**:
   - Login → Browse menu → Order → Review

2. **Admin Cabang Flow**:
   - Login → Manage stock → Process orders → Schedule delivery → Generate reports

3. **Admin Pusat Flow**:
   - Login → Analyze performance → Manage promotions → Generate overall reports

## 📈 Performance Metrics

### Admin Cabang KPIs
- Stok turnover rate
- Order processing time
- Delivery accuracy
- Customer satisfaction

### Admin Pusat KPIs
- Branch performance scores
- Revenue growth
- Promotion effectiveness
- Staff productivity

## 🔒 Security

- Role-based access control
- Password authentication
- Database connection security
- Input validation

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Failed**
   ```bash
   # Check PostgreSQL service
   pg_ctl status
   
   # Test connection
   test_connection.bat
   ```

2. **Login Failed**
   - Verify credentials in `setup_admin_data.sql`
   - Check role mapping in `LoginController.java`

3. **Features Not Working**
   - Run `add_promotion_tables.sql`
   - Check console for Java errors
   - Verify database schema

### Error Codes
- `DB001`: Database connection failed
- `AUTH001`: Authentication failed
- `ROLE001`: Role not found
- `FEATURE001`: Feature not available

## 📚 Documentation

- [Quick Start Guide](QUICK_START.md)
- [Admin Login Instructions](ADMIN_LOGIN_INSTRUCTIONS.md)
- [New Features Documentation](NEW_FEATURES.md)
- [Database Schema](sqlnya.sql)

## 🤝 Contributing

1. Fork the repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Create Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Authors

- **Your Name** - *Initial work* - [YourGitHub](https://github.com/yourusername)

## 🙏 Acknowledgments

- JavaFX Community
- PostgreSQL Documentation
- Maven Community

---

**Version**: 2.0  
**Last Updated**: January 2024  
**Status**: Production Ready ✅





