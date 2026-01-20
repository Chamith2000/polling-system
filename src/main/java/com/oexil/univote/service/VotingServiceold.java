//package com.oexil.univote.service;
//
//import com.oexil.univote.model.Candidate;
//import com.oexil.univote.model.Position;
//import com.oexil.univote.model.Student;
//import java.util.List;
//import java.util.Map;
//
//public interface VotingServiceold {
//    boolean validatePin(String pin);
//    Student validateStudentForVoting(String studentId) throws Exception;
//    Map<Position, List<Candidate>> getBallotPaper(Student student);
//    void submitVote(String studentId, List<Long> candidateIds) throws Exception;
//}