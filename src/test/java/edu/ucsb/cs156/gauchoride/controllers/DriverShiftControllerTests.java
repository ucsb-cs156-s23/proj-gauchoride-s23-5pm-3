package edu.ucsb.cs156.gauchoride.controllers;

import edu.ucsb.cs156.gauchoride.repositories.UserRepository;
import edu.ucsb.cs156.gauchoride.ControllerTestCase;
import edu.ucsb.cs156.gauchoride.entities.DriverShift;
import edu.ucsb.cs156.gauchoride.repositories.DriverShiftRepository;
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

@WebMvcTest(controllers = DriverShiftController.class)
@Import(TestConfig.class)
public class DriverShiftControllerTests extends ControllerTestCase {

  @MockBean
  DriverShiftRepository driverShiftRepository;

	@MockBean
	UserRepository userRepository;

  @Test
  public void driverShifts__logged_out() throws Exception {
    mockMvc.perform(get("/api/drivershifts/all"))
        .andExpect(status().is(403));
  }

  @WithMockUser(roles = { "ADMIN", "DRIVER", "RIDER" })
  @Test
  public void driverShifts_succ() throws Exception {

    // arrange

    DriverShift u1 = DriverShift.builder()
        .driverid(1L)
        .fullName("Bob")
        .day("Monday")
        .startTime("12AM")
        .stopTime("12AM")
        .backupDriver("")
        .build();
    DriverShift u2 = DriverShift.builder()
		.driverid(2L)
        .fullName("Victor")
        .day("Monday")
        .startTime("12AM")
        .stopTime("12AM")
        .backupDriver("")
        .build();

    ArrayList<DriverShift> expectedDriverShifts = new ArrayList<>();
    expectedDriverShifts.addAll(Arrays.asList(u1, u2));

    when(driverShiftRepository.findAll()).thenReturn(expectedDriverShifts);
    String expectedJson = mapper.writeValueAsString(expectedDriverShifts);
    
    // act

    MvcResult response = mockMvc.perform(get("/api/drivershifts/all"))
        .andExpect(status().isOk()).andReturn();

    // assert

    verify(driverShiftRepository, times(1)).findAll();
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);

  }

  @WithMockUser(roles = { "RIDER" })
  @Test
  public void driverShifts_getById_succ() throws Exception {

    // arrange

    DriverShift u1 = DriverShift.builder()
        .driverid(1L)
        .fullName("Bob")
        .day("Monday")
        .startTime("12AM")
        .stopTime("12AM")
        .backupDriver("")
        .build();
    DriverShift u2 = DriverShift.builder()
        .driverid(2L)
        .fullName("Victor")
        .day("Monday")
        .startTime("12AM")
        .stopTime("12AM")
        .backupDriver("")
        .build();

    ArrayList<DriverShift> expectedDriverShifts = new ArrayList<>();
    expectedDriverShifts.addAll(Arrays.asList(u1));

    when(driverShiftRepository.findAllByDriverid(1L)).thenReturn(expectedDriverShifts);
    String expectedJson = mapper.writeValueAsString(expectedDriverShifts);
    
    // act

    MvcResult response = mockMvc.perform(get("/api/drivershifts/all_driver?driverid=1"))
        .andExpect(status().isOk()).andReturn();

    // assert

    verify(driverShiftRepository, times(1)).findAllByDriverid(1L);
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);

  }

	@WithMockUser(roles = { "DRIVER" })
	@Test
	public void driver_post() throws Exception {
			// arrange

		DriverShift u1 = DriverShift.builder()
			.driverid(1L)
			.fullName("Bob")
			.day("Monday")
			.startTime("12AM")
			.stopTime("12AM")
			.backupDriver("")
			.build();
			when(driverShiftRepository.save(eq(u1))).thenReturn(u1);

			// act
			MvcResult response = mockMvc.perform(
							post("/api/drivershifts/post")
							.param("driverid", "1")
							.param("fullName", "Bob")
							.param("day", "Monday")
							.param("startTime", "12AM")
							.param("stopTime", "12AM")
							.param("backupDriver", "")
											.with(csrf()))
							.andExpect(status().isOk()).andReturn();

			// assert
			verify(driverShiftRepository, times(1)).save(u1);
			String expectedJson = mapper.writeValueAsString(u1);
			String responseString = response.getResponse().getContentAsString();
			assertEquals(expectedJson, responseString);
	}

	@WithMockUser(roles = { "ADMIN", "DRIVER" })
	@Test
	public void delete_test() throws Exception {

		DriverShift u1 = DriverShift.builder()
			.driverid(1L)
			.fullName("Bob")
			.day("Monday")
			.startTime("12AM")
			.stopTime("12AM")
			.backupDriver("")
			.build();

			when(driverShiftRepository.findById(eq(6L))).thenReturn(Optional.of(u1));

			// act
			MvcResult response = mockMvc.perform(
											delete("/api/drivershifts/delete?id=6")
																			.with(csrf()))
											.andExpect(status().isOk()).andReturn();

			// assert
			verify(driverShiftRepository, times(1)).findById(6L);
			verify(driverShiftRepository, times(1)).delete(any());

			Map<String, Object> json = responseToJson(response);
			assertEquals("Driver shift with id 6 deleted", json.get("message"));
	}

	@WithMockUser(roles = { "ADMIN", "DRIVER" })
	@Test
	public void delete_non_existant()
					throws Exception {
					// arrange

					when(driverShiftRepository.findById(eq(10L))).thenReturn(Optional.empty());

					// act
					MvcResult response = mockMvc.perform(
													delete("/api/drivershifts/delete?id=10")
																					.with(csrf()))
													.andExpect(status().isNotFound()).andReturn();

					// assert
					verify(driverShiftRepository, times(1)).findById(10L);
					Map<String, Object> json = responseToJson(response);
					assertEquals("DriverShift with id 10 not found", json.get("message"));
	}

	@WithMockUser(roles = { "ADMIN", "DRIVER" })
	@Test
	public void admin_rider_edit() throws Exception {
		// arrange

		DriverShift u1 = DriverShift.builder()
			.driverid(1L)
			.fullName("Bob")
			.day("Monday")
			.startTime("12AM")
			.stopTime("12AM")
			.backupDriver("")
			.build();
		DriverShift u2 = DriverShift.builder()
			.driverid(1L)
			.fullName("Victor")
			.day("Tuesday")
			.startTime("12PM")
			.stopTime("12PM")
			.backupDriver("Someone")
			.build();

		String requestBody = mapper.writeValueAsString(u2);

		when(driverShiftRepository.findById(eq(4L))).thenReturn(Optional.of(u1));

		// act
		MvcResult response = mockMvc.perform(
			put("/api/drivershifts/put?id=4")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.content(requestBody)
				.with(csrf()))
			.andExpect(status().isOk()).andReturn();

		// assert
		verify(driverShiftRepository, times(1)).findById(4L);
		verify(driverShiftRepository, times(1)).save(u2); // should be saved with updated info
		String responseString = response.getResponse().getContentAsString();
		assertEquals(requestBody, responseString);
	}

	@WithMockUser(roles = { "ADMIN", "DRIVER" })
	@Test
	public void edit_does_not_exist() throws Exception {
		// arrange

		DriverShift u2 = DriverShift.builder()
			.driverid(1L)
			.fullName("Bob")
			.day("Monday")
			.startTime("12AM")
			.stopTime("12AM")
			.backupDriver("")
			.build();
		String requestBody = mapper.writeValueAsString(u2);

		when(driverShiftRepository.findById(eq(5L))).thenReturn(Optional.empty());

		// act
		MvcResult response = mockMvc.perform(
			put("/api/drivershifts/put?id=5")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.content(requestBody)
				.with(csrf()))
			.andExpect(status().isNotFound()).andReturn();

		// assert
		verify(driverShiftRepository, times(1)).findById(5L);
		Map<String, Object> json = responseToJson(response);
		assertEquals("DriverShift with id 5 not found", json.get("message"));

	}

}
