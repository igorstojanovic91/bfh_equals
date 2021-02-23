SELECT  r.studentId, r.courseId,
        p.lastName, p.firstName,
        r.successRate, r.version
FROM    Rating r
        LEFT JOIN Person p ON r.studentId = p.id
WHERE   r.courseId = 413 OR r.courseID = 414
ORDER BY p.lastName asc, p.firstName asc;