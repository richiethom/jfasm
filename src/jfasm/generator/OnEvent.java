package jfasm.generator;

public final class OnEvent {
	
	private final String name;
	private final String transitionTo;
	private final Parameter parameter;
	private final String timeStamp;
	private final String event;
	
	public OnEvent(final String name, final String transitionTo, final Parameter parameter, final String timeStamp, final String event) {
		this.name = name;
		this.transitionTo = transitionTo;
		this.parameter = parameter;
		this.timeStamp = timeStamp;
		this.event = event;
	}

	public String getName() {
		return name;
	}

	public String getTransitionTo() {
		return transitionTo;
	}

	public Parameter getParameter() {
		return parameter;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public String getEvent() {
		return event;
	}

}
