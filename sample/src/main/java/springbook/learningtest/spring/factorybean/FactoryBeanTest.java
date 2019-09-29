package springbook.learningtest.spring.factorybean;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/springbook/learningtest/spring/factorybean/factorybean.xml")
public class FactoryBeanTest {
	@Autowired
	ApplicationContext context;
	
	@Test
	public void getMessageFromFactoryBean() {
		Object message = context.getBean("message");
		assertThat(message, is(Message.class));		// getBean returns Message not MessageFactoryBean
		assertThat(((Message)message).getText(), is("Factory Bean"));
	}
	
	@Test
	public void getFactoryBean() throws Exception{
		Object factory = context.getBean("&message");	// return factory bean
		assertThat(factory, is(MessageFactoryBean.class));
	}
}
