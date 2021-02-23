SELECT  DISTINCT c.id, c.name, c.shortName, c.moduleId, c.professorId, c.weight
FROM    Course c
        LEFT JOIN Module m ON c.moduleId = m.id
        LEFT JOIN Registration r on m.id = r.moduleId
WHERE   m.id = 36
AND     (c.professorId = 268 OR m.assistantId = 268 OR m.headId = 268 OR r.studentId = 268)
ORDER BY c.id asc;
