package com.ehub.controller;

import com.ehub.common.Gender;
import com.ehub.dto.response.UserPageResponse;
import com.ehub.dto.response.UserResponse;
import com.ehub.service.JwtService;
import com.ehub.service.UserService;
import com.ehub.service.UserServiceDetail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.List;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserServiceDetail userServiceDetail;

    @MockitoBean
    private JwtService jwtService;

    private static UserResponse newbie;
    private static UserResponse learnJava;

    @BeforeAll
    static void beforeAll() {
        // Chuan bi du lieu
        newbie = new UserResponse();
        newbie.setId(1L);
        newbie.setFirstName("Newbie");
        newbie.setLastName("");
        newbie.setGender(Gender.MALE);
        newbie.setBirthday(new Date());
        newbie.setEmail("newbie@gmail.com");
        newbie.setPhone("0987654321");
        newbie.setUsername("newbie");

        learnJava = new UserResponse();
        learnJava.setId(2L);
        learnJava.setFirstName("Learn");
        learnJava.setLastName("Java");
        learnJava.setGender(Gender.MALE);
        learnJava.setBirthday(new Date());
        learnJava.setEmail("learnjava@gmail.com");
        learnJava.setPhone("0123456789");
        learnJava.setUsername("learnjava");
    }

    @Test
    @WithMockUser(authorities = {"Admin", "Manager"})
    void getList_Success() throws Exception {
        List<UserResponse> userResponses = List.of(newbie, learnJava);

        UserPageResponse userPageResponse = new UserPageResponse();
        userPageResponse.setPageNumber(0);
        userPageResponse.setPageSize(20);
        userPageResponse.setTotalPages(1);
        userPageResponse.setTotalElements(2);
        userPageResponse.setUsers(userResponses);

        Mockito.when(userService.findAll(null, null, 0, 20)).thenReturn(userPageResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/list").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("User list"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.totalElements").value(2));
    }

    @Test
    @WithMockUser(authorities = {"Admin", "Manager", "User"})
    void getUserDetail_Success() throws Exception {
        Long userId = 2L;

        Mockito.when(userService.findById(userId)).thenReturn(learnJava);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", userId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("user"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.id").value(userId));
    }
}