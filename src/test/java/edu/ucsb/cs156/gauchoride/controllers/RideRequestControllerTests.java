package edu.ucsb.cs156.gauchoride.controllers;

import edu.ucsb.cs156.gauchoride.entities.User;
import edu.ucsb.cs156.gauchoride.repositories.UserRepository;
import edu.ucsb.cs156.gauchoride.ControllerTestCase;
import edu.ucsb.cs156.gauchoride.entities.RideRequest;
import edu.ucsb.cs156.gauchoride.repositories.RideRequestRepository;
import edu.ucsb.cs156.gauchoride.testconfig.TestConfig;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.FlashAttributeResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.MediaType;

@WebMvcTest(controllers = RideRequestController.class)
@Import(TestConfig.class)
public class RideRequestControllerTests extends ControllerTestCase {

    @MockBean
    RideRequestRepository rideRequestRepository;

    @MockBean
    UserRepository userRepository;

    @WithMockUser(roles = { "ADMIN" })
    @Test
	  public void admin_edit() throws Exception {
        // arrange
        RideRequest r1 = RideRequest.builder()
            .riderId(1L)
            .day("Monday")
            .course("ART 1")
            .startTime("12AM")
            .stopTime("12AM")
            .building("Phelps")
            .room("3525")
            .pickupLocation("asap")
            .build();
  
        RideRequest r2 = RideRequest.builder()
            .riderId(1L)
            .day("Tuesday")
            .course("ART 2")
            .startTime("1AM")
            .stopTime("1AM")
            .building("Girvetz")
            .room("1004")
            .pickupLocation("ASAP")
            .build();

        String requestBody = mapper.writeValueAsString(r2);

        when(rideRequestRepository.findById(eq(4L))).thenReturn(Optional.of(r1));

        // act
        MvcResult response = mockMvc.perform(
            put("/api/riderequests/put?id=4")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(requestBody)                
                    .with(csrf()))
                .andExpect(status().isOk()).andReturn();
  
        // assert
        verify(rideRequestRepository, times(1)).findById(4L);
        verify(rideRequestRepository, times(1)).save(r2); // should be saved with updated info
        String responseString = response.getResponse().getContentAsString();
        assertEquals(requestBody, responseString);
	  }

    @WithMockUser(roles = { "ADMIN" })
    @Test
	  public void admin_delete() throws Exception {
  
			  RideRequest r1 = RideRequest.builder()
            .riderId(1L)
            .day("Monday")
            .course("ART 1")
            .startTime("12AM")
            .stopTime("12AM")
            .building("Phelps")
            .room("3525")
            .pickupLocation("asap")
            .build();
        when(rideRequestRepository.findById(eq(6L))).thenReturn(Optional.of(r1));

        // act
        MvcResult response = mockMvc.perform(
            delete("/api/riderequests/delete?id=6")                
                .with(csrf()))
            .andExpect(status().isOk()).andReturn();

        // assert
        verify(rideRequestRepository, times(1)).findById(6L);
        verify(rideRequestRepository, times(1)).delete(any());

        Map<String, Object> json = responseToJson(response);
        assertEquals("Ride request with id 6 deleted", json.get("message"));
	  }

