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

# Concepts
Its important to understand the concept around this project before attempting to configure any rule
A Rule is an Object that is composed by
1. Name - rule name
2. priority
3. list of Actions (not supported yet)
4. group of rules/expressions - we call this a groupComposite, as this can be composed by other groups and/or predicates

The GroupComposite (group) is an object that is composed by
1. Logical operation
2. List of other groups
3. List of predicates

The predicate is the raw expression  (A>1)