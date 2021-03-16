package ch.bfh.cassd2021.gruppe1.equals.business.service;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Module;
import ch.bfh.cassd2021.gruppe1.equals.business.model.Role;
import ch.bfh.cassd2021.gruppe1.equals.business.model.StudentCourseRating;
import ch.bfh.cassd2021.gruppe1.equals.repository.ModuleRepository;

import java.util.List;

public class ModuleService {
    final ModuleRepository moduleRepository;

    public ModuleService() {
        moduleRepository = new ModuleRepository();
    }

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

    public List<StudentCourseRating> getSuccessRateOverviewForModule(int moduleId, int personId) {
        List<StudentCourseRating> studentCourseRatingList = moduleRepository.getSuccessRateOverviewForModule(moduleId, personId);
        for(StudentCourseRating studentCourseRating : studentCourseRatingList){
            studentCourseRating.calculateGrades();
        }
        return studentCourseRatingList;
    }

}
