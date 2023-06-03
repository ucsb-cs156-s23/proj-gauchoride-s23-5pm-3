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

@Api(description = "Ride request information (admin, driver, rider)")
@RequestMapping("/api/riderequests")
@RestController
public class RideRequestController extends ApiController {
    @Autowired
    RideRequestRepository rideRequestRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "Update a ride request (admin)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/put")
    public RideRequest updateRideRequest_Admin(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid RideRequest incoming) {

        RideRequest rideRequest = rideRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(RideRequest.class, id));

        rideRequest.setDay(incoming.getDay());
        rideRequest.setCourse(incoming.getCourse());
        rideRequest.setStartTime(incoming.getStartTime());
        rideRequest.setStopTime(incoming.getStopTime());
        rideRequest.setBuilding(incoming.getBuilding());
        rideRequest.setRoom(incoming.getRoom());
        rideRequest.setPickupLocation(incoming.getPickupLocation());

        rideRequestRepository.save(rideRequest);

        return rideRequest;
    }

	@ApiOperation(value = "Update a ride request (rider)")
    @PreAuthorize("hasRole('ROLE_RIDER')")
    @PutMapping("/put/rider")
    public RideRequest updateRideRequest_Rider(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid RideRequest incoming) {

		Long userId = getCurrentUser().getUser().getId();

        RideRequest rideRequest = rideRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(RideRequest.class, id));

		if(userId == rideRequest.getRiderId()){
			rideRequest.setDay(incoming.getDay());
			rideRequest.setCourse(incoming.getCourse());
			rideRequest.setStartTime(incoming.getStartTime());
			rideRequest.setStopTime(incoming.getStopTime());
			rideRequest.setBuilding(incoming.getBuilding());
			rideRequest.setRoom(incoming.getRoom());
			rideRequest.setPickupLocation(incoming.getPickupLocation());

			rideRequestRepository.save(rideRequest);

			return rideRequest;
		}else{
			throw new AccessDeniedException("403 returned"); 
		}
    }

}