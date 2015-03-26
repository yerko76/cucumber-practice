package component.nominee;


import com.awards.model.Category;
import com.awards.model.Nominee;
import com.awards.repository.CategoryRepository;
import com.awards.repository.NomineeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import component.configuration.ComponentHelper;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebAppConfiguration
@ContextConfiguration("classpath:cucumber.xml")
public class NomineeSteps {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private NomineeRepository nomineeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private MockMvc mockMvc;


    @Before
    public void clearMap(){
        mongoTemplate.getDb().dropDatabase();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @And("^I have the following nominees for (.+) category$")
    public void I_have_the_following_nominees(String categoryName, List<Nominee> nominees) throws Throwable {
        Category category = categoryRepository.findByName(categoryName);

        for(Nominee nominee : nominees){
            nominee.setCategoryId(category.getId());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ComponentHelper.getMap().put("nominees", objectMapper.writeValueAsString(nominees));
        ComponentHelper.getMap().put("categoryId", category.getId());
    }

    @When("^I hit categories api to save these nominees$")
    public void I_hit_categories_api_to_add_these_nominees_for_best_employee_category() throws Throwable {
         MockHttpServletResponse response = mockMvc.perform(post("/nominees/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content((String) ComponentHelper.getMap().get("nominees")))
                .andReturn()
                .getResponse();
        ComponentHelper.getMap().put("response", response);
    }

    @And("^I should have (\\d+) nominees for (?:best lorem ipsum) category$")
    public void I_should_have_nominees_for_best_employee_category(int elements) throws Throwable {
        MockHttpServletResponse response = (MockHttpServletResponse) ComponentHelper.getMap().get("response");
        List<Nominee> nomineesByCategory = new ObjectMapper() .readValue(response.getContentAsString(),
                new TypeReference<List<Nominee>>() { });

        assertEquals(elements, nomineesByCategory.size());
    }


    @And("^I have the following nominees for (.+) category in db$")
    public void I_have_the_following_nominees_for_best_lorem_ipsum_category_in_db(String categoryName, List<Nominee> nominees) throws Throwable {
        Category category = categoryRepository.findByName(categoryName);

        for(Nominee nominee : nominees){
            nominee.setCategoryId(category.getId());
        }

        nomineeRepository.save(nominees);
    }

    @When("^I hit categories api to retrieve all nominees for (.+) category$")
    public void I_hit_categories_api_to_retrieve_all_nominees_for_best_lorem_ipsum_category(String categoryName) throws Throwable {
         Category category = categoryRepository.findByName(categoryName);
         MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/nominees/search?category-id=" + category.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

         ComponentHelper.getMap().put("response", response);
    }

    @And("^I should have (\\d+) nominees for (?:best employee) category in db$")
    public void I_should_have_nominees_for_best_employee_category_in_db(int elements) throws Throwable {
        assertEquals(elements, nomineeRepository.count());
    }

    @When("^I hit categories api to delete the following nominees$")
    public void I_hit_categories_api_to_delete_the_following_nominees(List<Nominee> nominees) throws Throwable {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Nominee> nomineesFromDB = nomineeRepository.findNomineesByName(getNomineesNames(nominees));
        MockHttpServletResponse response = mockMvc.perform(delete("/nominees/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nomineesFromDB)))
                .andReturn()
                .getResponse();

        ComponentHelper.getMap().put("response", response);
    }

    private List<String> getNomineesNames(List<Nominee> nominees) {
        List<String> nomineesName = Lists.newArrayList();
        for(Nominee nominee : nominees){
            nomineesName.add(nominee.getName());
        }
        return nomineesName;
    }
}
