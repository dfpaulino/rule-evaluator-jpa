package com.example.resjpademo;


import org.farmtec.res.jpa.repositories.RulesRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
class ResJpaDemoApplicationTests {

	private final String URI_GET_ALL_RULES = "/rules";

	@Autowired
	RulesRepository rulesRepository;

	@LocalServerPort
	int localServerPort;

	@Autowired
	private WebTestClient webTestCli;

	@Test
	void contextLoads() {
	}
/*
"_embedded":{"simpleRuleDtoList":[{"id":1,"name":"Rule_1","priority":1,"actions":null,"_links":{"self":{"href":"http://localhost:33893/rules/1"}}}]}}

 */

	void displayGetRulesPayload() {
		FluxExchangeResult<String> x = webTestCli.get().uri(URI_GET_ALL_RULES)
				.exchange().expectStatus().isOk().returnResult(String.class);
		x.consumeWith(r -> r.getResponseBody().subscribe(s -> System.out.println(">>>>"+s)));
	}

	@Test
	@Order(1)
	void getRules_1() {
		webTestCli.get().uri(URI_GET_ALL_RULES)
				.exchange().expectStatus().isOk().expectBody()
				.jsonPath("$._embedded.simpleRuleDtoList").isArray()
				.jsonPath("$._embedded.simpleRuleDtoList[0].id").isNumber()
				.jsonPath("$._embedded.simpleRuleDtoList[1]").doesNotExist();
	}

	@Test
	void getRules_IdNotExists() {
		webTestCli.get().uri(URI_GET_ALL_RULES+"/1000")
				.exchange().expectStatus().isNotFound();
	}

	@Test
	@Order(2)
	void getRulesById() {
		testBody(webTestCli.get().uri(URI_GET_ALL_RULES+"/1")
				.exchange().expectStatus().isOk()
				.expectBody());
	}

