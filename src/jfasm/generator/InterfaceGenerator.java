package jfasm.generator;

import java.text.MessageFormat;
import java.util.Map;

public class InterfaceGenerator extends AbstractGenerator {
	


	public InterfaceGenerator(final String name, final Map<String, State> states, final Map<String, EventConfiguration> events, final String packageName) {
		super(name, states, events, packageName);
	}
	
	public InterfaceGenerator(Configuration configuration) {
		super(configuration);
	}

	public void generate() {
		final StringBuilder stringBuilder = new StringBuilder();
		generatePackage(stringBuilder);		
		stringBuilder.append(MessageFormat.format("public interface {0}", name));
		stringBuilder.append("{\n\n");
		stringBuilder.append("public enum State {");
		final int stateCount = states.size();
		int stateIndex = 0;
		for (Map.Entry<String, State> entry : states.entrySet()) {
			++stateIndex;
			stringBuilder.append(entry.getKey());
			if (stateIndex<stateCount) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append(";}\n\n");
		stringBuilder.append("State getState();\n\n");
		for (final Map.Entry<String, EventConfiguration> eventEntry : events.entrySet()) {
			final String eventName = eventEntry.getKey();
			final EventConfiguration eventConfiguration = eventEntry.getValue();
			
			stringBuilder.append(MessageFormat.format("void {0}(", eventName));
			final int parameterCount = eventConfiguration.getParameters().size();
			int currentParameter = 0;
			for (Map.Entry<String, String> parameterEntry : eventConfiguration.getParameters().entrySet()) {
				++currentParameter;
				stringBuilder.append(MessageFormat.format("{1} {0}", parameterEntry.getKey(), parameterEntry.getValue()));
				if (currentParameter<parameterCount) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append(");\n\n");
		}
		stringBuilder.append("}");
		System.out.println(stringBuilder.toString());
	}
	
	

}
