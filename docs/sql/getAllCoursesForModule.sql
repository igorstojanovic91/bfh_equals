SELECT  DISTINCT c.id as courseId, c.name, c.shortName, c.moduleId,
                 CONCAT(p.lastName, ' ', p.firstName) AS professor,
                 c.weight,
                 CASE
                     WHEN m.headId = 1 THEN 'Head'
                     WHEN c.professorId = 1 THEN 'Professor'
                     WHEN m.assistantId = 1 THEN 'Assistant'
                     WHEN r.studentId = 1 THEN 'Student'
                     ELSE 'None'
                     END AS role
FROM    Module m
            LEFT JOIN Course c ON m.id = c.moduleId
            INNER JOIN Person p ON c.professorId = p.id
            LEFT JOIN Registration r on m.id = r.moduleId
WHERE   m.id = 38
ORDER BY c.id asc;
