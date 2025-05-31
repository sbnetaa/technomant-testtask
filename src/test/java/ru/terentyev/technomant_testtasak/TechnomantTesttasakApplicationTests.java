package ru.terentyev.technomant_testtasak;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import ru.terentyev.technomant_testtasak.models.ArticleCreateRequest;
import ru.terentyev.technomant_testtasak.models.LoginRequest;
import ru.terentyev.technomant_testtasak.models.RegisterRequest;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TechnomantTesttasakApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void registerAndLogin_Success() throws Exception {
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setUsername("testuser");
		registerRequest.setPassword("password");
		registerRequest.setPasswordConfirm("password");

		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerRequest)))
				.andExpect(status().isOk());

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("testuser");
		loginRequest.setPassword("password");

		ResultActions result = mockMvc.perform(post("/api/auth/token")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists());

		String token = result.andReturn().getResponse().getContentAsString();

		mockMvc.perform(get("/api/articles")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk());
	}

	@Test
	public void createArticle_WithValidToken_ReturnsCreated() throws Exception {
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setUsername("testuser2");
		registerRequest.setPassword("password");
		registerRequest.setPasswordConfirm("password");

		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerRequest)))
				.andExpect(status().isOk());

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("testuser2");
		loginRequest.setPassword("password");

		ResultActions result = mockMvc.perform(post("/api/auth/token")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists());

		String token = result.andReturn().getResponse().getContentAsString();

		ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest();
		articleCreateRequest.setTitle("Test Article");
		articleCreateRequest.setAuthor("Test Author");
		articleCreateRequest.setContent("Test Content");
		articleCreateRequest.setPublishingDate(LocalDate.now());

		mockMvc.perform(post("/api/articles")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + token)
						.content(objectMapper.writeValueAsString(articleCreateRequest)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.title", is("Test Article")));
	}

//	@Test
//	public void getArticleStatistics_WithAdminToken_ReturnsOk() throws Exception {
//		RegisterRequest registerRequest = new RegisterRequest();
//		registerRequest.setUsername("admin");
//		registerRequest.setPassword("password");
//		registerRequest.setPasswordConfirm("password");
//
//		mockMvc.perform(post("/api/auth/register")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(objectMapper.writeValueAsString(registerRequest)))
//				.andExpect(status().isOk());
//
//		LoginRequest loginRequest = new LoginRequest();
//		loginRequest.setUsername("admin");
//		loginRequest.setPassword("password");
//
//		ResultActions result = mockMvc.perform(post("/api/auth/token")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(objectMapper.writeValueAsString(loginRequest)))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$").exists());
//
//		String token = result.andReturn().getResponse().getContentAsString();
//
//		mockMvc.perform(get("/api/articles/statistics")
//						.header("Authorization", "Bearer " + token))
//				.andExpect(status().isOk());
//	}


	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void getArticleStatistics_WithAdminToken_ReturnsOk() throws Exception {
		mockMvc.perform(get("/api/articles/statistics"))
				.andExpect(status().isOk());
	}

	@Test
	public void getArticleStatistics_WithNonAdminToken_ReturnsForbidden() throws Exception {
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setUsername("nonAdminUser");
		registerRequest.setPassword("password");
		registerRequest.setPasswordConfirm("password");

		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerRequest)))
				.andExpect(status().isOk());

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("nonAdminUser");
		loginRequest.setPassword("password");

		ResultActions result = mockMvc.perform(post("/api/auth/token")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists());

		String token = result.andReturn().getResponse().getContentAsString();

		mockMvc.perform(get("/api/articles/statistics")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isForbidden());
	}
}