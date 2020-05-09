package com.tushang.study;

import org.junit.jupiter.api.Test;
import java.util.concurrent.*;

class KafkaApplicationTests {

	@Test
	void contextLoads() throws ExecutionException, InterruptedException {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		FutureTask<String> futureTask = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						Thread.sleep(5000L);
						return "hello";
					}
				}
		);
		executorService.submit(futureTask);
		String s = futureTask.get();//同步
		System.out.println(s);
	}

	@Test
    void testAsync() throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		System.out.println(Thread.currentThread().getName());
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
				System.out.println(Thread.currentThread().getName());
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello";
        },executorService);
//		completableFuture.get();//同步
        completableFuture.whenComplete((s, throwable) -> {
			System.out.println(Thread.currentThread().getName());//和上面用同一个线程
            System.out.println(s);
        });//异步
        System.out.println("world");
        Thread.sleep(10000L);
    }

	@Test
	void testCompletableFuture(){
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		CompletableFuture.runAsync(()->{
			System.out.println("hello world");
		}, executorService);
		CompletableFuture.supplyAsync(() -> 1)
		.thenApply(integer -> integer+1)
		.thenApply(integer -> integer*integer)
		.whenComplete((integer, throwable) -> {
			System.out.println(integer);
		});
	}

	@Test
	public void cpuCount(){
		final int cpuCount = Runtime.getRuntime().availableProcessors();
		System.out.println(cpuCount);
	}

}
