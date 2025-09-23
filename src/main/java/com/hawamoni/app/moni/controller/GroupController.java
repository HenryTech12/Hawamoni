package com.hawamoni.app.moni.controller;

import com.hawamoni.app.moni.dto.GroupDTO;
import com.hawamoni.app.moni.response.GroupResponse;
import com.hawamoni.app.moni.service.GroupService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
