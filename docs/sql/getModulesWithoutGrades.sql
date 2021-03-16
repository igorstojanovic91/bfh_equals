SELECT DISTINCT m.id, m.name, m.shortName
FROM Module m
LEFT JOIN Course c ON m.id = c.moduleId
LEFT JOIN Registration r on m.id = r.moduleId
LEFT JOIN Rating ra on r.studentId = ra.studentId AND c.id = ra.courseId
WHERE m.assistantId = 101 AND (ra.successRate is NULL OR ra.successRate = 0);