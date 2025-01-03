package com.vit.carpool.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vit.carpool.entities.Request;
import com.vit.carpool.entities.RequestByCreator;
import com.vit.carpool.entities.RequestByUser;
import com.vit.carpool.enums.RequestStatus;
import com.vit.carpool.services.RequestService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    // Route to retrieve all requests
    @GetMapping
    public ResponseEntity<?> getAllRequests() {

        try {
            List<Request> requests = requestService.getAllRequests();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving requests: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Route to find a request by id
    @GetMapping("/{requestId}")
    public ResponseEntity<?> getRequestById(@PathVariable long requestId) {
        if (requestId < 0) {
            return new ResponseEntity<>("Invalid request ID", HttpStatus.BAD_REQUEST);
        }
        try {
            Request request = requestService.getRequestById(requestId);
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving request: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Route to create a new request
    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody Request request) {
        try {
            int result = requestService.createRequest(request);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating request: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Route to update a request status
    @PutMapping("/{requestId}/status")
    public ResponseEntity<?> updateRequestStatus(@PathVariable long requestId,
            @RequestBody RequestStatus status) {
        if (requestId < 0 || status == null) {
            return new ResponseEntity<>("Invalid request body", HttpStatus.BAD_REQUEST);
        } else {
            System.out.println(requestId + ", " + status);
        }
        try {
            int result = requestService.updateRequestStatus(requestId, status);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating request status: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Route to delete a request
    @DeleteMapping("/{requestId}")
    public ResponseEntity<?> deleteRequest(@PathVariable long requestId) {
        try {
            int result = requestService.deleteRequest(requestId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting request: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Route to fetch all requests for pools created by a specific user
    @GetMapping("/by-creator/{creatorId}")
    public ResponseEntity<?> getRequestsByCreator(@PathVariable String creatorId) {
        try {
            List<RequestByCreator> requests = requestService.getRequestsByCreator(creatorId.toLowerCase());
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving requests by creator: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<?> getMethodName(@PathVariable String userId) {
        try {
            List<RequestByUser> response = requestService.getRequestsByUser(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving requests by user: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}