SELECT  re.studentID, p.lastName, p.firstName,
        c.id as courseId, c.name, c.shortName, c.moduleId, c.professorId, c.weight,
        ra.successRate, ra.version
FROM    Registration re
        INNER JOIN Person p ON re.studentId = p.id
        INNER JOIN Course c ON re.moduleId = c.moduleId
        LEFT JOIN Rating ra ON re.studentId = ra.studentId
        LEFT JOIN Module m ON c.moduleId = m.id
WHERE   re.moduleId = 36
AND     (m.headId = 1 OR m.assistantId = 1 OR c.professorId = 1 OR re.studentId = 1)
ORDER BY p.lastName asc, p.firstName asc, re.studentId asc, c.id asc
