package com.hawamoni.app.moni.controller;

import com.hawamoni.app.moni.dto.GroupDTO;
import com.hawamoni.app.moni.dto.WithdrawalRequestDTO;
import com.hawamoni.app.moni.request.ApprovalRequest;
import com.hawamoni.app.moni.request.DepositRequest;
import com.hawamoni.app.moni.request.ExecutorRequest;
import com.hawamoni.app.moni.request.WithdrawalRequest;
import com.hawamoni.app.moni.response.GroupResponse;
import com.hawamoni.app.moni.service.GroupService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/moni")
@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/groups")
    public ResponseEntity<GroupResponse> createGroup(@RequestBody GroupDTO groupDTO, HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer "))
            return new ResponseEntity<>(groupService.createGroup(groupDTO,header.substring(7)), HttpStatus.OK);
        else
            return null;
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<GroupDTO> getGroup(@PathVariable Long id) {
        return new ResponseEntity<>(groupService.getGroupByID(id),HttpStatus.OK);
    }

    @PostMapping("/groups/{id}/deposit")
    public ResponseEntity<Map<String,Object>> makeDeposit(@RequestBody DepositRequest depositRequest, @PathVariable("id") Long groupId) {
        return new ResponseEntity<>(groupService.makeDeposit(depositRequest,groupId),HttpStatus.OK);
    }

    @PostMapping("/groups/{id}/requests")
    public ResponseEntity<Map<String,Object>> createWithdrawalRequest(@RequestBody WithdrawalRequest withdrawalRequest,
                                                                      @PathVariable("id") Long groupId) {
        return new ResponseEntity<>(groupService.createWithdrawalRequest(withdrawalRequest,groupId),HttpStatus.OK);
    }

    @PostMapping("/requests/{id}/approve")
    public ResponseEntity<Map<String,Object>> approveWithdrawalRequest(@RequestBody ApprovalRequest approvalRequest, @PathVariable("id") Long requestId) {
        return new ResponseEntity<>(groupService.approveRequest(approvalRequest,requestId),HttpStatus.OK);
    }

    @PostMapping("/requests/{id}/execute")
    public ResponseEntity<Map<String,Object>> executeWithdrawalRequest(@RequestBody ExecutorRequest executorRequest, @PathVariable("id") Long requestId) {
        return new ResponseEntity<>(groupService.executeRequest(executorRequest,requestId),HttpStatus.OK);
    }

    @GetMapping("/requests/{id}")
    public ResponseEntity<WithdrawalRequestDTO> getWithdrawalRequest(@PathVariable("id") Long requestId) {
        return new ResponseEntity<>(groupService.getRequest(requestId),HttpStatus.OK);
    }
}
