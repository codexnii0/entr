use entr_db;

-- List all users and the events they joined

SELECT users.username, events.event_name
FROM attendees
RIGHT JOIN users ON attendees.user_id = users.id
LEFT JOIN events ON attendees.event_id = events.id;