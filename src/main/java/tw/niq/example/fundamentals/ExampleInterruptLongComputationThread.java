package tw.niq.example.fundamentals;

import java.math.BigInteger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("ExampleInterruptLongComputationThread")
@Component
public class ExampleInterruptLongComputationThread implements CommandLineRunner {
	
	private static class LongComputationThread implements Runnable {

		private BigInteger base;
		private BigInteger power;
		
		public LongComputationThread(BigInteger base, BigInteger power) {
			this.base = base;
			this.power = power;
		}

		@Override
		public void run() {
			System.out.println(base + "^" + power + " = " + pow(base, power));
		}
		
		private BigInteger pow(BigInteger base, BigInteger power) {
			
			BigInteger result = BigInteger.ONE;
			
			for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
				// 在 for-loop 中檢查
				if (Thread.currentThread().isInterrupted()) {
					System.out.println("Prematurely interrupted computation");
					return BigInteger.ZERO;
				}
				result = result.multiply(base);
			}
			return result;
		}
	}

	@Override
	public void run(String... args) throws Exception {
		
		Thread thread = new Thread(new LongComputationThread(new BigInteger("200000"), new BigInteger("1000000000")));
		
		thread.start();
		
		// 使用 interrupt() 中斷耗時 for-loop 的 Thread
		thread.interrupt();
	}

}
