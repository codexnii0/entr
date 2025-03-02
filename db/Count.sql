use entr_db;

-- Count total users
SELECT COUNT(*) AS total_users FROM users;

-- Count total events
SELECT COUNT(*) AS total_events FROM events;

-- Count total attendees per event
SELECT event_id, COUNT(user_id) AS total_attendees FROM attendees GROUP BY event_id;