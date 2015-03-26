Feature: As Lorem ipsum user, I want to be able to vote for awards app.
Background:
  Given I have  list of categories in db
      |id   | name              | description                       | imageUrl  |
      |00001| best employee     | some best employee description    | imageUrl1 |
      |00002| best lorem ipsum  | some lorem ipsum description      | imageUrl2 |
    And I have the following nominees for best employee category in db
      |name              |description                              |imageUrl      |
      |lorem ipsum name  |great performance on lorem ipsum project |some image url|
      |john doe          |great performance on ipsum project       |some image url|
      |doe john          |great performance on demo project        |some image url|
      |kupzqf as3gg      |great performance on 76 project          |some image url|
      |ion kaion         |great performance on 123 project         |some image url|

  Scenario: Vote for one category
    Given My userId is LoremIpsum
    When I vote for best employee and I select john doe
    Then I should receive a succesful response
    And I should have 1 vote for john doe in the category best employee

  Scenario: Vote once and then vote for someone else
    Given My userId is LoremIpsum
    When I vote for best employee and I select john doe
    Then I should have 1 vote for john doe in the category best employee

    Given My userId is LoremIpsum
    When I vote for best employee and I select doe john
    Then I should have 0 vote for john doe in the category best employee
    And I should have 1 vote for doe john in the category best employee

    Scenario: Different users votes for the same nominee
      Given My userId is LoremIpsum
      When I vote for best employee and I select john doe
      Then I should have 1 vote for john doe in the category best employee

      Given My userId is IonKaion
      When I vote for best employee and I select john doe
      Then I should have 2 vote for john doe in the category best employee

      Given My userId is IpsumLorem
      When I vote for best employee and I select john doe
      Then I should have 3 vote for john doe in the category best employee

       Given My userId is IpsumLorem
      When I vote for best employee and I select doe john
      Then I should have 2 vote for john doe in the category best employee
      And I should have 1 vote for doe john in the category best employee