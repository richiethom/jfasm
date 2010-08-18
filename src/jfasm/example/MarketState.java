package jfasm.example;


public interface MarketState {
	
	public enum State {
		CLOSED_FOR_FEED_FAILURE, OPEN, AFTER_LAST_TRADE, EXPIRED;
	}

	State getState();

	void feedFailure();

	void priceReceived(final Price price);

	void expiryTimePassed();

	void lastTradeTimePassed();

}
