package com.aipm.ai_project_management.modules.tasks.dto;



import java.util.ArrayList;
import java.util.List;


public class TaskBoardDTO {
    private Long projectId;
    public Long getProjectId() {
		return projectId;
	}


	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}


	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public List<TaskColumnDTO> getColumns() {
		return columns;
	}


	public void setColumns(List<TaskColumnDTO> columns) {
		this.columns = columns;
	}


	private String projectName;
    private List<TaskColumnDTO> columns = new ArrayList<>();
    
    private String view;
    //private List<TaskBoardColumnDTO> columns;

    // Getter-Setters
    public String getView() {
        return view;
    }

//    public void setView(String view) {
//        this.view = view;
//    }
    
   
    public static class TaskColumnDTO {
        public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
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
		}
		private String id;
        private String name;
        private Integer taskCount;
        private List<TaskDTO> tasks = new ArrayList<>();
    }


	




	public void setView(String view2) {
		// TODO Auto-generated method stub
		
	}
}
