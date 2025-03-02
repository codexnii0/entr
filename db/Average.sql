use entr_db;

-- Average attendees per event

SELECT AVG(total_attendees) AS avg_attendance FROM 
(SELECT event_id, COUNT(user_id) AS total_attendees FROM event_attendees GROUP BY event_id) AS subquery;