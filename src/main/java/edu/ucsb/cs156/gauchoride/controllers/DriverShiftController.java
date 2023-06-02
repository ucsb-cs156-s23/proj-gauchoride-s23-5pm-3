package edu.ucsb.cs156.gauchoride.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ucsb.cs156.gauchoride.entities.DriverShift;
import edu.ucsb.cs156.gauchoride.repositories.DriverShiftRepository;

import edu.ucsb.cs156.gauchoride.entities.User;

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

@Api(description = "Driver shift information")
@RequestMapping("/api/drivershifts")
@RestController
public class DriverShiftController extends ApiController {
    @Autowired
    DriverShiftRepository driverShiftRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "Get a list of all driver shifts")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_DRIVER') || hasRole('ROLE_RIDER')")
    @GetMapping("/all")
    public ResponseEntity<String> allDriverShifts()
            throws JsonProcessingException {
        Iterable<DriverShift> driverShifts = driverShiftRepository.findAll();
        String body = mapper.writeValueAsString(driverShifts);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Create a new driver shift")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_DRIVER')")
    @PostMapping("/post")
    public DriverShift postDriverShift(
        @ApiParam(name="day", type="String", value="day, one of [Monday, Tuesday,..., Sunday]", example="Monday", required=true) @RequestParam String day,
        @ApiParam(name="startTime", type="String", value="start time, format: HH:MM XM", example="12:00 AM", required=true) @RequestParam String startTime,
        @ApiParam(name="stopTime", type="String", value="stop time, format: HH:MM XM", example="12:00 AM", required=true) @RequestParam String stopTime
        )
        throws JsonProcessingException {

        User user = getCurrentUser().getUser();

        DriverShift driverShift = new DriverShift();
        driverShift.setDriver(user);
        driverShift.setDay(day);
        driverShift.setStartTime(startTime);
        driverShift.setStopTime(stopTime);

        DriverShift saved = driverShiftRepository.save(driverShift);

        return saved;
    }
}