-- Add menu discount functionality
-- Create table for menu discounts
CREATE TABLE IF NOT EXISTS diskon_menu (
    id_diskon_menu SERIAL PRIMARY KEY,
    id_menu INT REFERENCES daftar_menu(id_menu),
    persentase_diskon INT NOT NULL,
    deskripsi_promo VARCHAR(100),
    tanggal_mulai DATE NOT NULL,
    tanggal_selesai DATE NOT NULL,
    status_aktif BOOLEAN DEFAULT true
);

-- Insert sample discount data
INSERT INTO diskon_menu (id_menu, persentase_diskon, deskripsi_promo, tanggal_mulai, tanggal_selesai) VALUES
(1, 20, 'Promo Awal Tahun', CURRENT_DATE, CURRENT_DATE + INTERVAL '30 days'),
(2, 15, 'Diskon Spesial', CURRENT_DATE, CURRENT_DATE + INTERVAL '15 days'),
(3, 25, 'Promo Weekend', CURRENT_DATE, CURRENT_DATE + INTERVAL '7 days');

-- Create function to calculate discounted price
CREATE OR REPLACE FUNCTION get_discounted_price(menu_id INT)
RETURNS INT AS $$
DECLARE
    original_price INT;
    discount_percentage INT;
    discounted_price INT;
BEGIN
    -- Get original price
    SELECT harga INTO original_price 
    FROM daftar_menu 
    WHERE id_menu = menu_id;
    
    -- Get active discount
    SELECT persentase_diskon INTO discount_percentage
    FROM diskon_menu 
    WHERE id_menu = menu_id 
    AND status_aktif = true 
    AND CURRENT_DATE BETWEEN tanggal_mulai AND tanggal_selesai
    ORDER BY persentase_diskon DESC
    LIMIT 1;
    
    -- Calculate discounted price
    IF discount_percentage IS NOT NULL THEN
        discounted_price := original_price - (original_price * discount_percentage / 100);
    ELSE
        discounted_price := original_price;
    END IF;
    
    RETURN discounted_price;
END;
$$ LANGUAGE plpgsql;

-- Create function to get discount info
CREATE OR REPLACE FUNCTION get_discount_info(menu_id INT)
RETURNS TABLE(
    original_price INT,
    discounted_price INT,
    discount_percentage INT,
    promo_description VARCHAR(100)
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        dm.harga as original_price,
        get_discounted_price(menu_id) as discounted_price,
        dmenu.persentase_diskon as discount_percentage,
        dmenu.deskripsi_promo as promo_description
    FROM daftar_menu dm
    LEFT JOIN diskon_menu dmenu ON dm.id_menu = dmenu.id_menu
    WHERE dm.id_menu = menu_id
    AND (dmenu.status_aktif IS NULL OR dmenu.status_aktif = true)
    AND (dmenu.tanggal_mulai IS NULL OR CURRENT_DATE BETWEEN dmenu.tanggal_mulai AND dmenu.tanggal_selesai)
    ORDER BY dmenu.persentase_diskon DESC
    LIMIT 1;
END;
$$ LANGUAGE plpgsql; 