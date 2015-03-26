package com.awards.controller;

import com.awards.model.Category;
import com.awards.repository.CategoryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryRepository categoryRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .build();
    }

    @Test
    public void shouldReturnCategories() throws Exception {
        when(categoryRepository.findAll())
                .thenReturn(getResponse());

        MockHttpServletResponse response = mockMvc.perform(get("/categories"))
                .andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));

        List<Category> categories = new ObjectMapper()
                .readValue(response.getContentAsString(), new TypeReference<List<Category>>() { });

        assertThat(categories, hasSize(2));
    }

     @Test
    public void shouldReturnEmptyResponse() throws Exception {
        when(categoryRepository.findAll())
                .thenReturn(new ArrayList<Category>());

        MockHttpServletResponse response = mockMvc.perform(get("/categories"))
                .andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));

        List<Category> categories = new ObjectMapper() .readValue(response.getContentAsString(),
                new TypeReference<List<Category>>() { });

         assertThat(categories, hasSize(0));
    }

    @Test
    public void shouldDeleteOneCategory() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete("/category/delete/lorem"))
                .andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is("Category deleted"));
    }

    @Test
    public void shouldSaveCategories() throws Exception {
        String requestJson="[{\n" +
                "  \"name\":\"lorem ipsum\",\n" +
                "  \"description\" : \"more lorem ipsum\",\n" +
                "  \"imageUrl\" : \"lorem ipsum image\"\n" +
                "},\n" +
                "{\n" +
                "  \"name\":\"lorem ipsum\",\n" +
                "  \"description\" : \"more lorem ipsum\",\n" +
                "  \"imageUrl\" : \"lorem ipsum image\"\n" +
                "}\n" +
                "]";

        MockHttpServletResponse response = mockMvc.perform(post("/category/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)).andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.CREATED.value()));
        assertThat(response.getContentAsString(), is("Categories created"));
    }


    @Test
    public void shouldFindACategoryById() throws Exception{
         when(categoryRepository.findOne(anyString()))
                .thenReturn(new Category("lorem","ipsum","ipsumz"));

        MockHttpServletResponse response = mockMvc.perform(get("/category/idd12345"))
                .andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));

        Category category = new ObjectMapper()
                .readValue(response.getContentAsString(), new TypeReference<Category>() { });

        assertThat(category, hasProperty("name", equalTo("lorem")));
        assertThat(category, hasProperty("description", equalTo("ipsum")));
        assertThat(category, hasProperty("imageUrl", equalTo("ipsumz")));
    }

    private List<Category> getResponse() { List<Category> categories = new ArrayList<Category>();
        categories.add(new Category("lorem","ipsum","ipslum"));
        categories.add(new Category("lorem","ipsum","ipslum"));
        return categories;
    }
}
