package springbook.learningtest.template;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CalcSumTest {
	Calculator calculator;
	String numFilepath;
	
	@Before
	public void setUp() {
		this.calculator = new Calculator();
		this.numFilepath = getClass().getResource("numbers.txt").getPath();
	}
	
	@Test
	public void sumOfNumbers() throws IOException{
		assertThat(calculator.calcSum(numFilepath), is(10));
	}
	
	@Test
	public void multiplyOfNumbers() throws IOException{
		assertThat(calculator.calcMultiply(numFilepath), is(24));
	}
	
	@Test
	public void concatenateNumbers() throws IOException{
		assertThat(calculator.concatenate(numFilepath), is("1234"));
	}
}
