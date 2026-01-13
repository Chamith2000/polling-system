//package com.oexil.univote.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "faculty_positions")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class FacultyPosition {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "faculty_id", nullable = false)
//    private Faculty faculty;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "position_id", nullable = false)
//    private Position position;
//}