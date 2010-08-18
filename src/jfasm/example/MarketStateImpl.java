package jfasm.example;

import java.util.Calendar;
import java.util.Date;

final class MarketStateImpl implements MarketState {
	
	private final MarketHashKey key;
	private final MarketPricingEngine marketPricingEngine;
	
	private final MarketState_CLOSED_FOR_FEED_FAILURE marketState_CLOSED_FOR_FEED_FAILURE = new MarketState_CLOSED_FOR_FEED_FAILURE();
	private final MarketState_OPEN marketState_OPEN = new MarketState_OPEN();
	private final MarketState_AFTER_LAST_TRADE marketState_AFTER_LAST_TRADE = new MarketState_AFTER_LAST_TRADE();
	private final MarketState_EXPIRED marketState_EXPIRED = new MarketState_EXPIRED();
	
	private MarketState marketStateInternal = marketState_OPEN;
	
	private String marketName;
	private Date priceLastReceived;

	public MarketStateImpl(final MarketHashKey key, final MarketPricingEngine marketPricingEngine) {
		this.key = key;
		this.marketPricingEngine = marketPricingEngine;
	}
	
	public State getState() {
		return marketStateInternal.getState();
	}
	
	public String getMarketName() {
		return marketName;
	}
	
	public Date getPriceLastReceived() {
		return priceLastReceived;
	}
	
	public void feedFailure() {
		marketStateInternal.feedFailure();
	}
	
	
	
	private abstract class AbstractMarketState implements MarketState {
		
		public void feedFailure() {
			throw new UnsupportedOperationException();
		}
		
		public void priceReceived(final Price price) {
			throw new UnsupportedOperationException();
		}
		
		public void expiryTimePassed() {
			throw new UnsupportedOperationException();
		}
		
		public final void lastTradeTimePassed() {
			MarketStateImpl.this.marketStateInternal=marketState_AFTER_LAST_TRADE;
		}
		
		public abstract State getState();
		
	}
	
	private final class MarketState_AFTER_LAST_TRADE extends AbstractMarketState {
		
		@Override
		public State getState() {
			return State.AFTER_LAST_TRADE;
		}
		
		@Override
		public void expiryTimePassed() {
			MarketStateImpl.this.marketStateInternal = marketState_EXPIRED;
		}
		
	}
	
	private class MarketState_EXPIRED extends AbstractMarketState {
		
		@Override
		public State getState() {
			return State.EXPIRED;
		}
		
	}
	
	private class MarketState_CLOSED_FOR_FEED_FAILURE extends AbstractMarketState {
		
		public void onEnter() {
			marketPricingEngine.feedFailure(State.CLOSED_FOR_FEED_FAILURE);
		}
		
		@Override
		public void priceReceived(Price arg0) {
			MarketStateImpl.this.marketStateInternal=MarketStateImpl.this.marketState_OPEN;
		}
		
		@Override
		public State getState() {
			return State.CLOSED_FOR_FEED_FAILURE;
		}
	}
	
	private class MarketState_OPEN extends AbstractMarketState {
		
		@Override
		public void feedFailure() {
			MarketStateImpl.this.marketStateInternal = MarketStateImpl.this.marketState_CLOSED_FOR_FEED_FAILURE;
		}
		
		@Override
		public void priceReceived(Price arg0) {
			marketPricingEngine.sendPrice(key);
			priceLastReceived = Calendar.getInstance().getTime();
		}

		@Override
		public State getState() {
			return State.OPEN;
		}
	}

	@Override
	public void expiryTimePassed() {
		marketStateInternal.expiryTimePassed();
		
	}

	@Override
	public void lastTradeTimePassed() {
		marketStateInternal.lastTradeTimePassed();
	}

	@Override
	public void priceReceived(Price price) {
		marketStateInternal.priceReceived(price);
	}
	
}
