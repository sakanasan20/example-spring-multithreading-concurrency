package tw.niq.example.thread;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"default", "ExampleCreateThread"})
@Component
public class ExampleCreateThread implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		
		// 使用 new 建立 Thread
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// Thread 將會執行的程式
				System.out.println("In thread: [" + Thread.currentThread().getName() + "] - Priority: [" + Thread.currentThread().getPriority() + "]");
				// 丟出 RuntimeException
				throw new RuntimeException("Internal Exception");
			}
		});
		
		// 設定 Thread 的名稱
		thread.setName("New Thread");
		
		// 設定 Thread 的優先序
		thread.setPriority(Thread.MAX_PRIORITY);
		
		// 設定 Thread 的 UncaughtExceptionHandler, 當 Thread 發生的 Exception 沒有其他 Thread 處理時, 會在此捕捉並處理
		thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println("In thread: [" + t.getName() + "] - Exception: [" + e.getMessage() + "]");
			}
		});
		
		System.out.println("In thread: [" + Thread.currentThread().getName() + "] - before thread.start()");
		
		// 使用 start() 啟動 Thread, JVM 此時會建立新的 Thread 並送去給 OS
		thread.start();
		
		System.out.println("In thread: [" + Thread.currentThread().getName() + "] - after thread.start()");
		
		// 讓 OS 暫停排程此 Thread 10 秒
		Thread.sleep(10000);
	}

}
