use entr_db;

-- Total attendees for ALL events

SELECT SUM(total_attendees) AS total_attendance FROM 
(SELECT event_id, COUNT(user_id) AS total_attendees FROM attendees GROUP BY event_id) AS subquery;