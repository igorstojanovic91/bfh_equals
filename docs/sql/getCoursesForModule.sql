SELECT  DISTINCT c.id, c.name, c.shortName, c.moduleId, c.professorId, c.weight
FROM    Course c
        LEFT JOIN Module m ON c.moduleId = m.id
        LEFT JOIN Registration r on m.id = r.moduleId
WHERE   m.id = 33
AND     (c.professorId = 1 OR m.assistantId = 1 OR m.headId = 1 OR r.studentId = 1)
ORDER BY c.id asc;
