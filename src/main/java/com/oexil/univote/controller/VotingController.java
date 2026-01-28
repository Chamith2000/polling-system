package com.oexil.univote.controller;

import com.oexil.univote.dto.ValidationResponse;
import com.oexil.univote.dto.VoteSubmissionDTO;
import com.oexil.univote.dto.VotingSessionDTO;
import com.oexil.univote.service.FacultyLookupService;
import com.oexil.univote.service.VotingService;
import com.oexil.univote.service.VotingStationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/voting")
public class VotingController {

    private final VotingService votingService;
    private final VotingStationService stationService;
    private final FacultyLookupService facultyLookupService;

    // ==================== OPERATOR INTERFACE ====================

    @GetMapping("/")
    public String index() {
        return "voting/pin-entry";
    }

    @PostMapping("/validate-pin")
    @ResponseBody
    public ValidationResponse validatePin(@RequestParam String pin, HttpSession session) {
        boolean isValid = votingService.validatePin(pin);

        if (isValid) {
            session.setAttribute("pinValidated", true);
            log.info("PIN validated successfully");
            return new ValidationResponse(true, "PIN validated successfully", null);
        } else {
            log.warn("Invalid PIN attempt: {}", pin);
            return new ValidationResponse(false, "Invalid or expired PIN", null);
        }
    }

    @GetMapping("/station-selection")
    public String stationSelection(HttpSession session, Model model) {
        Boolean pinValidated = (Boolean) session.getAttribute("pinValidated");

        if (pinValidated == null || !pinValidated) {
            return "redirect:/voting/";
        }

        model.addAttribute("faculties", facultyLookupService.getAllFaculties());
        return "voting/station-selection";
    }

    @GetMapping("/operator/{stationId}")
    public String operatorStation(@PathVariable String stationId,
                                  HttpSession session,
                                  Model model) {
        Boolean pinValidated = (Boolean) session.getAttribute("pinValidated");

        if (pinValidated == null || !pinValidated) {
            return "redirect:/voting/";
        }

        model.addAttribute("stationId", stationId);
        model.addAttribute("stationName", facultyLookupService.getStationName(stationId));

        model.addAttribute("facultyColor", facultyLookupService.getStationColor(stationId));

        log.info("Opening operator station: {} - {}", stationId,
                facultyLookupService.getStationName(stationId));

        return "voting/operator-station";
    }

    @PostMapping("/validate-student")
    @ResponseBody
    public ValidationResponse validateStudent(@RequestParam String studentId,
                                              @RequestParam String stationId,
                                              HttpSession session) {
        Boolean pinValidated = (Boolean) session.getAttribute("pinValidated");

        if (pinValidated == null || !pinValidated) {
            return new ValidationResponse(false, "PIN not validated", null);
        }

        ValidationResponse response = votingService.validateStudent(studentId);

        if (response.isSuccess()) {
            VotingSessionDTO votingSession = response.getVotingSession();

            // Verify student belongs to this station's faculty
            String facultyCode = facultyLookupService.getFacultyCode(votingSession.getFacultyId());
            if (!facultyCode.equals(stationId)) {
                log.warn("Student {} does not belong to station {}", studentId, stationId);
                return new ValidationResponse(false,
                        "Student does not belong to this faculty station", null);
            }

            // Activate ballot on the tablet for this station
            stationService.activateBallot(stationId, votingSession);

            log.info("Ballot activated for student {} at station {}", studentId, stationId);

            return new ValidationResponse(true,
                    "Ballot activated on tablet for " + votingSession.getStudentName(),
                    votingSession);
        }

        return response;
    }

    @GetMapping("/operator/search")
    @ResponseBody
    public List<Map<String, Object>> searchStudents(@RequestParam String stationId, @RequestParam String query, HttpSession session) {
        Boolean pinValidated = (Boolean) session.getAttribute("pinValidated");
        if (pinValidated == null || !pinValidated) return List.of();

        return votingService.searchStudentsForOperator(stationId, query);
    }

    @GetMapping("/operator/recent")
    @ResponseBody
    public List<Map<String, Object>> getRecentVotes(@RequestParam String stationId, HttpSession session) {
        Boolean pinValidated = (Boolean) session.getAttribute("pinValidated");
        if (pinValidated == null || !pinValidated) return List.of();

        return votingService.getRecentVoters(stationId);
    }

    @GetMapping("/operator/vote-count")
    @ResponseBody
    public Map<String, Object> getVoteCount(@RequestParam String stationId, HttpSession session) {
        Boolean pinValidated = (Boolean) session.getAttribute("pinValidated");
        if (pinValidated == null || !pinValidated) {
            return Map.of("count", 0, "error", "Not authenticated");
        }

        long count = votingService.getVoteCountByStation(stationId);
        return Map.of("count", count);
    }

    // ==================== STUDENT TABLET INTERFACE ====================

    @GetMapping("/tablet/{stationId}")
    public String studentTablet(@PathVariable String stationId, Model model) {
        model.addAttribute("stationId", stationId);
        model.addAttribute("stationName", facultyLookupService.getStationName(stationId));

        log.info("Student tablet opened for station: {}", stationId);

        return "voting/student-tablet";
    }

    @GetMapping("/get-session/{stationId}")
    @ResponseBody
    public ValidationResponse getActiveSession(@PathVariable String stationId) {
        VotingSessionDTO session = stationService.getActiveSession(stationId);

        if (session != null) {
            return new ValidationResponse(true, "Session found", session);
        } else {
            return new ValidationResponse(false, "No active session", null);
        }
    }

    @PostMapping("/submit-votes")
    @ResponseBody
    public ValidationResponse submitVotes(@RequestBody VoteSubmissionDTO submission,
                                          @RequestParam String stationId,
                                          HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();

        log.info("Submitting votes for student {} at station {}",
                submission.getStudentId(), stationId);

        ValidationResponse response = votingService.submitVotes(submission, ipAddress);

        if (response.isSuccess()) {
            // Reset the ballot on this station's tablet
            stationService.resetBallot(stationId);
            log.info("Vote submitted successfully and ballot reset for station {}", stationId);
        }

        return response;
    }

    @PostMapping("/reset-ballot/{stationId}")
    @ResponseBody
    public ValidationResponse resetBallot(@PathVariable String stationId) {
        stationService.resetBallot(stationId);
        log.info("Ballot manually reset for station {}", stationId);
        return new ValidationResponse(true, "Ballot reset successfully", null);
    }
}