package ch.bfh.cassd2021.gruppe1.equals.business.model;

import java.time.LocalDate;
import java.sql.Date;

/**
 * The Module class implements a module
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
public class Module {
    private int moduleId;
    private String name;
    private String shortName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int headId;
    private int assistantId;
    private Role role;
    private boolean hasOpenGrades;

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate.toLocalDate();
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate.toLocalDate();
    }

    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        this.headId = headId;
    }

    public int getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(int assistantId) {
        this.assistantId = assistantId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = Role.valueOf(role.toUpperCase());
    }

    public boolean isHasOpenGrades() {
        return hasOpenGrades;
    }

    public void setHasOpenGrades(boolean hasOpenGrades) {
        this.hasOpenGrades = hasOpenGrades;
    }

    @Override
    public String toString() {
        return "Module{" +
            "moduleId=" + moduleId +
            ", name='" + name + '\'' +
            ", shortName='" + shortName + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", headId=" + headId +
            ", assistantId=" + assistantId +
            ", role=" + role +
            ", hasOpenGrades=" + hasOpenGrades +
            '}';
    }
}
