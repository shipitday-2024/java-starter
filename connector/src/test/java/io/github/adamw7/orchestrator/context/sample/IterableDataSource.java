package io.github.adamw7.orchestrator.context.sample;

import java.io.Closeable;

public interface IterableDataSource extends AutoCloseable, Closeable {
	
	String[] getColumnNames();
	
	void open();
	
	String[] nextRow();

	boolean hasMoreData();
	
	void reset();
	
}
