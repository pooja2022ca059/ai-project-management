-- AI insights and predictions
CREATE TABLE ai_insights (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_type ENUM('PROJECT', 'TASK', 'USER', 'TEAM') NOT NULL,
    entity_id BIGINT NOT NULL,
    insight_type ENUM('RISK_PREDICTION', 'DEADLINE_PREDICTION', 'RESOURCE_RECOMMENDATION', 'PERFORMANCE_ANALYSIS') NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    confidence_score DECIMAL(3,2),
    impact_level ENUM('LOW', 'MEDIUM', 'HIGH') DEFAULT 'MEDIUM',
    status ENUM('ACTIVE', 'RESOLVED', 'DISMISSED') DEFAULT 'ACTIVE',
    data JSON,
    suggested_actions JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL,
    INDEX idx_entity (entity_type, entity_id),
    INDEX idx_insight_type (insight_type),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
);

-- AI automation rules
CREATE TABLE ai_automation_rules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    trigger_type VARCHAR(100) NOT NULL,
    trigger_conditions JSON,
    actions JSON,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    execution_count INT DEFAULT 0,
    success_count INT DEFAULT 0,
    last_executed_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    FOREIGN KEY (created_by) REFERENCES users(id),
    INDEX idx_trigger_type (trigger_type),
    INDEX idx_status (status)
);

-- AI predictions history
CREATE TABLE ai_predictions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_type ENUM('PROJECT', 'TASK') NOT NULL,
    entity_id BIGINT NOT NULL,
    prediction_type ENUM('COMPLETION_DATE', 'BUDGET_OVERRUN', 'RISK_SCORE', 'QUALITY_SCORE') NOT NULL,
    predicted_value VARCHAR(255),
    actual_value VARCHAR(255),
    confidence_score DECIMAL(3,2),
    factors JSON,
    is_accurate BOOLEAN NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_entity (entity_type, entity_id),
    INDEX idx_prediction_type (prediction_type),
    INDEX idx_created_at (created_at)
);