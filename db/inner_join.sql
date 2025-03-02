use entr_db;

-- List all events with their creators

SELECT events.id, events.event_name, events.event_date, users.username AS creator
FROM events 
INNER JOIN users ON events.user_id = users.id;