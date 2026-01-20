package com.oexil.univote.controller;

import com.oexil.univote.dto.ValidationResponse;
import com.oexil.univote.dto.VoteSubmissionDTO;
import com.oexil.univote.dto.VotingSessionDTO;
import com.oexil.univote.service.VotingService;
import com.oexil.univote.service.VotingStationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/voting")
public class VotingController {

    private final VotingService votingService;
    private final VotingStationService stationService;

    // ==================== OPERATOR INTERFACE ====================

    @GetMapping("/")
    public String index() {
        return "voting/pin-entry";
    }

    /**
     * SECURE PIN VALIDATION
     * This checks the PIN on the server side and sets a session attribute.
     */
    @PostMapping("/validate-pin")
    @ResponseBody
    public ValidationResponse validatePin(@RequestParam String pin, HttpSession session) {
        // Assume votingService.validatePin(pin) returns true if PIN is correct
        boolean isValid = votingService.validatePin(pin);

        if (isValid) {
            // Mark this session as authorized
            session.setAttribute("isOperatorAuthorized", true);
            return new ValidationResponse(true, "PIN validated successfully", null);
        } else {
            return new ValidationResponse(false, "Invalid or expired PIN", null);
        }
    }

    /**
     * OPERATOR STATION PAGE
     * Protected: Checks session before showing the page.
     */
    @GetMapping("/operator/{stationId}")
    public String operatorStation(@PathVariable String stationId,
                                  HttpSession session,
                                  Model model) {

        // Security Check: Has the user entered the PIN securely?
        Boolean isAuthorized = (Boolean) session.getAttribute("isOperatorAuthorized");

        if (isAuthorized == null || !isAuthorized) {
            // If not authorized, redirect them back or show error
            return "redirect:/voting/";
        }

        model.addAttribute("stationId", stationId);
        model.addAttribute("stationName", getStationName(stationId));
        return "voting/operator-station";
    }

    @PostMapping("/validate-student")
    @ResponseBody
    public ValidationResponse validateStudent(@RequestParam String studentId,
                                              @RequestParam String stationId,
                                              HttpSession session) {

        // Security Check for API actions too
        Boolean isAuthorized = (Boolean) session.getAttribute("isOperatorAuthorized");
        if (isAuthorized == null || !isAuthorized) {
            return new ValidationResponse(false, "Unauthorized: Please log in again", null);
        }

        ValidationResponse response = votingService.validateStudent(studentId);

        if (response.isSuccess()) {
            VotingSessionDTO session1 = response.getVotingSession();

            // Verify student belongs to this station's faculty
            String facultyCode = getFacultyCode(session1.getFacultyId());
            if (!facultyCode.equals(stationId)) {
                return new ValidationResponse(false,
                        "Student does not belong to this faculty station", null);
            }

            // Activate ballot on the tablet for this station
            stationService.activateBallot(stationId, session1);

            return new ValidationResponse(true,
                    "Ballot activated on tablet for " + session1.getStudentName(),
                    session1);
        }

        return response;
    }

    // ==================== STUDENT TABLET INTERFACE ====================

    @GetMapping("/tablet/{stationId}")
    public String studentTablet(@PathVariable String stationId, Model model) {
        model.addAttribute("stationId", stationId);
        model.addAttribute("stationName", getStationName(stationId));
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

        ValidationResponse response = votingService.submitVotes(submission, ipAddress);

        if (response.isSuccess()) {
            // Reset the ballot on this station's tablet
            stationService.resetBallot(stationId);
        }

        return response;
    }

    @PostMapping("/reset-ballot/{stationId}")
    @ResponseBody
    public ValidationResponse resetBallot(@PathVariable String stationId) {
        stationService.resetBallot(stationId);
        return new ValidationResponse(true, "Ballot reset successfully", null);
    }

    // ==================== HELPER METHODS ====================

    private String getStationName(String stationId) {
        return switch (stationId.toUpperCase()) {
            case "ENG" -> "Engineering Faculty";
            case "IT" -> "Information Technology Faculty";
            case "BM" -> "Business Management Faculty";
            case "AS" -> "Applied Sciences Faculty";
            default -> "Unknown Faculty";
        };
    }

    private String getFacultyCode(Long facultyId) {
        return switch (facultyId.intValue()) {
            case 1 -> "ENG";
            case 2 -> "IT";
            case 3 -> "BM";
            case 4 -> "AS";
            default -> "UNKNOWN";
        };
    }
}