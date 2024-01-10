package tw.niq.example.thread;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("ExampleJoinThread")
@Component
public class ExampleJoinThread implements CommandLineRunner {
	
	// 計算階乘 Thread
	private static class FactorialThread extends Thread {

		private long inputNumber;
		private BigInteger result = BigInteger.ZERO;
		private boolean isFinished = false;
		
		public FactorialThread(long inputNumber) {
			this.inputNumber = inputNumber;
		}

		@Override
		public void run() {
			this.result = factorial(inputNumber);
			this.isFinished = true;
		}

		private BigInteger factorial(long n) {

			BigInteger result = BigInteger.ONE;
			
			for (long i = n; i > 0; i--) {
				result = result.multiply(new BigInteger(Long.toString(i)));
			}
			
			return result;
		}

		public BigInteger getResult() {
			return result;
		}

		public boolean isFinished() {
			return isFinished;
		}
	}

	@Override
	public void run(String... args) throws Exception {

		List<Long> inputNumbers = Arrays.asList(10000000000L, 3435L, 35435L, 2324L, 4656L, 23L, 5556L);
		
		List<FactorialThread> threads = new ArrayList<>();
		
		for (long inputNumber : inputNumbers) {
			threads.add(new FactorialThread(inputNumber));
		}
		
		for (Thread thread : threads) {
			thread.setDaemon(true);
			thread.start();
		}
		
		for (Thread thread : threads) {
			// 設定 join(), 只等待 2 秒, 超時的會丟出 InterruptedException, 等待所有 Thread 都完成返回後 main thread 再繼續
			thread.join(2000);
		}
		
		for (int i = 0; i < inputNumbers.size(); i++) {
			FactorialThread factorialThread = threads.get(i);
			if (factorialThread.isFinished()) {
				System.out.println("Factorial of " + inputNumbers.get(i) + " is " + factorialThread.getResult());
			} else {
				System.out.println("Factorial of " + inputNumbers.get(i) + " is still in progress ");
			}
		}
	}

}
