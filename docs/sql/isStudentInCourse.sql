SELECT c.id FROM Course c
INNER JOIN Module m ON m.id = c.moduleId
INNER JOIN Registration r ON r.moduleId = m.id
WHERE r.studentId = 219 AND c.id = 417;

