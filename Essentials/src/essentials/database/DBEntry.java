package essentials.database;

import java.io.IOException;

public abstract class DBEntry {
	public abstract void fromString(String s) throws IOException;

	public abstract String primary();

	@Override
	public abstract String toString();

	public DBEntry() {
	}
}
