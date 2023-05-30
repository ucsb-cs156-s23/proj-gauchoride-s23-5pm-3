package edu.ucsb.cs156.gauchoride.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ucsb.cs156.gauchoride.entities.DriverShift;
import edu.ucsb.cs156.gauchoride.repositories.DriverShiftRepository;

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

@Api(description = "Driver shift information")
@RequestMapping("/api/drivershifts")
@RestController
public class RideRequestController extends ApiController {
    @Autowired
    DriverShiftRepository driverShiftRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "Get a list of all driver shifts")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_DRIVER') || hasRole('ROLE_RIDER')")
    @GetMapping("/all")
    public ResponseEntity<String> getDriverShifts()
            throws JsonProcessingException {
        Iterable<DriverShift> driverShifts = driverShiftRepository.findAll();
        String body = mapper.writeValueAsString(driverShifts);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Get list of shifts for driver with id")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_DRIVER') || hasRole('ROLE_RIDER')")
    @GetMapping("/all_driver")
    public ResponseEntity<String> getDriverShiftsById(
        @ApiParam(name="driverid", type="Long", value="driverid of the driver", example="123", required=true) @RequestParam Long userid
        )
        throws JsonProcessingException {

        Iterable<DriverShift> driverShifts = driverShiftRepository.findAllByDriverid(driverid);
        String body = mapper.writeValueAsString(driverShifts);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Create a new shift")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @PostMapping("/post")
    public RideRequest postDriverShift(
        @ApiParam(name="userid", type="Long", value="driverid of the driver", example="123", required=true)  @RequestParam Long driverid,
        @ApiParam(name="fullName", type="String", value="name of the driver", example="Joe", required=true) @RequestParam String fullName,
        @ApiParam(name="day", type="String", value="day, one of [Monday, Tuesday,..., Sunday]", example="Monday", required=true) @RequestParam String day,
        @ApiParam(name="startTime", type="String", value="start time, format: HH:MM XM", example="12:00 AM", required=true) @RequestParam String startTime,
        @ApiParam(name="stopTime", type="String", value="stop time, format: HH:MM XM", example="12:00 AM", required=true) @RequestParam String stopTime,
        @ApiParam(name="backupDriver", type="String", value="name of backup driver", example="Bob", required=true) @RequestParam String backupDriver
        )
        throws JsonProcessingException {

        DriverShift driverShift = new DriverShift();
        rideRequest.setDriverid(driverid);
        rideRequest.setFullName(fullName);
        rideRequest.setDay(day);
        rideRequest.setStartTime(startTime);
        rideRequest.setStopTime(stopTime);
        rideRequest.setBackupDriver(backupDriver);

        DriverShift saved = driverShiftRepository.save(driverShift);

        return saved;
    }

    @ApiOperation(value = "Delete a driver shift")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_DRIVER')")
    @DeleteMapping("/delete")
    public Object deleteDriverShift(
            @ApiParam("id") @RequestParam Long id) {
              DriverShift driverShift = driverShiftRepository.findById(id)
          .orElseThrow(() -> new EntityNotFoundException(DriverShift.class, id));

          driverShiftRepository.delete(driverShift);

        return genericMessage("Driver shift with id %s deleted".formatted(id));
    }

    @ApiOperation(value = "Update a driver shift")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_DRIVER')")
    @PutMapping("/put")
    public DriverShift updateDriverShift(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid DriverShift incoming) {

        DriverShift driverShift = driverShiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(DriverShift.class, id));

        driverShift.setFullName(incoming.getFullName());
        driverShift.setDay(incoming.getDay());
        driverShift.setStartTime(incoming.getStartTime());
        driverShift.setStopTime(incoming.getStopTime());
        driverShift.setBackupDriver(incoming.getBackupDriver());

        driverShiftRepository.save(driverShift);

        return driverShift;
    }
}