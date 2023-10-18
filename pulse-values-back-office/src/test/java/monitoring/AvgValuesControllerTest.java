package monitoring;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import monitoring.controller.AvgPulseController;
import monitoring.service.AvgPulseValuesService;

@WebMvcTest(AvgPulseController.class)
public class AvgValuesControllerTest {
private static final Integer AVG_VALUE_DATES = 90;
private static final Integer MIN_VALUE_DATES = 80;
private static final Integer MAX_VALUE_DATES = 100;
@Autowired
MockMvc mockMvc;
@MockBean
AvgPulseValuesService pulseService;
@BeforeEach
void serviceMocking() {
	
	when(pulseService.getAvgValue(ArgumentMatchers.anyLong(),
			ArgumentMatchers.any(LocalDateTime.class),
			ArgumentMatchers.any(LocalDateTime.class))).thenReturn(AVG_VALUE_DATES);
	when(pulseService.getMinValue(ArgumentMatchers.anyLong(),
			ArgumentMatchers.any(LocalDateTime.class),
			ArgumentMatchers.any(LocalDateTime.class))).thenReturn(MIN_VALUE_DATES);
	when(pulseService.getMaxValue(ArgumentMatchers.anyLong(),
			ArgumentMatchers.any(LocalDateTime.class),
			ArgumentMatchers.any(LocalDateTime.class))).thenReturn(MAX_VALUE_DATES);
	when(pulseService.getAllValues(ArgumentMatchers.anyLong(),
			ArgumentMatchers.any(LocalDateTime.class),
			ArgumentMatchers.any(LocalDateTime.class))).thenReturn(List.of(MIN_VALUE_DATES, AVG_VALUE_DATES, MAX_VALUE_DATES));
}

@Test
void datesTest() throws UnsupportedEncodingException, Exception {
	String res = mockMvc.perform(get("/pulse/values/avg/123")
			.param("from", "2023-01-01T12:00")
			.param("to", "2023-03-01T12:00")).andDo(print())
	.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	assertEquals(AVG_VALUE_DATES +"", res);
}
@Test
void allValuesTest() throws UnsupportedEncodingException, Exception {
	String resJSON = mockMvc.perform(get("/pulse/values/123")
			.param("from", "2023-01-01T12:00")
			.param("to", "2023-03-01T12:00")).andDo(print())
	.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	Integer[] expected = {MIN_VALUE_DATES, AVG_VALUE_DATES, MAX_VALUE_DATES};
	ObjectMapper mapper = new ObjectMapper();
	assertArrayEquals(expected, mapper.readValue(resJSON, Integer[].class));
	
}
@Test
void minDatesTest() throws UnsupportedEncodingException, Exception {
	String res = mockMvc.perform(get("/pulse/values/min/123")
			.param("from", "2023-01-01T12:00")
			.param("to", "2023-03-01T12:00")).andDo(print())
	.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	assertEquals(MIN_VALUE_DATES +"", res);
}
@Test
void maxDatesTest() throws UnsupportedEncodingException, Exception {
	String res = mockMvc.perform(get("/pulse/values/max/123")
			.param("from", "2023-01-01T12:00")
			.param("to", "2023-03-01T12:00")).andDo(print())
	.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	assertEquals(MAX_VALUE_DATES +"", res);
}
@Test
void dateFromTest() throws UnsupportedEncodingException, Exception {
	String res = mockMvc.perform(get("/pulse/values/avg/123")
			.param("from", "2023-01-01T12:00")).andDo(print())
	.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	assertEquals(AVG_VALUE_DATES +"", res);
}
@Test
void dateToTest() throws UnsupportedEncodingException, Exception {
	String res = mockMvc.perform(get("/pulse/values/avg/123")
			.param("to", "2023-01-01T12:00")).andDo(print())
	.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	assertEquals(AVG_VALUE_DATES +"", res);
}
@Test
void noDatesTest() throws UnsupportedEncodingException, Exception {
	String res = mockMvc.perform(get("/pulse/values/avg/123")).andDo(print())
	.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	assertEquals(AVG_VALUE_DATES +"", res);
}
@Test
void wrongToDateTest() throws UnsupportedEncodingException, Exception {
	mockMvc.perform(get("/pulse/values/avg/123")
			.param("to", "2023-01")).andDo(print())
	.andExpect(status().isBadRequest());
	
}
@Test
void wrongFromDateTest() throws UnsupportedEncodingException, Exception {
	mockMvc.perform(get("/pulse/values/avg/123")
			.param("from", "2023-01")).andDo(print())
	.andExpect(status().isBadRequest());
	
}
}
