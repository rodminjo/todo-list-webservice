package todo.list_service.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Test
    void 메인페이지_로딩(){
        //when
        String body = testRestTemplate.getForObject("/", String.class);

        //then
        Assertions.assertThat(body).contains("웹서비스");
    }
}