-- Tasks table
CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    project_id BIGINT NOT NULL,
    assignee_id BIGINT,
    reporter_id BIGINT,
    status ENUM('BACKLOG', 'TODO', 'IN_PROGRESS', 'IN_REVIEW', 'TESTING', 'DONE', 'CANCELLED') NOT NULL DEFAULT 'BACKLOG',
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') NOT NULL DEFAULT 'MEDIUM',
    due_date DATE,
    estimated_hours DECIMAL(8,2),
    logged_hours DECIMAL(8,2) DEFAULT 0.00,
    progress DECIMAL(5,2) DEFAULT 0.00,
    labels JSON,
    custom_fields JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (assignee_id) REFERENCES users(id),
    FOREIGN KEY (reporter_id) REFERENCES users(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    INDEX idx_project_id (project_id),
    INDEX idx_assignee_id (assignee_id),
    INDEX idx_status (status),
    INDEX idx_priority (priority),
    INDEX idx_due_date (due_date)
);

-- Subtasks
CREATE TABLE subtasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_task_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    assignee_id BIGINT,
    status ENUM('TODO', 'IN_PROGRESS', 'COMPLETED') NOT NULL DEFAULT 'TODO',
    due_date DATE,
    estimated_hours DECIMAL(8,2),
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    FOREIGN KEY (parent_task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (assignee_id) REFERENCES users(id),
    INDEX idx_parent_task_id (parent_task_id),
    INDEX idx_assignee_id (assignee_id),
    INDEX idx_status (status)
);

-- Task dependencies
CREATE TABLE task_dependencies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dependent_task_id BIGINT NOT NULL,
    prerequisite_task_id BIGINT NOT NULL,
    dependency_type ENUM('FINISH_TO_START', 'START_TO_START', 'FINISH_TO_FINISH', 'START_TO_FINISH') DEFAULT 'FINISH_TO_START',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dependent_task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (prerequisite_task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    UNIQUE KEY unique_dependency (dependent_task_id, prerequisite_task_id),
    INDEX idx_dependent_task (dependent_task_id),
    INDEX idx_prerequisite_task (prerequisite_task_id)
);

-- Task comments
CREATE TABLE task_comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id BIGINT NOT NULL,
    parent_comment_id BIGINT NULL,
    author_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    mentions JSON,
    attachments JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_comment_id) REFERENCES task_comments(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users(id),
    INDEX idx_task_id (task_id),
    INDEX idx_author_id (author_id),
    INDEX idx_parent_comment (parent_comment_id)
);

-- Time tracking
CREATE TABLE time_tracking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    hours DECIMAL(8,2) NOT NULL,
    description TEXT,
    date DATE NOT NULL,
    billable BOOLEAN DEFAULT TRUE,
    hourly_rate DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_task_id (task_id),
    INDEX idx_user_id (user_id),
    INDEX idx_date (date)
);