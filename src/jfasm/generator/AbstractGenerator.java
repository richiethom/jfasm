package jfasm.generator;

import java.text.MessageFormat;
import java.util.Map;

public abstract class AbstractGenerator {
	
	protected final String name;
	protected final Map<String, State> states;
	protected final Map<String, EventConfiguration> events;
	protected final String packageName;
	
	protected AbstractGenerator(final String name, final Map<String, State> states, final Map<String, EventConfiguration> events, String packageName) {
		this.name = name;
		this.states = states;
		this.events = events;
		this.packageName = packageName;
	}
	
	public AbstractGenerator(Configuration configuration) {
		this(configuration.)
	}

	protected void generatePackage(final StringBuilder stringBuilder) {
		stringBuilder.append(MessageFormat.format("package {0};\n\n", packageName));
	}
	
	protected abstract void generate();

}
