public class Test2 {
	
	static final int president = 524287;

	public static void main(String[] args) {
		
		// vérifie que les asserts sont activés
		if (!Test2.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Java>Debug>settings: Vm Args)");
			System.exit(1);
		}
		
		System.out.print("Test de la classe Network... ");
		
		Network network = new Network(1000000);
		
		// premier appel impliquant le Président
		network.runUntilCall(president);
		assert(network.getCallCount() == 1840579);
		assert(network.getLastCaller() == 910544);
		assert(network.getLastCallee() == 524287);
				
		// deuxième appel impliquant le Président
		network.runUntilCall(president);
		assert(network.getCallCount() == 2113887);
		assert(network.getLastCaller() == 898055);
		assert(network.getLastCallee() == 524287);

		System.out.println("[OK]");
		
	}
	
}
