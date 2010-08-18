package jfasm.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static jfasm.generator.GeneratorUtils.findItemByName;
import static jfasm.generator.GeneratorUtils.getChildNodesByName;
import static jfasm.generator.GeneratorUtils.getTextValeOfNodeAttribute;

public class FSMGenerator {
	
	public static void main(String[] args) throws Exception {
        final File file = new File(args[0]);
        final Configuration configuration = new FSMConfigurationParser().parseConfiguration(file);
        final FSMGenerator generator = new FSMGenerator();
        generator.generate(configuration);
        
	}

	private void generate(Configuration configuration) {
		final InterfaceGenerator interfaceGenerator = new InterfaceGenerator(configuration);
		interfaceGenerator.generate();
		final ClassGenerator classGenerator = new ClassGenerator(configuration);
		classGenerator.generate();
		final EventListenerGenerator eventListenerGenerator = new EventListenerGenerator(configuration);
		eventListenerGenerator.generate();
	}




	





	

	

}
