package com.example.resjpademo;


import org.farmtec.res.jpa.repositories.RulesRepository;
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
class RuleTestIT {

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
	void addRules() {
		String request = "{\n" +
				"\"liquid\":\"water\",\n" +
				"\"amount\":2000000\n" +
				"}";
		String ruleStr = "{\n" +
				"   \"name\":\"Rule_2\",\n" +
				"   \"priority\":1,\n" +
				"   \"filter\":\"Grid_A\",\n" +
				"		\"actions\":[" +
				"                {" +
				"                    \"type\":\"SMS\"," +
				"                    \"data\":\"send SMS\"," +
				"                    \"priority\":1" +
				"                }," +
				"                {" +
				"                    \"type\":\"EMAIL\"," +
				"                    \"data\":\"send EMAIL\"," +
				"                    \"priority\":2" +
				"                }" +
				" ],"+
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

		int currentRuleCount = rulesRepository.findAll().size();
		testBody(webTestCli.post().uri(URI_GET_ALL_RULES)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(ruleStr),String.class)
				.exchange().expectStatus().isOk().expectBody());

		assertThat(rulesRepository.findAll().size()).isEqualTo(currentRuleCount + 1);
	}

	@Test
	public void addActionToRule() throws Exception {

		int actionCnt = rulesRepository.findById(1L).get().getActions().size();
		String body ="{\"type\":\"LED\",\"data\":\"blink\",\"priority\":1}";
		testBody(webTestCli.put().uri(URI_GET_ALL_RULES+"/1/action")
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(body),String.class)
				.exchange().expectStatus().isOk().expectBody());
		assertThat(rulesRepository.findById(1L).get().getActions().size()).isEqualTo(actionCnt+1);
	}


	private void testBody(WebTestClient.BodyContentSpec body) {
		String s = new String (body.returnResult().getResponseBody());
		System.out.println(s);
		body.jsonPath("$.id").isNotEmpty()
				.jsonPath("$.name").isNotEmpty()
				.jsonPath("$.priority").exists()
				//.jsonPath("$.filter").exists()
				.jsonPath("$.group.logicalOperation").exists()
				//validate actions
				.jsonPath("actions").exists()
				.jsonPath("actions._embedded.actionList").isArray()
				.jsonPath("actions._embedded.actionList[0].type").isNotEmpty()
				.jsonPath("actions._embedded.actionList[0].data").isNotEmpty()
				.jsonPath("actions._embedded.actionList[0].priority").isNumber()
				.jsonPath("actions._embedded.actionList[0]._links.delete.href").isNotEmpty()
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
