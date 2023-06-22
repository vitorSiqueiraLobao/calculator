package com.nextar.calculator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextar.calculator.classes.CalculatorParameters;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CalculatorApplication.class)
@AutoConfigureMockMvc
class CalculatorApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testScenario1() throws Exception {
		CalculatorParameters request = new CalculatorParameters("(2+2)*3");
		ObjectMapper objectMapper = new ObjectMapper();
		mockMvc.perform(MockMvcRequestBuilders.post("/calculate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
		)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.resultado",equalTo(12.0)));
	}

	@Test
	public void testScenario2() throws Exception {
		CalculatorParameters request = new CalculatorParameters("2.2+2.2");
		ObjectMapper objectMapper = new ObjectMapper();
		mockMvc.perform(MockMvcRequestBuilders.post("/calculate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.resultado",equalTo(4.4)));
		mockMvc.perform(MockMvcRequestBuilders.post("/calculate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.resultado",equalTo(4.4)));
	}

	@Test
	public void testScenario3() throws Exception {
		CalculatorParameters request = new CalculatorParameters("2.3*2.3+5");
		ObjectMapper objectMapper = new ObjectMapper();
		mockMvc.perform(MockMvcRequestBuilders.post("/calculate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.resultado",equalTo(10.29)));
	}

	@Test
	public void testScenario4() throws Exception {
		CalculatorParameters request = new CalculatorParameters("2.33/3");
		ObjectMapper objectMapper = new ObjectMapper();
		mockMvc.perform(MockMvcRequestBuilders.post("/calculate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.resultado",equalTo(0.78)));
	}

	@Test
	public void testScenario5() throws Exception {
		CalculatorParameters request = new CalculatorParameters("1/0");
		ObjectMapper objectMapper = new ObjectMapper();
		mockMvc.perform(MockMvcRequestBuilders.post("/calculate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
				)
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void testScenario6() throws Exception {
		CalculatorParameters request = new CalculatorParameters("((22)+(63))/2");
		ObjectMapper objectMapper = new ObjectMapper();
		mockMvc.perform(MockMvcRequestBuilders.post("/calculate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.resultado",equalTo(42.5)));
	}


}
