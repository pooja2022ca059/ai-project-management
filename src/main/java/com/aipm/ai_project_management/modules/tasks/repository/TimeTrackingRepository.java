package com.aipm.ai_project_management.modules.tasks.repository;

import com.aipm.ai_project_management.modules.tasks.entity.TimeTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeTrackingRepository extends JpaRepository<TimeTracking, Long> {
    
    List<TimeTracking> findByTaskIdOrderByDateDescCreatedAtDesc(Long taskId);
    
    List<TimeTracking> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT SUM(t.hours) FROM TimeTracking t WHERE t.task.id = :taskId")
    Double getTotalHoursByTaskId(@Param("taskId") Long taskId);
    
    @Query("SELECT SUM(t.hours) FROM TimeTracking t WHERE t.task.id = :taskId AND t.billable = true")
    Double getTotalBillableHoursByTaskId(@Param("taskId") Long taskId);
}