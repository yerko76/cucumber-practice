package component.vote;

import com.awards.model.Category;
import com.awards.model.Nominee;
import com.awards.model.Vote;
import com.awards.repository.CategoryRepository;
import com.awards.repository.NomineeRepository;
import com.awards.repository.VoteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import component.configuration.ComponentHelper;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebAppConfiguration
@ContextConfiguration("classpath:cucumber.xml")
public class VoteSteps {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NomineeRepository nomineeRepository;

    @Autowired
    private VoteRepository voteRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mongoTemplate.getDb().dropDatabase();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @When("^I vote for (.*) and I select (.*)$")
    public void When_I_vote_for_the_best_employee_and_I_select_john_doe(String categoryName, String nomineeName) throws Throwable {
        MockHttpServletResponse response = mockMvc.perform(post("/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getVoteRequest(categoryName, nomineeName)))
                .andReturn()
                .getResponse();
        ComponentHelper.getMap().remove("userId");
        ComponentHelper.getMap().put("response", response);
    }

    private String getVoteRequest(String categoryName, String nomineeName) throws JsonProcessingException {
        Category category = categoryRepository.findByName(categoryName);
        Nominee nominee = nomineeRepository.findNomineeByName(nomineeName);
        ObjectMapper objectMapper = new ObjectMapper();
        return  objectMapper.writeValueAsString(new Vote((String) ComponentHelper.getMap().get("userId"),
                category.getId(), nominee.getId()));
    }

    @And("^I should have (\\d+) vote for (.*) in the category (.*)$")
    public void I_should_have_vote_for_john_doe_in_the_category_best_employee(int expectedVotes, String nomineeName, String categoryName) throws Throwable {
        Category category = categoryRepository.findByName(categoryName);
        Nominee nominee = nomineeRepository.findNomineeByName(nomineeName);
        int totalVotes = voteRepository.getTotalVotesByCategoryAndNominee(category.getId(), nominee.getId());
        assertEquals(expectedVotes, totalVotes);
    }

    @And("^My userId is (\\w+)$")
    public void My_userId_is(String userId) throws Throwable {
        ComponentHelper.getMap().put("userId", userId);
    }
}