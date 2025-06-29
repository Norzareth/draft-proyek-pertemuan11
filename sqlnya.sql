-- 1. Create base tables with no dependencies first
create table performa_cabang (
	id_performa_cabang serial primary key,
	tingkat_performa int,
	income int
);

create table cabang (
	cabang_id serial primary key,
	lokasi varchar(20),
	id_performa_cabang int references performa_cabang(id_performa_cabang)
);

create table penjual_sampingan (
	id_penjual serial primary key,
	nama_penjual varchar(30) not null,
	kontak varchar(30) not null
);

create table role (
	id_role serial primary key,
	jenis_user varchar(10) not null,
	jenis_akses varchar(10) not null
);

-- 2. Tables referencing the above
create table daftar_menu (
	id_menu serial primary key,
	nama_menu varchar(50),
	jenis varchar(20),
	id_penjual int references penjual_sampingan(id_penjual),
	harga int,
	cabang_id int references cabang(cabang_id)
);

create table users (
	user_id serial primary key,
	password varchar(8) not null, -- increased length for security
	email varchar(50) not null,
	username varchar(20) not null,
	id_role int references role(id_role)
);

create table pengiriman (
	pengiriman_id serial primary key,
	status_pengiriman varchar(20) not null,
	jadwal_kirim date not null,
	estimasi_sampai date not null
);

create table pemesanan (
	pesanan_id serial primary key,
	tanggal_pesanan date not null,
	status varchar(20) not null,
	user_id int references users(user_id),
	pengiriman_id int references pengiriman(pengiriman_id)
);

create table rating (
	id_rating serial primary key,
	comment varchar(200),
	nilai_rating int,
	user_id int references users(user_id),
	pesanan_id int references pemesanan(pesanan_id)
);

create table diskon (
	id_diskon serial primary key,
	persentase_diskon int,
	syarat_diskon varchar(50),
	pesanan_id int references pemesanan(pesanan_id)
);

create table pesanan_menu (
	pesanan_id int references pemesanan(pesanan_id),
	id_menu int references daftar_menu(id_menu)
);

create table staff_katering (
	id_staff serial primary key,
	nama varchar(20),
	job_desc varchar(100),
	cabang_id int references cabang(cabang_id)
);
