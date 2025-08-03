package com.aipm.ai_project_management.modules.tasks.dto;

import com.aipm.ai_project_management.common.enums.TaskStatus;
import java.util.List;

public class TaskBoardColumnDTO {
    
    private TaskStatus status;
    private String title;
    private String color;
    private Integer taskCount;
    private List<TaskDTO> tasks;
    private Integer order;

    // Default constructor
    public TaskBoardColumnDTO() {
    }

    // Constructor with basic fields
    public TaskBoardColumnDTO(TaskStatus status, String title, String color) {
        this.status = status;
        this.title = title;
        this.color = color;
        this.taskCount = 0;
    }

    // Getters and Setters
    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
        this.taskCount = tasks != null ? tasks.size() : 0;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "TaskBoardColumnDTO{" +
                "status=" + status +
                ", title='" + title + '\'' +
                ", color='" + color + '\'' +
                ", taskCount=" + taskCount +
                ", tasks=" + (tasks != null ? tasks.size() + " tasks" : "null") +
                ", order=" + order +
                '}';
    }
}
