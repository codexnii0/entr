use entr_db;

-- Update a user’s password
UPDATE users SET password = '12345' WHERE username = 'john_doe';

-- Update event details
UPDATE events 
SET event_name = 'Updated Blockchain Summit', 
    event_date = '2025-03-12',
    location = 'Cebu',
    time_start = '09:00:00',
    time_end = '15:00:00',
    description = 'Updated details about blockchain conference.'
WHERE id = 2;
