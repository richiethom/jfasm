package jfasm.generator;

import java.util.HashMap;
import java.util.Map;


public class EventListenerConfiguration {
	
	private final String generatedInterface;
	private final Map<String, Parameter> eventWithParameter = new HashMap<String, Parameter>();

	public EventListenerConfiguration(String generatedInterface) {
		this.generatedInterface = generatedInterface;
	}
	
	public String getGeneratedInterface() {
		return this.generatedInterface;
	}
	
	public void addEventWithParameter(final String eventName, final Parameter parameter) {
		eventWithParameter.put(eventName, parameter);
	}
	
	public Map<String, Parameter> getEventsWithParameters() {
		return eventWithParameter;
	}
	
}
