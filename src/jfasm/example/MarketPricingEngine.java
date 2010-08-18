package jfasm.example;

import jfasm.example.MarketState.State;

public interface MarketPricingEngine {

	void feedFailure(State closed_for_feed_failure);

	void sendPrice(MarketHashKey key);

}
