# rule-evaluator-jpa

[![CircleCI](https://circleci.com/gh/dfpaulino/rule-evaluator-jpa.svg?style=svg)](https://github.com/dfpaulino/rule-evaluator-jpa)

This is a ready to use user-friendly program to evaluate/apply logical expressions to incomming request
The core of thi rule evaluation is [rule-evaluator-service](https://github.com/dfpaulino/ruleEvaluatorService), where the rule evaluation happens.
The **Rule-evaluator-jpa** makes use of the **rule-evaluator-service**, and makes the rule creation easy.

This uses JPA (H2,mysql,postgres) to persist the rules.
Upon start the information in the database is transformed into rules, and loads them into memory (**rule-evaluator-service**).
Once the rules are loaded into memory, no further transactions with the database occurs (unless there is a reload request)

Three main groupsREST API's are provided to allow the user to create/update/delete rules, evaluate rules and reload rules.
Each group of API's are defined in the Wiki page of this project [API spec](https://github.com/dfpaulino/rule-evaluator-jpa/wiki)