package wtf.godlydev.console.utils;

public class Pair<P1, P2> {
	
	private final P1 value1;
	private final P2 value2;
	
	public Pair(P1 value1, P2 value2) {
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public P1 getValue1() {
		return value1;
	}
	
	public P2 getValue2() {
		return value2;
	}

}
