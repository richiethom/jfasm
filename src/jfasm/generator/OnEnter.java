package jfasm.generator;

public final class OnEnter {
	
	private final String eventName;
	private final Parameter parameter;
	
	public OnEnter(final String eventName, final Parameter parameter) {
		this.eventName = eventName;
		this.parameter = parameter;
	}
	
	public String getEventName() {
		return this.eventName;
	}
	
	public Parameter getParameter() {
		return this.parameter;
	}
}
