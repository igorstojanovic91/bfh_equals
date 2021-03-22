package ch.bfh.cassd2021.gruppe1.equals.business.service;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Module;
import ch.bfh.cassd2021.gruppe1.equals.business.model.Role;
import ch.bfh.cassd2021.gruppe1.equals.business.model.StudentCourseRating;
import ch.bfh.cassd2021.gruppe1.equals.repository.ModuleRepository;

import java.util.List;

/**
 * The ModuleService class implements the service layer for modules.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
public class ModuleService {
    final ModuleRepository moduleRepository;

    public ModuleService() {
        moduleRepository = new ModuleRepository();
    }

    /**
     * Returns a list of modules.
     * If person is not of role Student, a check is made for each module to see if any grades are outstanding.
     *
     * @param personId the personId
     * @return a list of modules
     */
    public List<Module> getModulesForPerson(int personId) {
        List<Module> moduleList = moduleRepository.getModulesForPerson(personId);

        boolean isNotStudent = moduleList.stream()
            .noneMatch(m -> Role.STUDENT.equals(m.getRole()));
        if (isNotStudent) {
            List<Integer> modulesWithoutGradesList = moduleRepository.getModulesWithoutGrades(personId);
            for (Module module : moduleList) {
                module.setHasOpenGrades(modulesWithoutGradesList.contains(module.getModuleId()));
            }
        }
        return moduleList;
    }

    /**
     * Returns a list of StudentCourseRatings.
     * After getting the list, all preliminary and overall grades are calculated and added to the corresponding
     * StudentCourseRating object.
     *
     * @param moduleId the moduleId
     * @param personId the personId
     * @return a list of StudentCourseRatings
     */
    public List<StudentCourseRating> getSuccessRateOverviewForModule(int moduleId, int personId) {
        List<StudentCourseRating> studentCourseRatingList = moduleRepository.getSuccessRateOverviewForModule(moduleId, personId);
        for (StudentCourseRating studentCourseRating : studentCourseRatingList) {
            studentCourseRating.calculateGrades();
        }
        return studentCourseRatingList;
    }

}
