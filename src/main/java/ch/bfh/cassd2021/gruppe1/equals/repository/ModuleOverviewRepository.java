package ch.bfh.cassd2021.gruppe1.equals.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.bfh.cassd2021.gruppe1.equals.business.model.Module;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleOverviewRepository {
    final Logger logger = LoggerFactory.getLogger(ModuleOverviewRepository.class);

    public List<Module> getModules(int personId) {
        List<Module> moduleList = new ArrayList<>();
        try(Connection connection = EqualsDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM MODULE WHERE headId = ?");
            preparedStatement.setInt(1, personId);



            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Module module = new Module();
                module.setModuleId(resultSet.getInt("id"));
                module.setName(resultSet.getString("name"));
                module.setShortName(resultSet.getString("shortName"));
                module.setStartDate(resultSet.getDate("startDate"));
                module.setEndDate(resultSet.getDate("endDate"));
                //module.setHead(null);
                //module.setAssistant(null);
                // TODO DO WE WANT TO CREATE A PERSON OBJECT HERE => 2nd DB-QUERY OR WORK WITH THE ID?
                int headId = resultSet.getInt("headId");
                int assistantId = resultSet.getInt("assistantId");

                moduleList.add(module);
            }
            return moduleList;
        } catch(SQLException e) {
            logger.error("Problem reading Database, message was {}", e.getMessage());
            throw new RepositoryException(e.getMessage());
        }

    }

}
