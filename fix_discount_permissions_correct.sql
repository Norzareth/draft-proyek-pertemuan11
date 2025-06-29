-- Fix permission issues for discount table with correct structure
-- This script should be run as a superuser (postgres)

-- Grant schema usage to squire
GRANT USAGE ON SCHEMA public TO squire;

-- Grant create table permission to squire
GRANT CREATE ON SCHEMA public TO squire;

-- Drop existing table if it has wrong structure
DROP TABLE IF EXISTS diskon;

-- Create discount table with correct structure
CREATE TABLE diskon (
    id_diskon SERIAL PRIMARY KEY,
    harga_baru INTEGER NOT NULL,
    deskripsi_diskon TEXT NOT NULL,
    id_menu INTEGER REFERENCES daftar_menu(id_menu) ON DELETE CASCADE
);

-- Grant all privileges on discount table to squire
GRANT ALL PRIVILEGES ON TABLE diskon TO squire;

-- Grant usage and select on the sequence
GRANT USAGE, SELECT ON SEQUENCE diskon_id_diskon_seq TO squire;

-- Insert sample discount data
INSERT INTO diskon (harga_baru, deskripsi_diskon, id_menu) VALUES 
(8000, 'Promo 20% off untuk menu favorit', 1),
(8500, 'Diskon 15% untuk pembelian pertama', 2)
ON CONFLICT DO NOTHING;

-- Verify permissions
SELECT 
    table_name,
    privilege_type,
    grantee
FROM information_schema.table_privileges 
WHERE table_name = 'diskon' AND grantee = 'squire';

-- Show current discount data
SELECT d.*, m.nama_menu 
FROM diskon d 
JOIN daftar_menu m ON d.id_menu = m.id_menu;

-- Show table structure
SELECT column_name, data_type, is_nullable
FROM information_schema.columns 
WHERE table_name = 'diskon' 
ORDER BY ordinal_position; 