package tw.niq.example.fundamentals;

import java.math.BigInteger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("ExampleInterruptDaemonThread")
@Component
public class ExampleInterruptDaemonThread implements CommandLineRunner {
	
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
				result = result.multiply(base);
			}
			return result;
		}
	}

	@Override
	public void run(String... args) throws Exception {
		
		Thread thread = new Thread(new LongComputationThread(new BigInteger("200000"), new BigInteger("1000000000")));
		
		// 設定為 Daemon
		thread.setDaemon(true);
		
		thread.start();
		
		// 使用 interrupt() 中斷耗時 for-loop 的 Daemon Thread
		thread.interrupt();
	}

}
