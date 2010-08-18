package marketpricing;

public interface MarketState {

	public enum State {
		OPEN, EXPIRED, AFTER_LAST_TRADE, CLOSED_FOR_FEED_FAILURE;
	}

	State getState();

	void feedFailure();

	void expiryTimePassed();

	void priceReceived(marketpricing.Price price);

	void lastTradeTimePassed();

}