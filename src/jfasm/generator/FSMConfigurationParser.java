package jfasm.generator;

import static jfasm.generator.GeneratorUtils.findItemByName;
import static jfasm.generator.GeneratorUtils.getChildNodesByName;
import static jfasm.generator.GeneratorUtils.getTextValeOfNodeAttribute;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FSMConfigurationParser {
	
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

	public Configuration parseConfiguration(File file) throws Exception {
		parse(file);
		return Configuration.newBuilder()
							.setName(name)
							.setPackageName(packageName)
							.setAssociatedWith(associatedWith)
							.setStartState(startState)
							.setThreadSafe(threadSafe)
							.setNeedsCalendar(needsCalendar)
							.setEventListeners(eventListeners)
							.setFields(fields)
							.setStates(states)
							.setEvents(events)
							.setEventsWithTransitions(eventsWithTransitions)
							.build();
	}
	
	private void extractProperties(Node stateDiagram) throws Exception {
		final Node propertiesNode = findItemByName(stateDiagram, "properties");
		final List<Node> childNodes = getChildNodesByName(propertiesNode, "property");
		for (Node node : childNodes) {
			if (node.getNodeName().equals("property")) {
				final String propertyName = getTextValeOfNodeAttribute(node, "name");
				final Class<?> propertyType = Class.forName(getTextValeOfNodeAttribute(node, "type"));
				final boolean propertyIsNullable = Boolean.parseBoolean(getTextValeOfNodeAttribute(node, "nullable"));
				fields.put(propertyName, new Property(propertyType, propertyIsNullable));
				System.out.println("Found property called "+propertyName);
			}
		}
	}
	
	private void extractEventListeners(Node stateDiagram) {
		final List<Node> childNodes = getChildNodesByName(findItemByName(stateDiagram,"eventListeners"), "eventListener");
		for (final Node item : childNodes) {
			final String nodeName = item.getNodeName();
			if (nodeName.equals("eventListener")) {
				final String eventListenerName = getTextValeOfNodeAttribute(item, "name");
				final String generatedInterface = getTextValeOfNodeAttribute(item, "generatedInterface");
				System.out.println("Found eventListener called "+eventListenerName);
				eventListeners.put(eventListenerName, new EventListenerConfiguration(generatedInterface));
			}
		}
	}

	private void extractGlobalSettings(final Node stateDiagram) {
		final NamedNodeMap attributes = stateDiagram.getAttributes();
        name = attributes.getNamedItem("name").getTextContent();
        packageName = attributes.getNamedItem("package").getTextContent();
        associatedWith = attributes.getNamedItem("associatedWith").getTextContent();
        startState = attributes.getNamedItem("startState").getTextContent();
        threadSafe = Boolean.parseBoolean(attributes.getNamedItem("threadSafe").getTextContent());
	}
	
	private void parse(File file) throws Exception {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder newDocumentBuilder = dbf.newDocumentBuilder();
        final Document document = newDocumentBuilder.parse(file);
        final NodeList nodeList = document.getElementsByTagName("statediagram");
        final Node stateDiagram = nodeList.item(0);
        extractGlobalSettings(stateDiagram);
        extractEventListeners(stateDiagram);
        extractProperties(stateDiagram);
        extractEvents(stateDiagram);
        extractTimedEvents(stateDiagram);
        extractStates(stateDiagram);
        
        outputDetails();
        
	}
	
	private void outputDetails() {
		System.out.println("Name:"+name);
		System.out.println("Package:"+packageName);
		System.out.println("Associated with:"+associatedWith);
		System.out.println("Start State:"+startState);
		System.out.println("ThreadSafe:"+threadSafe);
		
		for (Map.Entry<String, State> entry : states.entrySet()) {
			System.out.print(entry.getKey());
			System.out.print(" -> ");
			final State state = entry.getValue();
			final List<OnEvent> onEventTransitions = state.getOnEventTransitions();
			for (OnEvent onEvent : onEventTransitions) {
				System.out.print(onEvent.getTransitionTo());
				System.out.print(", ");
			}
			System.out.println();
		}
	}
	
	private void extractStates(Node stateDiagram) {
		final List<Node> stateNodes = getChildNodesByName(findItemByName(stateDiagram, "states"), "state");
		for (Node node : stateNodes) {
			final String stateName = getTextValeOfNodeAttribute(node, "name");
			System.out.println("Processing state "+stateName);
			if (!states.containsKey(stateName)) {
				states.put(stateName, new State(stateName));
			}
			final State state = states.get(stateName);
			final List<Node> onEventNodes = getChildNodesByName(node, "onEvent");
			for (final Node onEventNode : onEventNodes) {
				final String onEventName = getTextValeOfNodeAttribute(onEventNode, "name");
				final String transitionTo = getTextValeOfNodeAttribute(onEventNode, "transitionTo");
				final String event = getTextValeOfNodeAttribute(onEventNode, "event");
				final String parameterAsAString = getTextValeOfNodeAttribute(onEventNode, "parameter");
				final String timeStamp = getTextValeOfNodeAttribute(onEventNode, "timeStamp");
				final Parameter parameter;
				if (parameterAsAString!=null) {
					parameter = Parameter.valueOf(parameterAsAString.toUpperCase());
				} else {
					parameter = null;
				}
				if (event!=null) {
					final String[] split = event.split("\\.");
					eventListeners.get(split[0]).addEventWithParameter(split[1], parameter);
				}
				final String transitionToString;
				if (transitionTo==null) {
					transitionToString = state.getName();
				} else {
					transitionToString = transitionTo;
				}
				final OnEvent onEvent = new OnEvent(onEventName, transitionToString, parameter, timeStamp, event);
				state.addOnEventTransition(onEvent);
				if (timeStamp !=null) {
					System.out.println("Processing timestamp with name "+timeStamp);
					needsCalendar = true;
					if (!fields.containsKey(timeStamp)) {
						fields.put(timeStamp, new Property(java.util.Date.class, false));
					}
				}
			}
			final List<Node> onEnterNodes = getChildNodesByName(node, "onEnter");
			for (final Node onEnterNode : onEnterNodes) {
				final String event = getTextValeOfNodeAttribute(onEnterNode, "event");
				final String parameterAsAString = getTextValeOfNodeAttribute(onEnterNode, "parameter");
				final Parameter parameter = Parameter.valueOf(parameterAsAString.toUpperCase());
				final OnEnter onEnter = new OnEnter(event, parameter);
				state.addOnEnterEvent(onEnter);
			}
		}
	}
	
	private void extractEvents(final Node stateDiagram) {
		final Node eventsNodes = findItemByName(stateDiagram, "events");
		final List<Node> childNodesByName = getChildNodesByName(eventsNodes, "event");
		for (Node node : childNodesByName) {
			final String eventName = getTextValeOfNodeAttribute(node, "name");
			final String globalTransition = getTextValeOfNodeAttribute(node, "transitionTo");
			final List<Node> parameters = getChildNodesByName(node, "parameter");
			final Map<String, String> globalParameters = new HashMap<String, String>();
			for (final Node parameterNode : parameters) {
				globalParameters.put(getTextValeOfNodeAttribute(parameterNode, "name"),
									   getTextValeOfNodeAttribute(parameterNode, "type"));
			}
			if (!events.containsKey(eventName)) {
				events.put(eventName, new EventConfiguration(globalTransition, globalParameters));
			}
		}
	}
	
	private void extractTimedEvents(Node stateDiagram) {
		System.out.println("TODO - Extract timed events");
	}

}
