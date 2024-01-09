package tw.niq.example.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("ExampleGuessingPassword")
@Component
public class ExampleGuessingPassword implements CommandLineRunner {
	
	public static final int MAX_PASSWORD = 9999;
	
	// 金庫
	private static class Vault {
		
		private int password;

		public Vault(int password) {
			this.password = password;
		}
		
		public boolean isCorrectPassword(int guess) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				
			}
			return this.password == guess;
		}
	}

	// 駭客
	private static abstract class Hacker extends Thread {
		
		protected Vault vault;

		public Hacker(Vault vault) {
			this.vault = vault;
			this.setName(this.getClass().getSimpleName());
			this.setPriority(MAX_PRIORITY);
		}

		@Override
		public synchronized void start() {
			System.out.println("In thread: [" + this.getName() + "] - starting");
			super.start();
		}
	}
	
	// 升冪駭客
	private static class AscendingHacker extends Hacker {

		public AscendingHacker(Vault vault) {
			super(vault);
		}

		@Override
		public void run() {
			for (int guess = 0; guess < MAX_PASSWORD; guess++) {
				if (vault.isCorrectPassword(guess)) {
					System.out.println("In thread: [" + this.getName() + "] - bingo: [" + guess + "]");
					System.exit(0);
				}
			}
		}
	}
	
	// 降冪駭客
	private static class DescendingHacker extends Hacker {

		public DescendingHacker(Vault vault) {
			super(vault);
		}

		@Override
		public void run() {
			for (int guess = MAX_PASSWORD; guess >= 0; guess--) {
				if (vault.isCorrectPassword(guess)) {
					System.out.println("In thread: [" + this.getName() + "] - bingo: [" + guess + "]");
					System.exit(0);
				}
			}
		}
	}
	
	// 警察
	private static class Police extends Thread {
		
		public Police() {
			this.setName(this.getClass().getSimpleName());
		}
		
		@Override
		public void run() {
			for (int count = 10; count >= 0; count--) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
				}
				System.out.println("In thread: [" + this.getName() + "] - counting down: [" + count + "]");
			}
			System.out.println("In thread: [" + this.getName() + "] - game over");
			System.exit(0);
		}
	}
	
	@Override
	public void run(String... args) throws Exception {

		Random random = new Random();
		
		Vault vault = new Vault(random.nextInt(MAX_PASSWORD));
		
		List<Thread> threads = new ArrayList<>();
		
		threads.add(new AscendingHacker(vault));
		threads.add(new DescendingHacker(vault));
		threads.add(new Police());
		
		for (Thread thread : threads) {
			thread.start();
		}
	}

}
