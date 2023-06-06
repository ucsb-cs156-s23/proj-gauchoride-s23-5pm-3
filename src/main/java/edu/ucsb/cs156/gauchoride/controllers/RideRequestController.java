package edu.ucsb.cs156.gauchoride.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ucsb.cs156.gauchoride.entities.RideRequest;
import edu.ucsb.cs156.gauchoride.repositories.RideRequestRepository;

import edu.ucsb.cs156.gauchoride.errors.EntityNotFoundException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.validation.Valid;

@Api(description = "Ride request information")
@RequestMapping("/api/riderequests")
@RestController
public class RideRequestController extends ApiController {
    @Autowired
    RideRequestRepository rideRequestRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "Delete a ride request (admin)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public Object deleteRideRequest_Admin(
        @ApiParam("id") @RequestParam Long id) {
            RideRequest rideRequest = rideRequestRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(RideRequest.class, id));

        rideRequestRepository.delete(rideRequest);

        return genericMessage("Ride request with id %s deleted".formatted(id));
    }

    @ApiOperation(value = "Delete a ride request (rider)")
    @PreAuthorize("hasRole('ROLE_RIDER')")
    @DeleteMapping("/delete/rider")
    public Object deleteRideRequest_Rider(
        @ApiParam("id") @RequestParam Long id) {
            RideRequest rideRequest = rideRequestRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(RideRequest.class, id));

        Long userId = getCurrentUser().getUser().getId();
        if(userId == rideRequest.getRiderId()){
            rideRequestRepository.delete(rideRequest);
            return genericMessage("Ride request with id %s deleted".formatted(id));
        }else{
            throw new AccessDeniedException("403 returned"); 
        }
    }

}