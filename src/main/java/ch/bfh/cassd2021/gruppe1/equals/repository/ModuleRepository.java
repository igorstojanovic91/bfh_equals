package ch.bfh.cassd2021.gruppe1.equals.repository;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModuleRepository {

    final Logger logger = LoggerFactory.getLogger(ModuleRepository.class);

    public List<Module> getModulesForPerson(int personId) {
        logger.debug("Entering getModulesForPerson()...");
        List<Module> moduleList = new ArrayList<>();

        String query = "SELECT  DISTINCT m.id, m.name, m.shortName, m.startDate, m.endDate, m.headId, m.assistantId,"
        + " CASE"
        + " WHEN m.headId = ? THEN 'Head'"
            + " WHEN c.professorId = ? THEN 'Professor'"
            + " WHEN m.assistantId = ? THEN 'Assistant'"
            + " ELSE 'Student'"
            + " END AS role"
        + " FROM Module m"
        + " LEFT JOIN Course c ON m.id = c.moduleId"
        + " INNER JOIN Person h ON m.headId = h.id"
        + " INNER JOIN Person a ON m.assistantId = a.id"
        + " LEFT JOIN Registration r on m.id = r.moduleId"
        + " WHERE m.headId = ? OR c.professorId = ? OR m.assistantId = ? OR r.studentId = ?"
        + " ORDER BY m.shortName asc, m.startDate asc";

        try (Connection connection = EqualsDataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, personId);
            statement.setInt(2, personId);
            statement.setInt(3, personId);
            statement.setInt(4, personId);
            statement.setInt(5, personId);
            statement.setInt(6, personId);
            statement.setInt(7, personId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Module module = new Module();
                module.setModuleId(resultSet.getInt("id"));
                module.setName(resultSet.getString("name"));
                module.setShortName(resultSet.getString("shortName"));
                module.setStartDate(resultSet.getDate("startDate"));
                module.setEndDate(resultSet.getDate("endDate"));
                module.setHeadId(resultSet.getInt("headId"));
                module.setAssistantId(resultSet.getInt("assistantId"));
                module.setRole(resultSet.getString("role"));
                moduleList.add(module);
            }

        } catch (SQLException throwables) {
            logger.error("Problem reading Database, mesage was {}", throwables.getMessage());
            throw new RepositoryException(throwables.getMessage());
        }

        return moduleList;
    }

}
