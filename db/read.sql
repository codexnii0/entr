use entr_db;

-- Get all users
SELECT * FROM users;

-- Get all events
SELECT * FROM events;

-- Get all attendees for a specific event
SELECT users.username FROM attendees 
JOIN users ON attendees.user_id = users.id
WHERE attendees.event_id = 1;

-- Get all events a user has joined
SELECT events.event_name, events.event_date, events.location 
FROM attendees 
JOIN events ON attendees.event_id = events.id
WHERE attendees.user_id = 2;