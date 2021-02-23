SELECT  DISTINCT m.id as moduleId, m.name, m.shortName, m.startDate, m.endDate,
                 CONCAT(h.lastName, ' ', h.firstName) AS head,
                 CONCAT(a.lastName, ' ', a.firstName) AS assistant,
                 CASE
                    WHEN m.headId = 268 THEN 'Head'
                    WHEN c.professorId = 268 THEN 'Professor'
                    WHEN m.assistantId = 268 THEN 'Assistant'
                    ELSE 'Student'
                 END AS role
FROM    Module m
        LEFT JOIN Course c ON m.id = c.moduleId
        INNER JOIN Person h ON m.headId = h.id
        INNER JOIN Person a ON m.assistantId = a.id
        LEFT JOIN Registration r on m.id = r.moduleId
WHERE   m.headId = 268 OR c.professorId = 268 OR m.assistantId = 268 OR r.studentId = 268
ORDER BY m.shortName asc, m.startDate asc;