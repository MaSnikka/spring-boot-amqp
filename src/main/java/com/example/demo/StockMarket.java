package com.example.demo;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@EnableScheduling
public class StockMarket {

    static List<String> STOCK_SYMBOLS = Arrays.asList("ATT", "SBUX", "ZOOM");
    static Random RAND = new Random();

    // This is the Amqp template
    AmqpTemplate amqpTemplate;

    Map<String, StockMovement> lastTrade;

    public StockMarket(AmqpTemplate amqpTemplate){
        this.amqpTemplate = amqpTemplate;
        this.lastTrade = new HashMap<>();
        STOCK_SYMBOLS.forEach(symbol -> {
            StockMovement initialTrade = new StockMovement(symbol, RAND.nextFloat() * 100.0F);
            this.lastTrade.put(symbol, initialTrade);
        });
    }

    static String randomStockSymbol(){
        return STOCK_SYMBOLS.get(RAND.nextInt(STOCK_SYMBOLS.size()));
    }

    static float newPrice(float oldPrize){
        return oldPrize * (RAND.nextInt() + 0.5F);
    }

    @Scheduled(fixedRate = 500L) // ms
    void marketMovement() {
        String nextStockSymbol = randomStockSymbol();
        StockMovement lastTrade = this.lastTrade.get(nextStockSymbol);
        float newPrice = newPrice(lastTrade.getPrice());
        lastTrade.setPrice(newPrice);

        // Publish every stock-market action to the broker
        // Here it gets published to an exchange of 'stock-market'
        amqpTemplate.convertAndSend("stock-market", lastTrade.getStockName(), lastTrade);
    }
}
