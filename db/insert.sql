use entr_db;

-- Insert a new user
INSERT INTO users (username, password) VALUES ('john_doe', 'johndoe123');

-- Insert a new event (replace `user_id` with an actual ID)
INSERT INTO events (user_id, event_name, event_date, location, time_start, time_end, description) 
VALUES (1, 'Blockchain Summit', '2025-03-10', 'Manila', '10:00:00', '16:00:00', 'A conference on blockchain technology.');

-- Insert an attendee for an event (replace `user_id` and `event_id` with actual IDs)
INSERT INTO attendees (user_id, event_id) VALUES (2, 3);
