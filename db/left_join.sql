use entr_db;

-- List all events including ones with no attendees yet
SELECT events.event_name, COUNT(attendees.user_id) AS attendees
FROM events
LEFT JOIN attendees ON events.id = attendees.event_id
GROUP BY events.id;
