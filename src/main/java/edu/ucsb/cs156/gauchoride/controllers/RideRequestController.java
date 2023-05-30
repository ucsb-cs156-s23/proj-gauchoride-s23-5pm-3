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

@Api(description = "Ride request information (admin, driver, rider)")
@RequestMapping("/api/riderequests")
@RestController
public class RideRequestController extends ApiController {
    @Autowired
    RideRequestRepository rideRequestRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "Get a list of all ride requests")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_DRIVER')")
    @GetMapping("/all")
    public ResponseEntity<String> rideRequestsAdmin()
            throws JsonProcessingException {
        Iterable<RideRequest> rideRequests = rideRequestRepository.findAll();
        String body = mapper.writeValueAsString(rideRequests);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Get a list of all ride requests for current rider")
    @PreAuthorize("hasRole('ROLE_RIDER')")
    @GetMapping("/all_rider")
    public ResponseEntity<String> rideRequestsRider(
        @ApiParam(name="userid", type="Long", value="userid of the rider", example="123", required=true) @RequestParam Long userid
        )
        throws JsonProcessingException {

        Iterable<RideRequest> rideRequests = rideRequestRepository.findAllByUserid(userid);
        String body = mapper.writeValueAsString(rideRequests);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Create a new ride request")
    @PreAuthorize("hasRole('ROLE_RIDER')")
    @PostMapping("/post")
    public RideRequest postRideRequest(
        @ApiParam(name="userid", type="Long", value="userid of the rider", example="123", required=true)  @RequestParam Long userid,
        @ApiParam(name="day", type="String", value="day, one of [Monday, Tuesday,..., Sunday]", example="Monday", required=true) @RequestParam String day,
        @ApiParam(name="fullName", type="String", value="name of the rider", example="Joe", required=true) @RequestParam String fullName,
        @ApiParam(name="course", type="String", value="course to attend", example="CS 156", required=true) @RequestParam String course,
        @ApiParam(name="startTime", type="String", value="start time, format: HH:MM XM", example="12:00 AM", required=true) @RequestParam String startTime,
        @ApiParam(name="stopTime", type="String", value="stop time, format: HH:MM XM", example="12:00 AM", required=true) @RequestParam String stopTime,
        @ApiParam(name="building", type="String", value="destination building", example="Phelps", required=true) @RequestParam String building,
        @ApiParam(name="room", type="String", value="destination room", example="EUCR, 3525", required=true) @RequestParam String room,
        @ApiParam(name="pickup", type="String", value="pickup location", example="San Rafael Hall", required=true) @RequestParam String pickup
        )
        throws JsonProcessingException {

        RideRequest rideRequest = new RideRequest();
        rideRequest.setUserid(userid);
        rideRequest.setDay(day);
        rideRequest.setFullName(fullName);
        rideRequest.setCourse(course);
        rideRequest.setStartTime(startTime);
        rideRequest.setStopTime(stopTime);
        rideRequest.setBuilding(building);
        rideRequest.setRoom(room);
        rideRequest.setPickup(pickup);

        RideRequest saved = rideRequestRepository.save(rideRequest);

        return saved;
    }

    @ApiOperation(value = "Delete a ride request")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_RIDER')")
    @DeleteMapping("/delete")
    public Object deleteRideRequest_Admin(
            @ApiParam("id") @RequestParam Long id) {
              RideRequest rideRequest = rideRequestRepository.findById(id)
          .orElseThrow(() -> new EntityNotFoundException(RideRequest.class, id));

          rideRequestRepository.delete(rideRequest);

        return genericMessage("Ride request with id %s deleted".formatted(id));
    }

    @ApiOperation(value = "Update a ride request")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_RIDER')")
    @PutMapping("/put")
    public RideRequest updateRideRequest(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid RideRequest incoming) {

        RideRequest rideRequest = rideRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(RideRequest.class, id));

        rideRequest.setDay(incoming.getDay());
        rideRequest.setFullName(incoming.getFullName());
        rideRequest.setCourse(incoming.getCourse());
        rideRequest.setStartTime(incoming.getStartTime());
        rideRequest.setStopTime(incoming.getStopTime());
        rideRequest.setBuilding(incoming.getBuilding());
        rideRequest.setRoom(incoming.getRoom());
        rideRequest.setPickup(incoming.getPickup());

        rideRequestRepository.save(rideRequest);

        return rideRequest;
    }
}