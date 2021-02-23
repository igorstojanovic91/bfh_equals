SELECT  DISTINCT m.id, m.name, m.shortName, m.startDate, m.endDate, m.headId, m.assistantId,
        CONCAT(h.lastName, ' ', h.firstName) AS head,
        CONCAT(a.lastName, ' ', a.firstName) AS assistant,
        CASE
            WHEN m.headId = 1 THEN 'Head'
            WHEN c.professorId = 1 THEN 'Professor'
            WHEN m.assistantId = 1 THEN 'Assistant'
            ELSE 'Student'
        END AS role
FROM    Module m
        LEFT JOIN Course c ON m.id = c.moduleId
        INNER JOIN Person h ON m.headId = h.id
        INNER JOIN Person a ON m.assistantId = a.id
        LEFT JOIN Registration r on m.id = r.moduleId
WHERE   m.headId = 1 OR c.professorId = 1 OR m.assistantId = 1 OR r.studentId = 1
ORDER BY m.shortName asc, m.startDate asc;
