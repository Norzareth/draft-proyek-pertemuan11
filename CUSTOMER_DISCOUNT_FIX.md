# Customer Discount Column Fix

## Problem
Customer view was showing error: `column d.persentase_diskon does not exist` because the SQL query was still using old column names after the database schema was updated.

## Root Cause
The discount table structure was changed from:
- `persentase_diskon` (percentage discount) → `harga_baru` (new fixed price)
- `syarat_diskon` (discount terms) → `deskripsi_diskon` (discount description)

But the UserController.java was still using the old column names in the SQL query.

## Solution Applied

### 1. Updated SQL Query in UserController.java
**File:** `src/main/java/com/example/bdsqltester/scenes/user/UserController.java`
**Method:** `loadMenu()`

**Before:**
```sql
COALESCE(d.persentase_diskon, 0) as persentase_diskon,
d.syarat_diskon,
CASE 
    WHEN d.persentase_diskon IS NOT NULL 
    THEN m.harga - (m.harga * d.persentase_diskon / 100)
    ELSE m.harga 
END as harga_diskon,
CASE 
    WHEN d.persentase_diskon IS NOT NULL 
    THEN 'Harga dari Rp ' || m.harga || ' menjadi Rp ' || 
         (m.harga - (m.harga * d.persentase_diskon / 100)) || 
         ', ' || COALESCE(d.syarat_diskon, 'Promo Spesial')
    ELSE ''
END as info_diskon
```

**After:**
```sql
COALESCE(d.harga_baru, m.harga) as harga_diskon,
CASE 
    WHEN d.harga_baru IS NOT NULL 
    THEN 'Harga dari Rp ' || m.harga || ' menjadi Rp ' || d.harga_baru || 
         ', ' || COALESCE(d.deskripsi_diskon, 'Promo Spesial')
    ELSE ''
END as info_diskon
```

### 2. Key Changes
- Removed percentage-based discount calculation
- Now uses fixed new price (`harga_baru`) directly
- Updated column references to use new names
- Simplified logic since we now have direct price values

## Testing

### Run Test Script
```bash
test_customer_discount_fix.bat
```

### Manual Testing Steps
1. Start the application
2. Login as customer
3. Check if menu loads without errors
4. Verify discount column shows correct information
5. Test adding items with discounts to cart
6. Verify discounted prices are used in cart

### Expected Behavior
- No more "column does not exist" errors
- Discount column shows: "Harga dari Rp [original] menjadi Rp [new], [description]"
- Cart uses discounted prices when available
- No discount shows empty string in discount column

## Files Modified
- `src/main/java/com/example/bdsqltester/scenes/user/UserController.java`

## Files Created
- `test_customer_discount_fix.bat` - Test script
- `CUSTOMER_DISCOUNT_FIX.md` - This documentation

## Database Schema Reference
Current discount table structure:
```sql
CREATE TABLE diskon (
    id_diskon SERIAL PRIMARY KEY,
    id_menu INTEGER REFERENCES daftar_menu(id_menu),
    harga_baru INTEGER NOT NULL,
    deskripsi_diskon TEXT
);
``` 