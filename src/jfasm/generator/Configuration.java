package jfasm.generator;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
	
	private Map<String, EventListenerConfiguration> eventListeners = new HashMap<String,EventListenerConfiguration>();
	private Map<String, Property> fields = new HashMap<String, Property>();
	private Map<String, State> states = new HashMap<String, State>();
	private Map<String, EventConfiguration> events = new HashMap<String, EventConfiguration>();
	private Map<String, String> eventsWithTransitions = new HashMap<String, String>();
	
	private String name;
	private String packageName;
	private String associatedWith;
	private String startState;
	private Boolean threadSafe;
	private boolean needsCalendar;
	
	public static class Builder {
		
		private final Configuration result = new Configuration();
		
		private Builder() {
			//prevent construction outside Configuration
		}

		public Builder setName(String name) {
			this.result.name = name;
			return this;
		}

		public Builder setPackageName(String packageName) {
			this.result.packageName = packageName;
			return this;
		}

		public Builder setAssociatedWith(String associatedWith) {
			this.result.associatedWith = associatedWith;
			return this;
		}

		public Builder setStartState(String startState) {
			this.result.startState = startState;
			return this;
		}

		public Builder setThreadSafe(Boolean threadSafe) {
			this.result.threadSafe = threadSafe;
			return this;
		}

		public Builder setNeedsCalendar(boolean needsCalendar) {
			this.result.needsCalendar = needsCalendar;
			return this;
		}

		public Builder setEventListeners(
				Map<String, EventListenerConfiguration> eventListeners) {
			this.result.eventListeners = eventListeners;
			return this;
		}

		public Builder setFields(Map<String, Property> fields) {
			this.result.fields = fields;
			return this;
		}

		public Builder setStates(Map<String, State> states) {
			this.result.states = states;
			return this;
		}

		public Builder setEvents(Map<String, EventConfiguration> events) {
			this.result.events = events;
			return this;
		}

		public Builder setEventsWithTransitions(
				Map<String, String> eventsWithTransitions) {
			this.result.eventsWithTransitions = eventsWithTransitions;
			return this;
		}

		public Configuration build() {
			return this.result;
		}
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}
	
	private Configuration() {
		//prevent direct instantiation
	}

}