    @WithMockUser(roles = { "ADMIN" })
    @Test
	  public void admin_edit_does_not_exist() throws Exception {
        // arrange

        RideRequest r2 = RideRequest.builder()
            .riderId(1L)
            .day("Tuesday")
            .course("ART 2")
            .startTime("1AM")
            .stopTime("1AM")
            .building("Girvetz")
            .room("1004")
            .pickupLocation("ASAP")
            .build();
        String requestBody = mapper.writeValueAsString(r2);

        when(rideRequestRepository.findById(eq(5L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
            put("/api/riderequests/put?id=5")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(requestBody)
                    .with(csrf()))
                .andExpect(status().isNotFound()).andReturn();
  
        // assert
        verify(rideRequestRepository, times(1)).findById(5L);
        Map<String, Object> json = responseToJson(response);
        assertEquals("RideRequest with id 5 not found", json.get("message"));
    }
  
    @WithMockUser(roles = { "ADMIN" })
    @Test          
	  public void admin_delete_non_existant()
        throws Exception {
        // arrange

        when(rideRequestRepository.findById(eq(10L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
            delete("/api/riderequests/delete?id=10")
                .with(csrf()))
            .andExpect(status().isNotFound()).andReturn();
  
        // assert
        verify(rideRequestRepository, times(1)).findById(10L);
        Map<String, Object> json = responseToJson(response);
        assertEquals("RideRequest with id 10 not found", json.get("message"));
    }

    @WithMockUser(roles = { "RIDER" })
	  @Test
	  public void rider_edit() throws Exception {
        // arrange
        RideRequest r1 = RideRequest.builder()
            .riderId(1L)
            .day("Monday")
            .course("ART 1")
            .startTime("12AM")
            .stopTime("12AM")
            .building("Phelps")
            .room("3525")
            .pickupLocation("asap")
            .build();
        
        RideRequest r2 = RideRequest.builder()
            .riderId(1L)
            .day("Tuesday")
            .course("ART 2")
            .startTime("1AM")
            .stopTime("1AM")
            .building("Girvetz")
            .room("1004")
            .pickupLocation("ASAP")
            .build();

        User u1 = User.builder().id(1L).build();
        String requestBody = mapper.writeValueAsString(r2);

        when(rideRequestRepository.findById(eq(4L))).thenReturn(Optional.of(r1));

        // act
        MvcResult response = mockMvc.perform(
            put("/api/riderequests/put/rider?id=4")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(requestBody)
                    .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(rideRequestRepository, times(1)).findById(4L);
        verify(rideRequestRepository, times(1)).save(r2); // should be saved with updated info
        String responseString = response.getResponse().getContentAsString();
        assertEquals(requestBody, responseString);
	  }

    @WithMockUser(roles = { "RIDER" })
	  @Test
	  public void rider_delete() throws Exception {
		    RideRequest r1 = RideRequest.builder()
            .riderId(1L)
            .day("Monday")
            .course("ART 1")
            .startTime("12AM")
            .stopTime("12AM")
            .building("Phelps")
            .room("3525")
            .pickupLocation("asap")
            .build();

        User u1 = User.builder().id(1L).build();
        when(rideRequestRepository.findById(eq(6L))).thenReturn(Optional.of(r1));

        // act
        MvcResult response = mockMvc.perform(
            delete("/api/riderequests/delete/rider?id=6")
                .with(csrf()))
            .andExpect(status().isOk()).andReturn();

        // assert
        verify(rideRequestRepository, times(1)).findById(6L);
        verify(rideRequestRepository, times(1)).delete(any());

        Map<String, Object> json = responseToJson(response);
        assertEquals("Ride request with id 6 deleted", json.get("message"));
	  }

    @WithMockUser(roles = { "RIDER" })
    @Test
    public void rider_edit_does_not_exist() throws Exception {
        // arrange

        RideRequest r2 = RideRequest.builder()
            .riderId(1L)
            .day("Tuesday")
            .course("ART 2")
            .startTime("1AM")
            .stopTime("1AM")
            .building("Girvetz")
            .room("1004")
            .pickupLocation("ASAP")
            .build();

        User u1 = User.builder().id(1L).build();
        String requestBody = mapper.writeValueAsString(r2);

        when(rideRequestRepository.findById(eq(5L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
            put("/api/riderequests/put/rider?id=5")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(requestBody)
                    .with(csrf()))
                .andExpect(status().isNotFound()).andReturn();

        // assert
        verify(rideRequestRepository, times(1)).findById(5L);
        Map<String, Object> json = responseToJson(response);
        assertEquals("RideRequest with id 5 not found", json.get("message"));
    }

    @WithMockUser(roles = { "RIDER" })
    @Test
    public void rider_delete_non_existant()
        throws Exception {
        // arrange

        User u1 = User.builder().id(1L).build();
        when(rideRequestRepository.findById(eq(10L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
            delete("/api/riderequests/delete/rider?id=10")
                .with(csrf()))
            .andExpect(status().isNotFound()).andReturn();

        // assert
        verify(rideRequestRepository, times(1)).findById(10L);
        Map<String, Object> json = responseToJson(response);
        assertEquals("RideRequest with id 10 not found", json.get("message"));
	  }

    @WithMockUser(roles = { "RIDER" })
	  @Test
	  public void rider_edit_unowned() throws Exception {
        // arrange

        RideRequest r1 = RideRequest.builder()
            .riderId(2L)
            .day("Monday")
            .course("ART 1")
            .startTime("12AM")
            .stopTime("12AM")
            .building("Phelps")
            .room("3525")
            .pickupLocation("asap")
            .build();
          RideRequest r2 = RideRequest.builder()
            .riderId(2L)
            .day("Tuesday")
            .course("ART 2")
            .startTime("1AM")
            .stopTime("1AM")
            .building("Girvetz")
            .room("1004")
            .pickupLocation("ASAP")
            .build();

        User u1 = User.builder().id(1L).build();
        String requestBody = mapper.writeValueAsString(r2);

        when(rideRequestRepository.findById(eq(4L))).thenReturn(Optional.of(r1));

        // act
        mockMvc.perform(
            put("/api/riderequests/put/rider?id=4")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().is(403));
	  }

    @WithMockUser(roles = { "RIDER" })
	  @Test
	  public void rider_delete_unowned_request()
        throws Exception {
        // arrange

        RideRequest r2 = RideRequest.builder()
            .riderId(2L)
            .day("Monday")
            .course("ART 1")
            .startTime("12AM")
            .stopTime("12AM")
            .building("Phelps")
            .room("3525")
            .pickupLocation("asap")
            .build();
        User u1 = User.builder().id(1L).build();
        when(rideRequestRepository.findById(eq(10L))).thenReturn(Optional.of(r2));

        // act
        mockMvc.perform(
            delete("/api/riderequests/delete/rider?id=10")
                .with(csrf()))
            .andExpect(status().is(403));
	  }

}
