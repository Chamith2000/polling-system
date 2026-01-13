//package com.oexil.univote.repository;
//
//import com.oexil.univote.model.FacultyPosition;
//import com.oexil.univote.model.Faculty;
//import com.oexil.univote.model.Position;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import java.util.List;
//
//@Repository
//public interface FacultyPositionRepository extends JpaRepository<FacultyPosition, Long> {
//    List<FacultyPosition> findByFaculty(Faculty faculty);
//    List<FacultyPosition> findByPosition(Position position);
//}