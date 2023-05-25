package edu.ucsb.cs156.gauchoride.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ucsb.cs156.gauchoride.entities.RideRequest;
import edu.ucsb.cs156.gauchoride.repositories.RideRequestRepository;

import edu.ucsb.cs156.gauchoride.errors.EntityNotFoundException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@Api(description = "Ride request information (admin, driver, rider)")
@RequestMapping("/api/riderequests")
@RestController
public class RideRequestController extends ApiController {
    @Autowired
    RideRequestRepository rideRequestRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "Get a list of all ride requests")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<String> rideRequests()
            throws JsonProcessingException {
        Iterable<RideRequest> rideRequests = rideRequestRepository.findAll();
        String body = mapper.writeValueAsString(rideRequests);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Get ride requests by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get")
    public RideRequest rideRequest(
            @ApiParam("id") @RequestParam Long id)
            throws JsonProcessingException {
        RideRequest rideRequest = rideRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(RideRequest.class, id));
        return rideRequest;
    }

    @ApiOperation(value = "Delete a ride request (admin, driver)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public Object deleteRideRequest_Admin(
            @ApiParam("id") @RequestParam Long id) {
              RideRequest rideRequest = rideRequestRepository.findById(id)
          .orElseThrow(() -> new EntityNotFoundException(RideRequest.class, id));

          rideRequestRepository.delete(rideRequest);

        return genericMessage("Ride request with id %s deleted".formatted(id));
    }

    
    @ApiOperation(value = "Toggle the admin field")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/toggleAdmin")
    public Object toggleAdmin( @ApiParam("id") @RequestParam Long id){
        RideRequest rideRequest = rideRequestRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(RideRequest.class, id));

        rideRequest.setAdmin(!user.getAdmin());
        rideRequestRepository.save(rideRequest);
        return genericMessage("Ride request with id %s has toggled admin status".formatted(id));
    }

}