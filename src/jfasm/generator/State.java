package jfasm.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class State {
	
	private final String name;
	private final List<OnEnter> onEnterEvents = new ArrayList<OnEnter>();
	private final List<OnEvent> onEvents = new ArrayList<OnEvent>();
	
	public State(final String name) {
		this.name = name;
	}
	
	public void addOnEnterEvent(final OnEnter onEnter) {
		this.onEnterEvents.add(onEnter);
	}
	
	public void addOnEventTransition(final OnEvent onEvent) {
		this.onEvents.add(onEvent);
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<OnEnter> getOnEnterEvents() {
		return Collections.unmodifiableList(onEnterEvents);
	}
	
	public List<OnEvent> getOnEventTransitions() {
		return Collections.unmodifiableList(onEvents);
	}

}
