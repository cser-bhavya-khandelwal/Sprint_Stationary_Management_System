package com.stationery.request.repository;

import com.stationery.request.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Request persistence.
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    /**
     * Retrieves all stationery requests submitted by a specific student.
     */
    List<Request> findByStudentEmail(String studentEmail);
}
