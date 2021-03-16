SELECT  DISTINCT pers.id, pers.lastName, pers.firstName, pers.username,
        head.headId, crs.professorId, ass.assistantId, reg.studentId
FROM    Person pers
        LEFT JOIN Module head ON pers.id = head.headId
        LEFT JOIN Course crs ON pers.id = crs.professorId
        LEFT JOIN Module ass ON pers.id = ass.assistantId
        LEFT JOIN Registration reg ON pers.id = reg.studentId
WHERE   pers.id = 1