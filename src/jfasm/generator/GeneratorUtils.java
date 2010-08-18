package jfasm.generator;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class GeneratorUtils {
	
	private GeneratorUtils() {
		//prevent instantiation
	}
	
	public static String firstCharacterToLowerCase(final String stringToConvert) {
		return Character.toLowerCase(stringToConvert.charAt(0))+stringToConvert.substring(1);
	}
	
	public static String getClassNameFromFullyQualified(final String fullyQualifiedName) {
		final String[] split = fullyQualifiedName.split("\\.");
		return split[split.length-1];
	}
	
	public static String getPackageNameFromFullyQualified(final String fullyQualifiedName) {
		return fullyQualifiedName.substring(0, fullyQualifiedName.length()-getClassNameFromFullyQualified(fullyQualifiedName).length()-1);
	}
	
	public static String getTextValeOfNodeAttribute(final Node node, final String attributeName) {
		final Node namedItem = node.getAttributes().getNamedItem(attributeName);
		if (namedItem==null) {
			return null;
		}
		return namedItem.getTextContent();
	}
	
	public static List<Node> getChildNodesByName(Node nodeToSearch, String name) {
		final List<Node> returnList = new ArrayList<Node>();
		final NodeList childNodes = nodeToSearch.getChildNodes();
		for (int i=0;i<childNodes.getLength();i++) {
			final Node item = childNodes.item(i);
			if (item.getNodeName().equals(name)) {
				returnList.add(item);
			}
		}
		return returnList;
	}

	public static Node findItemByName(Node nodeToSearch, String name) {
		final NodeList childNodes = nodeToSearch.getChildNodes();
		for (int i=0;i<childNodes.getLength();i++) {
			final Node item = childNodes.item(i);
			final String nodeName = item.getNodeName();
			if (nodeName.equals(name)) {
				return item;
			}
		}
		throw new IllegalStateException("Could not find node by name of "+name+" as child of "+nodeToSearch.getNodeName());
	}
	
}
