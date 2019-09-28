package springbook.learningtest.jdk;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class ReflectionTest {
	@Test
	public void invokeMethod() throws Exception{
		String name = "Spring";
		
		// length()
		assertThat(name.length(), is(6));
		
		Method lengthMethod = String.class.getMethod("length");
		assertThat((Integer)lengthMethod.invoke(name), is(6));
		
		// charAt()
		assertThat(name.charAt(0), is('S'));
		
		Method charAtMethod = String.class.getMethod("charAt",int.class);
		assertThat((Character)charAtMethod.invoke(name,0), is('S'));
	}
	
	@Test
	public void simpleProxy() {
		Hello hello = new HelloTarget();
		assertThat(hello.sayHello("Toby"), is("Hello Toby"));
		assertThat(hello.sayHi("Toby"), is("Hi Toby"));
		assertThat(hello.sayThankYou("Toby"), is("Thank You Toby"));
		
		Hello proxiedHello = new HelloUppercase(hello);
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
		
		Hello proxiedHelloWithHandler = (Hello)Proxy.newProxyInstance(
				getClass().getClassLoader(), new Class[] {Hello.class}, new UppercaseHandler(new HelloTarget()));
		assertThat(proxiedHelloWithHandler.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHelloWithHandler.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHelloWithHandler.sayThankYou("Toby"), is("THANK YOU TOBY"));
	}
}
