package com.example.resjpademo;


import jakarta.transaction.Transactional;
import org.farmtec.res.jpa.repositories.GroupCompositeRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
class GroupCompositeApiIT {

	private final String URI_GET_GROUP = "/groups";

	@Autowired
	GroupCompositeRepository groupCompositeRepository;

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

	@Test
	void displayGetGroupPayload() {
		FluxExchangeResult<String> x = webTestCli.get().uri(URI_GET_GROUP+"/1")
				.exchange().expectStatus().isOk().returnResult(String.class);
		x.consumeWith(r -> r.getResponseBody().subscribe(s -> System.out.println(">>>>"+s)));
	}

	@Test
	@Order(1)
	void getGroupById_1() {
		webTestCli.get().uri(URI_GET_GROUP+"/1")
				.exchange().expectStatus().isOk().expectBody()
				.jsonPath("$.logicalOperation").exists()
				.jsonPath("$.groups._embedded.groupCompositeRepresentationModelList").isArray()
				.jsonPath("$.groups._embedded.groupCompositeRepresentationModelList[0].logicalOperation").exists()
				.jsonPath("$.groups._embedded.groupCompositeRepresentationModelList[0].predicates._embedded" +
						".predicateLeafList").isArray()
				.jsonPath("$.groups._embedded.groupCompositeRepresentationModelList[0].predicates._embedded" +
						".predicateLeafList[0].id").exists()
				.jsonPath("$.groups._embedded.groupCompositeRepresentationModelList[0].predicates._embedded" +
						".predicateLeafList[0].type").exists()
				.jsonPath("$.groups._embedded.groupCompositeRepresentationModelList[0].predicates._embedded" +
						".predicateLeafList[0].operation").exists()
				.jsonPath("$.groups._embedded.groupCompositeRepresentationModelList[0].predicates._embedded" +
						".predicateLeafList[0].tag").exists()
				.jsonPath("$.groups._embedded.groupCompositeRepresentationModelList[0].predicates._embedded" +
						".predicateLeafList[0].value").exists()
				//second predicate of group
				.jsonPath("$.groups._embedded.groupCompositeRepresentationModelList[0].predicates._embedded" +
						".predicateLeafList[1].id").exists()
				.jsonPath("$.groups._embedded.groupCompositeRepresentationModelList[0].predicates._embedded" +
						".predicateLeafList[1].type").exists()
				.jsonPath("$.groups._embedded.groupCompositeRepresentationModelList[0].predicates._embedded" +
						".predicateLeafList[1].operation").exists()
				.jsonPath("$.groups._embedded.groupCompositeRepresentationModelList[0].predicates._embedded" +
						".predicateLeafList[1].tag").exists()
				.jsonPath("$.groups._embedded.groupCompositeRepresentationModelList[0].predicates._embedded" +
						".predicateLeafList[1].value").exists()
				//second group
				.jsonPath("$.groups._embedded.groupCompositeRepresentationModelList[1].logicalOperation").exists()
				.jsonPath("$.groups._embedded.groupCompositeRepresentationModelList[1].predicates._embedded" +
						".predicateLeafList").isArray();

	}

	@Test
	void getGroup_IdNotExists() {
		webTestCli.get().uri(URI_GET_GROUP+"/1000")
				.exchange().expectStatus().isNotFound();
	}

	@Test
	@Order(2)
	@Transactional
	void addPredicateToGroup() {
		String predicate = "{\n" +
				"\"type\":\"string\",\n" +
				"\"operation\":\"CONTAINS\",\n" +
				"\"tag\":\"name\",\n" +
				"\"value\":\"GoodMan\"\n" +
				"}";
		long groupId = 2L;

		displayPayload(webTestCli.post().uri(URI_GET_GROUP+"/"+groupId+"/predicate")
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(predicate),String.class)
				.exchange().expectStatus().isOk().returnResult(String.class));

		assertThat(groupCompositeRepository.findById(2L).get().getPredicateLeaves().size()).isEqualTo(3);
	}

	@Test
	@Order(4)
	@Disabled(value = "to be defined. should be delete groups/{id}/group/{childId}")
	void deleteRule() {
		System.out.println("GROUP DELETE");
		int rulesListSize = groupCompositeRepository.findAll().size();
		webTestCli.delete().uri(URI_GET_GROUP+"/2")
				.exchange().expectStatus().isOk();

		assertThat(groupCompositeRepository.findAll().size()).isEqualTo(rulesListSize -1);
	}

	private void displayPayload(FluxExchangeResult<String> res) {
		res.consumeWith(r -> r.getResponseBody().subscribe(s -> System.out.println(" Payload:[ "+s+" ]")));
	}

	private void testBody(WebTestClient.BodyContentSpec body) {
		body.jsonPath("$.id").isNotEmpty()
				.jsonPath("$.name").isNotEmpty()
				.jsonPath("$.priority").exists()
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