	@Test
	@Order(3)
	void addRules() {
		String ruleStr = "{\n" +
				"   \"name\":\"Rule_2\",\n" +
				"   \"priority\":1,\n" +
				"\"actions\": [\n"
				+ "            {\n"
				+ "                \"type\": \"SMS\",\n"
				+ "                \"data\": \"send SMS\",\n"
				+ "                \"priority\": 1\n"
				+ "            },\n"
				+ "            {\n"
				+ "                \"type\": \"EMAIL\",\n"
				+ "                \"data\": \"send EMAIL\",\n"
				+ "                \"priority\": 2\n"
				+ "            }\n"
				+ "        ],"
				+
				"   \"groupComposite\":{\n" +
				"      \"logicalOperation\":\"OR\",\n" +
				"      \"groupComposites\":[\n" +
				"         {\n" +
				"            \"logicalOperation\":\"AND\",\n" +
				"            \"predicateLeaves\":[\n" +
				"               {\n" +
				"                  \"type\":\"string\",\n" +
				"                  \"operation\":\"EQ\",\n" +
				"                  \"tag\":\"name\",\n" +
				"                  \"value\":\"Walter White\"\n" +
				"               },\n" +
				"               {\n" +
				"                  \"type\":\"long\",\n" +
				"                  \"operation\":\"GTE\",\n" +
				"                  \"tag\":\"tag2\",\n" +
				"                  \"value\":\"50\"\n" +
				"               }\n" +
				"            ]\n" +
				"         },\n" +
				"         {\n" +
				"            \"logicalOperation\":\"AND\",\n" +
				"            \"predicateLeaves\":[\n" +
				"               {\n" +
				"                  \"type\":\"string\",\n" +
				"                  \"operation\":\"CONTAINS\",\n" +
				"                  \"tag\":\"name\",\n" +
				"                  \"value\":\"GoodMan\"\n" +
				"               },\n" +
				"               {\n" +
				"                  \"type\":\"integer\",\n" +
				"                  \"operation\":\"LTE\",\n" +
				"                  \"tag\":\"age\",\n" +
				"                  \"value\":\"50\"\n" +
				"               }\n" +
				"            ]\n" +
				"         }\n" +
				"      ]\n" +
				"    }\n" +
				"}";
		int currentRuleSize = rulesRepository.findAll().size();
		testBody(webTestCli.post().uri(URI_GET_ALL_RULES)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(ruleStr),String.class)
				.exchange().expectStatus().isOk().expectBody());
		assertThat(rulesRepository.findAll().size()).isEqualTo(currentRuleSize + 1);
	}

	@Test
	@Order(4)
	void deleteRule() {
		int rulesListSize = rulesRepository.findAll().size();
		webTestCli.delete().uri(URI_GET_ALL_RULES+"/1")
				.exchange().expectStatus().isOk();
		assertThat(rulesRepository.findAll().size()).isEqualTo(rulesListSize -1);
	}

	@Test

	void addRules_invalidType() {
		String ruleStr = "{\n" +
				"   \"name\":\"Rule_2\",\n" +
				"   \"priority\":1,\n" +
				"   \"groupComposite\":{\n" +
				"      \"logicalOperation\":\"OR\",\n" +
				"      \"groupComposites\":[\n" +
				"         {\n" +
				"            \"logicalOperation\":\"AND\",\n" +
				"            \"predicateLeaves\":[\n" +
				"               {\n" +
				"                  \"type\":\"strong\",\n" +
				"                  \"operation\":\"EQ\",\n" +
				"                  \"tag\":\"name\",\n" +
				"                  \"value\":\"Walter White\"\n" +
				"               },\n" +
				"               {\n" +
				"                  \"type\":\"long\",\n" +
				"                  \"operation\":\"GTE\",\n" +
				"                  \"tag\":\"tag2\",\n" +
				"                  \"value\":\"50\"\n" +
				"               }\n" +
				"            ]\n" +
				"         },\n" +
				"         {\n" +
				"            \"logicalOperation\":\"AND\",\n" +
				"            \"predicateLeaves\":[\n" +
				"               {\n" +
				"                  \"type\":\"string\",\n" +
				"                  \"operation\":\"CONTAINS\",\n" +
				"                  \"tag\":\"name\",\n" +
				"                  \"value\":\"GoodMan\"\n" +
				"               },\n" +
				"               {\n" +
				"                  \"type\":\"integer\",\n" +
				"                  \"operation\":\"LTE\",\n" +
				"                  \"tag\":\"age\",\n" +
				"                  \"value\":\"50\"\n" +
				"               }\n" +
				"            ]\n" +
				"         }\n" +
				"      ]\n" +
				"    }\n" +
				"}";

		webTestCli.post().uri(URI_GET_ALL_RULES)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(ruleStr),String.class)
				.exchange().expectStatus().isBadRequest();
	}

	private void testBody(WebTestClient.BodyContentSpec body) {
		body.jsonPath("$.id").isNotEmpty()
				.jsonPath("$.name").isNotEmpty()
				.jsonPath("$.priority").exists()
				.jsonPath("$.actions").exists()
				.jsonPath("$.actions._embedded.actionList").isArray()
				.jsonPath("$.actions._embedded.actionList[0].type").isNotEmpty()
				.jsonPath("$.actions._embedded.actionList[0].data").isNotEmpty()
				.jsonPath("$.actions._embedded.actionList[0].priority").isNumber()
				.jsonPath("$.actions._embedded.actionList[1].type").isNotEmpty()
				.jsonPath("$.actions._embedded.actionList[1].data").isNotEmpty()
				.jsonPath("$.actions._embedded.actionList[1].priority").isNumber()
				.jsonPath("$.group.logicalOperation").exists()
				//validate groups
				.jsonPath("$.group.groups._embedded.groupCompositeRepresentationModelList").isArray()
				//validate group
				.jsonPath("$.group.groups._embedded.groupCompositeRepresentationModelList[0].logicalOperation")
				.exists()
				.jsonPath("$.group.groups._embedded.groupCompositeRepresentationModelList[0]").exists()
				//validate predicate
				.jsonPath("$.group.groups._embedded.groupCompositeRepresentationModelList[0]" +
						".predicates._embedded.predicateLeafList").isArray()
				.jsonPath("$.group.groups.._embedded.groupCompositeRepresentationModelList[0]" +
						".predicates._embedded.predicateLeafList[0]").exists()
				.jsonPath("$.group.groups._embedded.groupCompositeRepresentationModelList[0]" +
						".predicates._embedded.predicateLeafList[0].id").exists()
				.jsonPath("$.group.groups.._embedded.groupCompositeRepresentationModelList[0]" +
						".predicates._embedded.predicateLeafList[0].type").isNotEmpty()
				.jsonPath("$.group.groups.._embedded.groupCompositeRepresentationModelList[0]" +
						".predicates._embedded.predicateLeafList[0].operation").isNotEmpty()
				.jsonPath("$.group.groups.._embedded.groupCompositeRepresentationModelList[0]" +
						".predicates._embedded.predicateLeafList[0].tag").isNotEmpty()
				.jsonPath("$.group.groups.._embedded.groupCompositeRepresentationModelList[0]" +
						".predicates._embedded.predicateLeafList[0].value").isNotEmpty()

				//the other predicate
				.jsonPath("$.group.groups.._embedded.groupCompositeRepresentationModelList[0]" +
						".predicates._embedded.predicateLeafList[1]").exists()
				//the other group
				.jsonPath("$.group.groups.._embedded.groupCompositeRepresentationModelList[1]").exists()
				.jsonPath("$.group.groups.._embedded.groupCompositeRepresentationModelList[1]").exists()
				.jsonPath("$.group.groups.._embedded.groupCompositeRepresentationModelList[1]" +
						".predicates._embedded.predicateLeafList").isArray()
				.jsonPath("$.group.groups.._embedded.groupCompositeRepresentationModelList[1]" +
						".predicates._embedded.predicateLeafList[0]").exists()
				.jsonPath("$.group.groups.._embedded.groupCompositeRepresentationModelList[1]" +
						".predicates._embedded.predicateLeafList[0].id").exists()
				.jsonPath("$.group.groups.._embedded.groupCompositeRepresentationModelList[1]" +
						".predicates._embedded.predicateLeafList[1]").exists();
	}
}
