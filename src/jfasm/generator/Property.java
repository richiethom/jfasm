package jfasm.generator;

public final class Property {
	
	private final Class<?> clazz;
	private final boolean nullable;
	
	public Property(final Class<?> clazz, final boolean nullable) {
		this.clazz = clazz;
		this.nullable = nullable;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
	
	public boolean isNullable() {
		return nullable;
	}

}
