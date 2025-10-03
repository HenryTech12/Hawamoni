package com.hawamoni.app.moni.controller;

import com.hawamoni.app.moni.dto.GroupDTO;
import com.hawamoni.app.moni.dto.MemberDTO;
import com.hawamoni.app.moni.request.GroupRequest;
import com.hawamoni.app.moni.request.MemberRequest;
import com.hawamoni.app.moni.request.UpdateMemberRequest;
import com.hawamoni.app.moni.response.GroupResponse;
import com.hawamoni.app.moni.service.GroupService;
import com.hawamoni.app.moni.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/moni/groups")
@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private MemberService memberService;

    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@RequestBody GroupRequest groupRequest, HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer "))
            return new ResponseEntity<>(groupService.createGroup(groupRequest,header.substring(7)), HttpStatus.OK);
        else
            return null;
    }

    @GetMapping
    public ResponseEntity<List<GroupDTO>> getGroups(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(header != null)
            return new ResponseEntity<>(groupService.getGroups(header.substring(7)),HttpStatus.OK);
        else
            return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> getGroup(@PathVariable Long id) {
        return new ResponseEntity<>(groupService.getGroupByID(id),HttpStatus.OK);
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<MemberDTO>> getMembers(@PathVariable("id") Long groupId) {
        return new ResponseEntity<>(memberService.getMembers(groupId),HttpStatus.OK);
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Object> createMember(@PathVariable("id") Long groupId , @RequestBody MemberRequest memberRequest, HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if(header != null)
            return new ResponseEntity<>(memberService.
                    createMember(groupId,memberRequest,header.substring(7)),HttpStatus.OK);
        else
            return null;
    }

    @PutMapping("/{id}/members/{memberId}")
    public ResponseEntity<Map<String,Object>> updateMember(@PathVariable("id") Long groupId , @PathVariable Long memberId, @RequestBody UpdateMemberRequest memberRequest) {
        return new ResponseEntity<>(memberService.updateMemberDetails(groupId,memberId,memberRequest),HttpStatus.OK);
    }

    @DeleteMapping("/{id}/members/{memberId}")
    public ResponseEntity<Map<String,Object>> deleteMember(@PathVariable("id") Long groupId , @PathVariable Long memberId) {
        return new ResponseEntity<>(memberService.deleteMemberDetails(groupId,memberId),HttpStatus.OK);
    }
}
