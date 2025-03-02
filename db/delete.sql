use entr_db;

-- Delete a user (this will also delete their events and attendance due to cascading)
DELETE FROM users WHERE username = 'john_doe';

-- Delete an event
DELETE FROM events WHERE id = 3;

-- Remove a user from an event (unjoin event)
DELETE FROM attendees WHERE user_id = 2 AND event_id = 3;