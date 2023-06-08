package edu.ucsb.cs156.gauchoride.controllers;

import edu.ucsb.cs156.gauchoride.repositories.UserRepository;
import edu.ucsb.cs156.gauchoride.ControllerTestCase;
import edu.ucsb.cs156.gauchoride.entities.DriverShift;
import edu.ucsb.cs156.gauchoride.repositories.DriverShiftProjection;
import edu.ucsb.cs156.gauchoride.repositories.DriverShiftRepository;
import edu.ucsb.cs156.gauchoride.testconfig.TestConfig;
import edu.ucsb.cs156.gauchoride.entities.User;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.ArrayList;
import java.util.Arrays;

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
	DriverShiftProjection dsp1 = new DriverShiftProjection() {
		public long getId(){return 1L;}
		public String getDay(){return "Monday";}
		public String getStartTime(){return "12:00 PM";}
		public String getStopTime(){return "3:00 PM";}
		public UserProjection getDriver(){
			return new UserProjection() {
				public long getId(){return 1L;}
				public String getFullName(){return "Fake Name";}
			};
		}
	};
	DriverShiftProjection dsp2 = new DriverShiftProjection() {
		public long getId(){return 2L;}
		public String getDay(){return "Tuesday";}
		public String getStartTime(){return "12:00 PM";}
		public String getStopTime(){return "3:00 PM";}
		public UserProjection getDriver(){
			return new UserProjection() {
				public long getId(){return 1L;}
				public String getFullName(){return "Fake Name";}
			};
		}
	};

    ArrayList<DriverShiftProjection> expectedDriverShifts = new ArrayList<>();
    expectedDriverShifts.addAll(Arrays.asList(dsp1,dsp2));

    when(driverShiftRepository.getAll()).thenReturn(expectedDriverShifts);
    String expectedJson = mapper.writeValueAsString(expectedDriverShifts);

    // act

    MvcResult response = mockMvc.perform(get("/api/drivershifts/all"))
        .andExpect(status().isOk()).andReturn();

    // assert

    verify(driverShiftRepository, times(1)).getAll();
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);

    }

	@WithMockUser(roles = { "ADMIN", "DRIVER" })
	@Test
	public void driver_post() throws Exception {
		// arrange
    User u = currentUserService.getUser();

		DriverShift d1 = DriverShift.builder()
				.day("Monday")
				.startTime("12AM")
				.stopTime("12AM")
				.driver(u)
				.build();
		when(driverShiftRepository.save(eq(d1))).thenReturn(d1);

		// act
		MvcResult response = mockMvc.perform(
				post("/api/drivershifts/post")
						.param("day", "Monday")
						.param("startTime", "12AM")
						.param("stopTime", "12AM")
						.with(csrf()))
				.andExpect(status().isOk()).andReturn();

		// assert
		verify(driverShiftRepository, times(1)).save(d1);
		String expectedJson = mapper.writeValueAsString(d1);
		String responseString = response.getResponse().getContentAsString();
		assertEquals(expectedJson, responseString);
	}
}