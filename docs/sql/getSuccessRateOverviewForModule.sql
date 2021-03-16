SELECT  DISTINCT re.studentID, p.lastName, p.firstName,
        c.id as courseId, c.name, c.shortName, c.moduleId, c.professorId, c.weight,
        ra.successRate, ra.version
FROM    Registration re
        INNER JOIN Module m on re.moduleId = m.id
        INNER JOIN Course c on c.moduleId = m.id
        LEFT JOIN Rating ra on ra.courseId = c.id AND re.studentId = ra.studentId
        INNER JOIN Person p on p.id = re.studentId
WHERE   m.id = 36
AND     (m.headId = 1 OR m.assistantId = 1 OR c.professorId = 1 OR re.studentId = 1)
ORDER BY p.lastName asc, p.firstName asc, re.studentId asc, c.id asc
