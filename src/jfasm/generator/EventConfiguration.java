package jfasm.generator;

import java.util.HashMap;
import java.util.Map;

public class EventConfiguration {
	
	private String globalTransition;
	private final Map<String, String> globalParameters;
	private Map<String, Object> stateSpecificConfiguration = new HashMap<String, Object>();
	
	public EventConfiguration(final Map<String, String> globalParameters) {
		this(null, globalParameters);
	}
	
	public EventConfiguration(final String globalTransition, final Map<String, String> globalParameters) {
		this.globalTransition = globalTransition;
		this.globalParameters = new HashMap<String, String>(globalParameters);
	}
	
	public void setGlobalTransition(final String globalTransition) {
		this.globalTransition = globalTransition;
	}
	
	public void setConfigurationForState(final String stateName, Object configuration) {
		stateSpecificConfiguration.put(stateName, configuration);
	}
	
	public boolean hasGlobalTransition() {
		return globalTransition!=null;
	}
	
	public String getGlobalTransition() {
		return this.globalTransition;
	}

	public Map<String, String> getParameters() {
		return globalParameters;
	}
	
	public boolean hasParameters() {
		return globalParameters.size()>0;
	}

}
