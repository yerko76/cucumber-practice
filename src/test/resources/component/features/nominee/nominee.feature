Feature: As a lorem ipsum user, I want to be able to create, delete and update nominees inside existing categories

  Scenario: Add nominees to existing category
    Given I have  list of categories in db
      |id   | name              | description                       | imageUrl  |
      |00001| best employee     | some best employee description    | imageUrl1 |
      |00002| best lorem ipsum  | some lorem ipsum description      | imageUrl2 |

    And I have the following nominees for best employee category
      |name              |description                              |imageUrl      |
      |lorem ipsum name  |great performance on lorem ipsum project |some image url|
      |john doe          |great performance on ipsum project       |some image url|
      |doe john          |great performance on demo project        |some image url|
      |kupzqf as3gg      |great performance on 76 project          |some image url|
      |ion kaion         |great performance on 123 project         |some image url|

    When I hit categories api to save these nominees
    Then I should receive a create response with the following message: Nominees created
    And I should have 5 nominees for best employee category in db

  Scenario: Return all nominees for specific category
    Given I have  list of categories in db
      |id   | name              | description                       | imageUrl  |
      |00001| best employee     | some best employee description    | imageUrl1 |
      |00002| best lorem ipsum  | some lorem ipsum description      | imageUrl2 |

    And I have the following nominees for best lorem ipsum category in db
      |name              |description                              |imageUrl      |
      |lorem ipsum name  |great performance on lorem ipsum project |some image url|
      |john doe          |great performance on ipsum project       |some image url|

    When I hit categories api to retrieve all nominees for best lorem ipsum category
    Then I should receive a succesful response
    And I should have 2 nominees for best lorem ipsum category
    
    Scenario: Delete nominees for existing category
     Given I have  list of categories in db
      |id   | name              | description                       | imageUrl  |
      |00001| best employee     | some best employee description    | imageUrl1 |
      |00002| best lorem ipsum  | some lorem ipsum description      | imageUrl2 |

     And I have the following nominees for best lorem ipsum category in db
      |name              |description                              |imageUrl      |
      |lorem ipsum name  |great performance on lorem ipsum project |some image url|
      |john doe          |great performance on ipsum project       |some image url|
      |doe john          |great performance on demo project        |some image url|
      |kupzqf as3gg      |great performance on 76 project          |some image url|
      |ion kaion         |great performance on 123 project         |some image url|

    When I hit categories api to delete the following nominees
      |name              |description                              |imageUrl      |
      |lorem ipsum name  |great performance on lorem ipsum project |some image url|
      |john doe          |great performance on ipsum project       |some image url|
    Then I should receive a succesful response
    And I should have 3 nominees for best employee category in db