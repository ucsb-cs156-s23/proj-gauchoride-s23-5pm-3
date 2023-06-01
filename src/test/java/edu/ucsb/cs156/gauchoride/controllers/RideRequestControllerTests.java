package edu.ucsb.cs156.gauchoride.controllers;

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

    @Test
    public void rideRequests__logged_out() throws Exception {
    mockMvc.perform(get("/api/riderequests/all"))
        .andExpect(status().is(403));
    }

    @WithMockUser(roles = { "RIDER" })
    @Test
    public void rideRequests__rider_all_fail() throws Exception {
    mockMvc.perform(get("/api/riderequests/all"))
        .andExpect(status().is(403));
    }

  @WithMockUser(roles = { "ADMIN", "DRIVER" })
  @Test
  public void rideRequests__admin_driver_fail() throws Exception {
    mockMvc.perform(get("/api/riderequests/all_rider?userid=1"))
        .andExpect(status().is(403));
  }


  @WithMockUser(roles = { "ADMIN", "DRIVER" })
  @Test
  public void rideRequests__admin_driver_succ() throws Exception {

    // arrange

    RideRequest u1 = RideRequest.builder()
        .userid(1L)
        .day("Monday")
        .fullName("Bob")
        .course("ART 1")
        .startTime("12AM")
        .stopTime("12AM")
        .building("Phelps")
        .room("3525")
        .pickup("asap")
        .build();
    RideRequest u2 = RideRequest.builder()
        .userid(2L)
        .day("Monday")
        .fullName("Victor")
        .course("ART 1")
        .startTime("12AM")
        .stopTime("12AM")
        .building("Phelps")
        .room("3525")
        .pickup("asap")
        .build();

    ArrayList<RideRequest> expectedRideRequests = new ArrayList<>();
    expectedRideRequests.addAll(Arrays.asList(u1, u2));

    when(rideRequestRepository.findAll()).thenReturn(expectedRideRequests);
    String expectedJson = mapper.writeValueAsString(expectedRideRequests);
    
    // act

    MvcResult response = mockMvc.perform(get("/api/riderequests/all"))
        .andExpect(status().isOk()).andReturn();

    // assert

    verify(rideRequestRepository, times(1)).findAll();
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);

  }

  @WithMockUser(roles = { "RIDER" })
  @Test
  public void rideRequests__rider_succ() throws Exception {

    // arrange

    RideRequest u1 = RideRequest.builder()
        .userid(1L)
        .day("Monday")
        .fullName("Bob")
        .course("ART 1")
        .startTime("12AM")
        .stopTime("12AM")
        .building("Phelps")
        .room("3525")
        .pickup("asap")
        .build();
    RideRequest u2 = RideRequest.builder()
        .userid(2L)
        .day("Monday")
        .fullName("Victor")
        .course("ART 1")
        .startTime("12AM")
        .stopTime("12AM")
        .building("Phelps")
        .room("3525")
        .pickup("asap")
        .build();

    ArrayList<RideRequest> expectedRideRequests = new ArrayList<>();
    expectedRideRequests.addAll(Arrays.asList(u1));

    when(rideRequestRepository.findAllByUserid(1L)).thenReturn(expectedRideRequests);
    String expectedJson = mapper.writeValueAsString(expectedRideRequests);
    
    // act

    MvcResult response = mockMvc.perform(get("/api/riderequests/all_rider?userid=1"))
        .andExpect(status().isOk()).andReturn();

    // assert

    verify(rideRequestRepository, times(1)).findAllByUserid(1L);
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);

  }

	@WithMockUser(roles = { "RIDER" })
	@Test
	public void rider_post() throws Exception {
			// arrange

			RideRequest u1 = RideRequest.builder()
        .userid(1L)
        .day("Monday")
        .fullName("Bob")
        .course("ART 1")
        .startTime("12AM")
        .stopTime("12AM")
        .building("Phelps")
        .room("3525")
        .pickup("asap")
        .build();
			when(rideRequestRepository.save(eq(u1))).thenReturn(u1);

			// act
			MvcResult response = mockMvc.perform(
							post("/api/riderequests/post")
							.param("userid", "1")
							.param("day", "Monday")
							.param("fullName", "Bob")
							.param("course", "ART 1")
							.param("startTime", "12AM")
							.param("stopTime", "12AM")
							.param("building", "Phelps")
							.param("room", "3525")
							.param("pickup", "asap")
											.with(csrf()))
							.andExpect(status().isOk()).andReturn();

			// assert
			verify(rideRequestRepository, times(1)).save(u1);
			String expectedJson = mapper.writeValueAsString(u1);
			String responseString = response.getResponse().getContentAsString();
			assertEquals(expectedJson, responseString);
	}

}
