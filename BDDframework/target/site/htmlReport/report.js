$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("login.feature");
formatter.feature({
  "line": 2,
  "name": "Home Functionality",
  "description": "",
  "id": "home-functionality",
  "keyword": "Feature"
});
formatter.before({
  "duration": 3157757900,
  "status": "passed"
});
formatter.scenario({
  "line": 5,
  "name": "Validation of Car Search Functionality",
  "description": "",
  "id": "home-functionality;validation-of-car-search-functionality",
  "type": "scenario",
  "keyword": "Scenario",
  "tags": [
    {
      "line": 4,
      "name": "@regression"
    }
  ]
});
formatter.step({
  "line": 7,
  "name": "I am on the Homepage of carsguide website",
  "keyword": "Given "
});
formatter.step({
  "comments": [
    {
      "line": 8,
      "value": "#When I move to buy search cars"
    }
  ],
  "line": 9,
  "name": "I click on Search Cars",
  "keyword": "And "
});
formatter.match({
  "location": "CarsearchSteps.i_am_on_the_Homepage_of_carsguide_website()"
});
formatter.result({
  "duration": 7076716700,
  "status": "passed"
});
formatter.match({
  "location": "CarsearchSteps.i_click_on_Search_Cars()"
});
formatter.result({
  "duration": 5450305300,
  "status": "passed"
});
formatter.after({
  "duration": 912077900,
  "status": "passed"
});
});