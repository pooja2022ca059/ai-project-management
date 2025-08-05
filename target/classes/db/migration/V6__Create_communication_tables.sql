-- Message threads
CREATE TABLE message_threads (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('PROJECT', 'DIRECT', 'TEAM') NOT NULL DEFAULT 'PROJECT',
    title VARCHAR(255),
    project_id BIGINT NULL,
    is_pinned BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id),
    INDEX idx_project_id (project_id),
    INDEX idx_type (type)
);

-- Thread participants
CREATE TABLE thread_participants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    thread_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_read_at TIMESTAMP NULL,
    FOREIGN KEY (thread_id) REFERENCES message_threads(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY unique_thread_participant (thread_id, user_id),
    INDEX idx_thread_id (thread_id),
    INDEX idx_user_id (user_id)
);

-- Messages
CREATE TABLE messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    thread_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    mentions JSON,
    attachments JSON,
    reactions JSON,
    edited_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (thread_id) REFERENCES message_threads(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users(id),
    INDEX idx_thread_id (thread_id),
    INDEX idx_author_id (author_id),
    INDEX idx_created_at (created_at)
);

-- Notifications
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type ENUM('TASK_ASSIGNED', 'TASK_COMPLETED', 'PROJECT_UPDATE', 'MENTION', 'DEADLINE_REMINDER', 'AI_SUGGESTION') NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT,
    data JSON,
    priority ENUM('LOW', 'MEDIUM', 'HIGH') DEFAULT 'MEDIUM',
    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP NULL,
    actions JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at)
);