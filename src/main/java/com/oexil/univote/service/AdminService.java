//package com.oexil.univote.service;
//
//import com.oexil.univote.model.*;
//import com.oexil.univote.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class AdminService {
//
//    private final PositionRepository positionRepository;
//    private final FacultyRepository facultyRepository;
//    private final FacultyPositionRepository facultyPositionRepository;
//    private final StudentRepository studentRepository;
//    private final CandidateRepository candidateRepository;
//    private final VotingPinRepository votingPinRepository;
//    private final VotedStudentRepository votedStudentRepository;
//    private final VoteRepository voteRepository;
//
//    // Position Management
//    public Position createPosition(String name, Boolean isCommon, Integer orderPriority, String description) {
//        Position position = new Position();
//        position.setName(name);
//        position.setIsCommon(isCommon);
//        position.setOrderPriority(orderPriority);
//        position.setDescription(description);
//        return positionRepository.save(position);
//    }
//
//    public List<Position> getAllPositions() {
//        return positionRepository.findAllByOrderByOrderPriorityAsc();
//    }
//
//    public Position updatePosition(Long id, String name, Boolean isCommon, Integer orderPriority, String description) {
//        Position position = positionRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Position not found"));
//        position.setName(name);
//        position.setIsCommon(isCommon);
//        position.setOrderPriority(orderPriority);
//        position.setDescription(description);
//        return positionRepository.save(position);
//    }
//
//    public void deletePosition(Long id) {
//        positionRepository.deleteById(id);
//    }
//
//    // Faculty Management
//    public Faculty createFaculty(String name, String code, String colorCode) {
//        Faculty faculty = new Faculty();
//        faculty.setName(name);
//        faculty.setCode(code);
//        faculty.setColorCode(colorCode);
//        return facultyRepository.save(faculty);
//    }
//
//    public List<Faculty> getAllFaculties() {
//        return facultyRepository.findAll();
//    }
//
//    public Faculty updateFaculty(Long id, String name, String code, String colorCode) {
//        Faculty faculty = facultyRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Faculty not found"));
//        faculty.setName(name);
//        faculty.setCode(code);
//        faculty.setColorCode(colorCode);
//        return facultyRepository.save(faculty);
//    }
//
//    public void deleteFaculty(Long id) {
//        facultyRepository.deleteById(id);
//    }
//
//    // Faculty Position Assignment
//    public FacultyPosition assignPositionToFaculty(Long facultyId, Long positionId) {
//        Faculty faculty = facultyRepository.findById(facultyId)
//                .orElseThrow(() -> new RuntimeException("Faculty not found"));
//        Position position = positionRepository.findById(positionId)
//                .orElseThrow(() -> new RuntimeException("Position not found"));
//
//        FacultyPosition facultyPosition = new FacultyPosition();
//        facultyPosition.setFaculty(faculty);
//        facultyPosition.setPosition(position);
//        return facultyPositionRepository.save(facultyPosition);
//    }
//
//    public void removeFacultyPosition(Long facultyPositionId) {
//        facultyPositionRepository.deleteById(facultyPositionId);
//    }
//
//    // Student Management
//    public Student createStudent(String studentId, String fullName, Long facultyId, String email, String phoneNumber) {
//        Faculty faculty = facultyRepository.findById(facultyId)
//                .orElseThrow(() -> new RuntimeException("Faculty not found"));
//
//        Student student = new Student();
//        student.setStudentId(studentId);
//        student.setFullName(fullName);
//        student.setFaculty(faculty);
//        student.setEmail(email);
//        student.setPhoneNumber(phoneNumber);
//        return studentRepository.save(student);
//    }
//
//    public List<Student> getAllStudents() {
//        return studentRepository.findAll();
//    }
//
//    public Student updateStudent(String studentId, String fullName, Long facultyId, String email, String phoneNumber) {
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//        Faculty faculty = facultyRepository.findById(facultyId)
//                .orElseThrow(() -> new RuntimeException("Faculty not found"));
//
//        student.setFullName(fullName);
//        student.setFaculty(faculty);
//        student.setEmail(email);
//        student.setPhoneNumber(phoneNumber);
//        return studentRepository.save(student);
//    }
//
//    public void deleteStudent(String studentId) {
//        studentRepository.deleteById(studentId);
//    }
//
//    // Candidate Management
//    public Candidate createCandidate(String studentId, Long positionId, Long facultyId, String manifesto, String photoUrl) {
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//        Position position = positionRepository.findById(positionId)
//                .orElseThrow(() -> new RuntimeException("Position not found"));
//
//        Faculty faculty = null;
//        if (facultyId != null) {
//            faculty = facultyRepository.findById(facultyId)
//                    .orElseThrow(() -> new RuntimeException("Faculty not found"));
//        }
//
//        Candidate candidate = new Candidate();
//        candidate.setStudent(student);
//        candidate.setPosition(position);
//        candidate.setFaculty(faculty);
//        candidate.setManifesto(manifesto);
//        candidate.setPhotoUrl(photoUrl);
//        return candidateRepository.save(candidate);
//    }
//
//    public List<Candidate> getAllCandidates() {
//        return candidateRepository.findAll();
//    }
//
//    public Candidate updateCandidate(Long id, String manifesto, String photoUrl) {
//        Candidate candidate = candidateRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Candidate not found"));
//        candidate.setManifesto(manifesto);
//        candidate.setPhotoUrl(photoUrl);
//        return candidateRepository.save(candidate);
//    }
//
//    public void deleteCandidate(Long id) {
//        candidateRepository.deleteById(id);
//    }
//
//    // Voting PIN Management
//    public VotingPin createVotingPin(String pinValue, LocalDateTime activeFrom, LocalDateTime activeUntil) {
//        VotingPin votingPin = new VotingPin();
//        votingPin.setPinValue(pinValue);
//        votingPin.setActiveFrom(activeFrom);
//        votingPin.setActiveUntil(activeUntil);
//        votingPin.setActive(true);
//        return votingPinRepository.save(votingPin);
//    }
//
//    public List<VotingPin> getAllVotingPins() {
//        return votingPinRepository.findAll();
//    }
//
//    public VotingPin deactivatePin(Long id) {
//        VotingPin pin = votingPinRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("PIN not found"));
//        pin.setActive(false);
//        return votingPinRepository.save(pin);
//    }
//
//    // Reset Operations
//    @Transactional
//    public void resetAllVotes() {
//        voteRepository.deleteAll();
//        votedStudentRepository.deleteAll();
//    }
//
//    @Transactional
//    public void resetStudentVote(String studentId) {
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        List<Vote> votes = voteRepository.findByStudent(student);
//        voteRepository.deleteAll(votes);
//        votedStudentRepository.deleteById(studentId);
//    }
//
//    // Statistics
//    public long getTotalVotesCast() {
//        return votedStudentRepository.count();
//    }
//
//    public long getTotalStudents() {
//        return studentRepository.count();
//    }
//
//    public long getTotalCandidates() {
//        return candidateRepository.count();
//    }
//
//    public long getVotesForCandidate(Long candidateId) {
//        Candidate candidate = candidateRepository.findById(candidateId)
//                .orElseThrow(() -> new RuntimeException("Candidate not found"));
//        return voteRepository.countByCandidate(candidate);
//    }
//}