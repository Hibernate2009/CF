package ru.vtb.uip.jms.jmsmessaging.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class CF {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("Первая фича");
                Thread.sleep(1000);
                System.out.println("Первая фича отработала");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result 1";
        });
        CompletableFuture<String> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("Вторая фича");
                Thread.sleep(2000);
                System.out.println("Вторая фича отработала");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result 2";
        });
        List<CompletableFuture<String>> completableFutures = new ArrayList<>();
        completableFutures.add(completableFuture);
        completableFutures.add(completableFuture2);

        //CompletableFuture<String> stringCompletableFuture = anyOf(completableFutures);
        //String s = stringCompletableFuture.get();
        //System.out.println(s);
        CompletableFuture<List<String>> listCompletableFuture = allOf(completableFutures);
        List<String> strings = listCompletableFuture.get();
        System.out.println(strings);

    }

    public static <T> CompletableFuture<T> anyOf(List<CompletableFuture<T>> cfs) {
        return CompletableFuture.anyOf(cfs.toArray(new CompletableFuture[0]))
                .thenApply(o -> (T) o);
    }

    public static <T> CompletableFuture<List<T>> allOf(Collection<CompletableFuture<T>> futures) {
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(__ -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }
}
