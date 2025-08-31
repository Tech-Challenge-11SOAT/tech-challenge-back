package br.com.postech.techchallange;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TechchallangeApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		// Verifica se o contexto do Spring foi carregado corretamente
		assertThat(applicationContext).isNotNull();
	}

	@Test
	void mainApplicationBeanIsPresent() {
		// Verifica se a classe principal da aplicação está presente no contexto
		assertThat(applicationContext.containsBean("techchallangeApplication")).isTrue();
	}

	@Test
	void applicationContextContainsExpectedBeans() {
		// Verifica se o contexto contém beans essenciais
		String[] beanNames = applicationContext.getBeanDefinitionNames();
		assertThat(beanNames).isNotEmpty();
		assertThat(beanNames.length).isGreaterThan(0);
	}
}
