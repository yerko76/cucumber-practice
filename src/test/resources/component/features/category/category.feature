Feature: As lorem ipsum user, I want to be able to manage all categories for awards app!

  
  Scenario: Retrieve a list of existing categories
    Given I have  list of categories in db
      |id   | name              | description                       | imageUrl  |
      |00001| best employee     | some best employee description    | imageUrl1 |
      |00002| best lorem ipsum  | some lorem ipsum description      | imageUrl2 |
    When I hit the categories api
    Then I should receive a succesful response
    And the response is a list containing 2 categories
    And I should see categories with the following attributes
      |id   | name              | description                       | imageUrl  |
      |00001| best employee     | some best employee description    | imageUrl1 |
      |00002| best lorem ipsum  | some lorem ipsum description      | imageUrl2 |

  
  Scenario: Save categories into db
    Given I have a request to save the following categories
    """
    [
      {"id":"00001","name": "best employee", "description": "best lorem ipsum employee","imageUrl":"best employee url"},
      {"id":"00002","name": "best lorem ipsum", "description": "best lorem ipsum description","imageUrl":"some image url"}
    ]
    """
    When I hit categories api to save this categories
    Then I should receive a create response with the following message: Categories created
    And I should have 2 categories into db

  
  Scenario: find category by Id
    Given I have  list of categories in db
      |id   | name              | description                       | imageUrl  |
      |00001| best employee     | some best employee description    | imageUrl1 |
      |00002| best lorem ipsum  | some lorem ipsum description      | imageUrl2 |

    When I hit categories api passing id: 00001
    Then I should receive a succesful response
    And I should see one category with the following attributes
      |id   | name              | description                       | imageUrl  |
      |00001| best employee     | some best employee description    | imageUrl1 |

  
  Scenario: delete category by id
    Given I have  list of categories in db
      |id   | name              | description                       | imageUrl  |
      |00001| best employee     | some best employee description    | imageUrl1 |
      |00002| best lorem ipsum  | some lorem ipsum description      | imageUrl2 |

    When I hit categories api to delete one category using id: 00002
    Then I should receive a succesful response
    And I should have 1 category in db

  
  Scenario: Update existing categories
    Given I have  list of categories in db
      |id   | name              | description                       | imageUrl  |
      |00001| best employee     | some best employee description    | imageUrl1 |
      |00002| best lorem ipsum  | some lorem ipsum description      | imageUrl2 |
      |00003| some lorem ipsum  | some nothing to do here           | imageUrl3 |

    And I have a request to update the following categories
    """
    [
      {"id":"00001","name": "best employee", "description": "new description","imageUrl":"best employee url"},
      {"id":"00002","name": "best ipsum lorem", "description": "new lorem ipsum description","imageUrl":"some image url"}
    ]
    """

    When I hit categories api to update this categories
    Then I should receive a succesful response
    And I should see updated categories with the following attributes
      |id   | name              | description                 | imageUrl          |
      |00001| best employee     | new description             | best employee url |
      |00002| best ipsum lorem  | new lorem ipsum description | some image url    |
