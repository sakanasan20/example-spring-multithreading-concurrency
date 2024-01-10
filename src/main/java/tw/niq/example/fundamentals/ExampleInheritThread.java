package tw.niq.example.fundamentals;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("ExampleInheritThread")
@Component
public class ExampleInheritThread implements CommandLineRunner {
	
	// 客製 Thread
	private static class CustomThread extends Thread {
		
		@Override
		public void run() {
			System.out.println("In custom thread: [" + this.getName() + "]");
		}
	}

	@Override
	public void run(String... args) throws Exception {
		
		// 使用 new 建立客製 Thread
		Thread customThread = new CustomThread();
		
		customThread.start();
	}

}
