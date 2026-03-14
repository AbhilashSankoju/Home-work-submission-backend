package com.portal.homework.repository;

import com.portal.homework.entity.Assignment;
import com.portal.homework.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByTeacher(User teacher);

    List<Assignment> findByTeacherId(Long teacherId);
}
