package jfasm.generator;

import java.text.MessageFormat;
import java.util.Map;

import static jfasm.generator.GeneratorUtils.firstCharacterToLowerCase;

public class EventListenerGenerator {
	
	private final Map<String, EventListenerConfiguration> eventListeners;
	private String associatedWith;
	private String name;

	public EventListenerGenerator(
			Map<String, EventListenerConfiguration> eventListeners, String associatedWith, String name) {
		this.eventListeners = eventListeners;
		this.associatedWith = associatedWith;
		this.name = name;
	}

	public void generate() {
		final StringBuilder stringBuilder = new StringBuilder();
		for (Map.Entry<String, EventListenerConfiguration> entry : eventListeners.entrySet()) {
			
			final String generatedInterface = entry.getValue().getGeneratedInterface();
			stringBuilder.append(MessageFormat.format("package {0};\n\n", GeneratorUtils.getPackageNameFromFullyQualified(generatedInterface)));
			stringBuilder.append(MessageFormat.format("public interface {0} ", GeneratorUtils.getClassNameFromFullyQualified(generatedInterface)));
			stringBuilder.append("{ \n");
			final Map<String, Parameter> eventsWithParameters = entry.getValue().getEventsWithParameters();
			for (Map.Entry<String, Parameter> eventWithParameter : eventsWithParameters.entrySet()) {
				final Parameter value = eventWithParameter.getValue();
				final String parameterString;
				switch (value) {
				case KEY:
					parameterString = associatedWith + " " + firstCharacterToLowerCase(GeneratorUtils.getClassNameFromFullyQualified(associatedWith));
					break;
				case STATE:
					parameterString = MessageFormat.format("{0}.State {1}State",name,firstCharacterToLowerCase(name));
					break;
				case THIS:
					parameterString = MessageFormat.format("{0} {1}", name, firstCharacterToLowerCase(name));
					break;
				default:
					throw new IllegalStateException("Unrecognised Parameter type:"+value);
				}
				stringBuilder.append(MessageFormat.format("void {0}({1});\n\n", eventWithParameter.getKey(), parameterString));
			}
			stringBuilder.append("}");
			
		}
		System.out.println(stringBuilder.toString());
	}

}
