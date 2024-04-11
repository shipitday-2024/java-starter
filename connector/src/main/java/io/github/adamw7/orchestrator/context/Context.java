package io.github.adamw7.orchestrator.context;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.adamw7.orchestrator.ClassContainer;

public class Context {
	private static final Logger log = LogManager.getLogger(Context.class.getName());
	private final Set<ClassContainer> allContainers;

	public Context(Set<ClassContainer> allContainers) {
		this.allContainers = allContainers;
	}

	public Set<ClassContainer> find(ClassContainer root, int depth) {
		Set<ClassContainer> classes = findAllUsedClasses(root, depth);
		for (int i = depth - 1; i > 0; i--) {
			for (ClassContainer clazz : classes) {
				classes.addAll(find(clazz, i));
			}
		}
		return classes;
	}

	private Set<ClassContainer> findAllUsedClasses(ClassContainer root, int depth) {
		if (depth <= 0) {
			throw new IllegalArgumentException("Depth must be positive and received: " + depth);
		}
		Pattern pattern = Pattern.compile("\\b[A-Z][A-Za-z0-9_]*(\\.[A-Z][A-Za-z0-9_]*)*\\b");
		return findByRegEx(root, pattern);
	}

	private Set<ClassContainer> findByRegEx(ClassContainer root, Pattern pattern) {
		Set<ClassContainer> classes = ConcurrentHashMap.newKeySet();
		Matcher matcher = pattern.matcher(root.getOriginalCode());
		while (matcher.find()) {
			String className = matcher.group();
			ClassContainer container = findContainer(className);
			if (container != null) {
				log.info("Found {} used in {}", container.getClassName(), root.getClassName());
				classes.add(container);				
			}
		}
		return classes;
	}

	private ClassContainer findContainer(String className) {
		for (ClassContainer container : allContainers) {
			if (container.getClassName().equals(className + ".java")) {
				return container;
			}
		}
		return null;
	}
}
