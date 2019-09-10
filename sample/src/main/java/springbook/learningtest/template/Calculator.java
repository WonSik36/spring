package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
	public Integer calcSum(String filepath)throws IOException{
		BufferedReaderCallback sumCallBack = new BufferedReaderCallback(){
			public Integer doSomethingWithReader(BufferedReader br) throws IOException{
				Integer sum = 0;
				String line = null;
				while((line = br.readLine())!=null) {
					sum+=Integer.valueOf(line);
				}
				return sum;
			}
		};
		return fileReadTemplate(filepath, sumCallBack);
	}
	
	public Integer calcMultiply(String filepath)throws IOException{
		BufferedReaderCallback multiplyCallBack = new BufferedReaderCallback(){
			public Integer doSomethingWithReader(BufferedReader br) throws IOException{
				Integer multiply = 1;
				String line = null;
				while((line = br.readLine())!=null) {
					multiply*=Integer.valueOf(line);
				}
				return multiply;
			}
		};
		return fileReadTemplate(filepath, multiplyCallBack);
	}
	
	public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException{
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filepath));
			int ret = callback.doSomethingWithReader(br);
			return ret;
		}catch(IOException e) {
			System.out.println(e.getMessage());
			throw e;
		}finally {
			if(br != null) {
				try {br.close();}
				catch(IOException e) {System.out.println(e.getMessage());}
			}	
		}
	}
}
