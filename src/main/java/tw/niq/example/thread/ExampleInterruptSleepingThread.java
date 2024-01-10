package tw.niq.example.thread;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("ExampleInterruptSleepingThread")
@Component
public class ExampleInterruptSleepingThread implements CommandLineRunner {
	
	private static class BlockingThread implements Runnable {

		@Override
		public void run() {
			try {
				Thread.sleep(500000);
			} catch (InterruptedException e) {
				// 在 try-catch 中檢查
				System.out.println("Exiting blocking thread");
			}
		}
	}

	@Override
	public void run(String... args) throws Exception {
		
		Thread thread = new Thread(new BlockingThread());
		
		thread.start();
		
		// 使用 interrupt() 中斷長時間 sleep 的 Thread
		thread.interrupt();
	}

}
