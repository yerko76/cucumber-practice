package component.category;

import com.awards.model.Category;
import com.awards.repository.CategoryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import component.configuration.ComponentHelper;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebAppConfiguration
@ContextConfiguration("classpath:cucumber.xml")
public class CategorySteps {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MongoTemplate mongoTemplate;

    private MockMvc mockMvc;

    @Before
    public void clearMap(){
        mongoTemplate.getDb().dropDatabase();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Given("^I have  list of categories in db$")
    public void I_have_list_of_categories(List<Category> categories) throws Throwable {
        categoryRepository.save(categories);
    }

    @When("^I hit the categories api$")
    public void I_hit_the_categories_api() throws Throwable {
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/categories").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        ComponentHelper.getMap().put("response", response);
    }

    @Then("^I should receive a succesful response$")
    public void I_should_receive_a_succesful_response() throws Throwable {
        MockHttpServletResponse response = (MockHttpServletResponse) ComponentHelper.getMap().get("response");
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @And("^the response is a list containing (\\d+) categories$")
    public void the_response_is_a_list_containing_two_categories(int expectedElements) throws Throwable {
        MockHttpServletResponse response = (MockHttpServletResponse) ComponentHelper.getMap().get("response");
        List<Category> categories = new ObjectMapper() .readValue(response.getContentAsString(),
                new TypeReference<List<Category>>() { });

        assertEquals(categories.size(), expectedElements);
        ComponentHelper.getMap().put("categories", categories);
    }

    @And("^I should see (one category|updated categories|categories) with the following attributes$")
    public void I_should_see_categories_with_the_following_attributes(String type, List<Category> expectedCategories) throws Throwable {
        MockHttpServletResponse response = (MockHttpServletResponse) ComponentHelper.getMap().get("response");
        if("categories".equals(type)){
            List<Category> categories = new ObjectMapper() .readValue(response.getContentAsString(),
                    new TypeReference<List<Category>>() { });
            executeAssertionsForCategories(expectedCategories, categories);

        } else if("updated categories".equals(type)){
            List<Category> categories = categoryRepository.findByIds(getCategoryIds(expectedCategories));
            executeAssertionsForCategories(expectedCategories, categories);
        } else {
            Category category = new ObjectMapper() .readValue(response.getContentAsString(),
                    new TypeReference<Category>() { });

            executeAssertionsForSingleCategory(expectedCategories, category);
        }

    }

    private List<String> getCategoryIds(List<Category> expectedCategories) {
        List<String> categoryIds = new ArrayList<String>();
        for (Category category : expectedCategories){
            categoryIds.add(category.getId());
        }

        return categoryIds;
    }

    private void executeAssertionsForCategories(List<Category> expectedCategories, List<Category> categories) {
        int i=0;
        for(Category category : categories){
            assertThat(category.getId(), is(expectedCategories.get(i).getId()));
            assertThat(category.getName(), is(expectedCategories.get(i).getName()));
            assertThat(category.getDescription(), is(expectedCategories.get(i).getDescription()));
            assertThat(category.getImageUrl(), is(expectedCategories.get(i).getImageUrl()));
            i++;
        }
    }

    private void executeAssertionsForSingleCategory(List<Category> expectedCategories, Category category) {
        assertThat(category.getId(), is(expectedCategories.get(0).getId()));
        assertThat(category.getName(), is(expectedCategories.get(0).getName()));
        assertThat(category.getDescription(), is(expectedCategories.get(0).getDescription()));
        assertThat(category.getImageUrl(), is(expectedCategories.get(0).getImageUrl()));
    }

    @Given("^I have a request to (?:save|update) the following categories$")
    public void I_have_list_of_categories(String categoriesRequest) throws Throwable {
            ComponentHelper.getMap().put("request", categoriesRequest);
    }

    @When("^I hit categories api to save this categories$")
    public void I_hit_categories_api_to_save_this_categories() throws Throwable {
        MockHttpServletResponse response = mockMvc.perform(post("/category/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content((String) ComponentHelper.getMap().get("request")))
                .andReturn()
                .getResponse();
       ComponentHelper.getMap().put("response", response);
    }


    @Then("^I should receive a create response with the following message: (.*)$")
    public void I_should_receive_a_create_response(String message) throws Throwable {
        MockHttpServletResponse response = (MockHttpServletResponse) ComponentHelper.getMap().get("response");
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(message, response.getContentAsString());
    }

    @And("^I should have (\\d+) categories into db$")
    public void I_should_have_categories_into_db(long elements) throws Throwable {
        assertThat(categoryRepository.count(), is(elements));
    }

    @When("^I hit categories api passing id: (\\w+)$")
    public void I_hit_categories_api_passing_id(String categoryId) throws Throwable {
        MockHttpServletResponse response = mockMvc.perform(get("/category/" + categoryId))
                .andReturn().getResponse();
        ComponentHelper.getMap().put("response", response);
    }

    @When("^I hit categories api to delete one category using id: (\\w+)$")
    public void I_hit_categories_api_to_delete_one_category_using_id(String categoryId) throws Throwable {
        MockHttpServletResponse response = mockMvc.perform(delete("/category/delete/" + categoryId))
                .andReturn().getResponse();
        ComponentHelper.getMap().put("response", response);
    }

    @And("^I should have (\\d+) category in db$")
    public void I_should_have_category_in_db(long element) throws Throwable {
        assertEquals(element, categoryRepository.count());
    }

    @When("^I hit categories api to update this categories$")
    public void I_hit_categories_api_to_update_this_categories() throws Throwable {
        MockHttpServletResponse response = mockMvc.perform(post("/category/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content((String) ComponentHelper.getMap().get("request")))
                .andReturn()
                .getResponse();
        ComponentHelper.getMap().put("response", response);
    }
}
