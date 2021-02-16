package ch.bfh.cassd2021.gruppe1.equals.business.model;

import java.time.LocalDate;

public class Module {
    private int moduleId;
    private String name;
    private String shortName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int headId;
    private int assistantId;

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

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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
            '}';
    }
}
