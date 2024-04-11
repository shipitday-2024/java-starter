package io.github.adamw7.orchestrator.context;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.adamw7.orchestrator.ClassContainer;
import io.github.adamw7.orchestrator.context.sample.AbstractFileSource;
import io.github.adamw7.orchestrator.context.sample.CSVDataSource;
import io.github.adamw7.orchestrator.context.sample.IterableDataSource;

public class ContextTest {
	protected final static Set<ClassContainer> allContainers = new HashSet<>();
	protected final static String currentDir = System.getProperty("user.dir")
			+ "/src/test/java/io/github/adamw7/orchestrator/context/sample/";

	@BeforeAll
	public static void prepareSources() {
		allContainers.add(createContainer(currentDir + toFileName(AbstractFileSource.class)));
		allContainers.add(createContainer(currentDir + toFileName(CSVDataSource.class)));
		allContainers.add(createContainer(currentDir + toFileName(IterableDataSource.class)));
	}

	private static String toFileName(@SuppressWarnings("rawtypes") Class clazz) {
		return clazz.getSimpleName() + ".java";
	}

	private static ClassContainer createContainer(String location) {
		File file = new File(location);
		Path path = Path.of(file.getAbsolutePath());
		return new ClassContainer(path, path.getFileName().toString(), null);
	}

	@Test
	public void levelOne() {
		ClassContainer root = createContainer(currentDir + toFileName(AbstractFileSource.class));
		Set<ClassContainer> classes = new Context(allContainers).find(root, 1);

		require(classes, toFileName(IterableDataSource.class), toFileName(AbstractFileSource.class));
	}

	private void require(Set<ClassContainer> classes, String... fileNames) {
		assertThat(classes).hasSize(fileNames.length);
		Set<String> found = lookForFileNames(classes, fileNames);
		for (String fileName : fileNames) {
			assertThat(found).contains(fileName);
		}
	}

	private Set<String> lookForFileNames(Set<ClassContainer> classes, String... fileNames) {
		Set<String> found = new HashSet<>();

		for (ClassContainer clazz : classes) {
			for (String fileName : fileNames) {
				if (clazz.getClassName().equals(fileName)) {
					found.add(fileName);
				}
			}
		}
		return found;
	}
	
	@Test
	public void levelTwo() {
		ClassContainer root = createContainer(currentDir + toFileName(CSVDataSource.class));
		
		Set<ClassContainer> classes = new Context(allContainers).find(root, 2);

		require(classes, toFileName(IterableDataSource.class), toFileName(AbstractFileSource.class),
				toFileName(CSVDataSource.class));
	}
}
