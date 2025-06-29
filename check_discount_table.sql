-- Check if discount table exists
SELECT EXISTS (
    SELECT FROM information_schema.tables 
    WHERE table_schema = 'public' 
    AND table_name = 'diskon'
);

-- Create discount table if it doesn't exist
CREATE TABLE IF NOT EXISTS diskon (
    id_diskon SERIAL PRIMARY KEY,
    persentase_diskon INTEGER NOT NULL,
    syarat_diskon TEXT NOT NULL,
    id_menu INTEGER REFERENCES daftar_menu(id_menu) ON DELETE CASCADE
);

-- Grant permissions to the user
GRANT ALL PRIVILEGES ON TABLE diskon TO squire;
GRANT USAGE, SELECT ON SEQUENCE diskon_id_diskon_seq TO squire;

-- Insert sample discount data
INSERT INTO diskon (persentase_diskon, syarat_diskon, id_menu) VALUES 
(20, 'Promo 20% off untuk menu favorit', 1),
(15, 'Diskon 15% untuk pembelian pertama', 2)
ON CONFLICT DO NOTHING;

-- Show current discount data
SELECT d.*, m.nama_menu 
FROM diskon d 
JOIN daftar_menu m ON d.id_menu = m.id_menu; 