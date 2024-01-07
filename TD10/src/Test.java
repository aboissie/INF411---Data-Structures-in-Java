import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.PriorityQueue;

public class Test {

	public Test() {
	}

	public static Field test_base_field(Dijkstra d, String name, String type) 
			throws IllegalArgumentException, IllegalAccessException {
		Class<?> dijkstraClass = d.getClass();
		Field field = null;
		for (Field f : dijkstraClass.getDeclaredFields()) {
			if (f.getName().equals(name)) {
				field = f;
				break;
			}
		}
		assert field != null : "\n\nVous n'avez pas ajouté le champ '" + name + "'\n";
		Class<?> fieldClass = field.getType(); 
		assert fieldClass.getName().equals(type) : "\n\nLa classe du champ '" + name + "' n'est pas '" + type + "'\n";
		return field;
	}
	
	public static Field test_util_field(Dijkstra d, String name, String type, String paramType)
			throws IllegalArgumentException, IllegalAccessException {
		Class<?> dijkstraClass = d.getClass();
		Field field = null;
		for (Field f : dijkstraClass.getDeclaredFields()) {
			if (f.getName().equals(name)) {
				field = f;
				break;
			}
		}
		assert field != null : "\n\nVous n'avez pas ajouté le champ '" + name + "'\n";
		Class<?> fieldClass = field.getType(); 
		assert fieldClass.getName().equals("java.util." + type) : "\n\nLa classe du champ '" + name + "' n'est pas '" + type + "'\n";

		if (paramType != null) {
			Type genericFieldType = field.getGenericType();		
			assert genericFieldType instanceof ParameterizedType;
		    ParameterizedType aType = (ParameterizedType) genericFieldType;
		    Type[] fieldArgTypes = aType.getActualTypeArguments();
		    assert fieldArgTypes.length == 1;
		    String fieldArgType = fieldArgTypes[0].getTypeName();
		    assert fieldArgType.equals(paramType) : 
		    	"\n\nLa classe du champ '" + name + "' n'est pas '" + type + "<" + paramType + ">'\n";
		}
		
		return field;
	}
	
	@SuppressWarnings("unchecked")
	public static PriorityQueue<Node> getPriQueue(Dijkstra d, String name) 
			throws IllegalArgumentException, IllegalAccessException {
		Field field = test_util_field(d, name, "PriorityQueue", "Node");
		return (field == null) ? null : (PriorityQueue<Node>) field.get(d);
	}
	
	public static Field test_array_field(Dijkstra d, String name, String type)
			throws IllegalArgumentException {
		Class<?> dijkstraClass = d.getClass();
		Field field = null;
		for (Field f : dijkstraClass.getDeclaredFields()) {
			if (f.getName().equals(name)) {
				field = f;
				break;
			}
		}
		assert field != null : "\n\nVous n'avez pas ajouté le champ '" + name + "'\n";
		Class<?> fieldClass = field.getType(); 
		assert fieldClass.isArray() : "\n\nLe champ '" + name + "' n'est pas un tableau\n";
		assert fieldClass.getComponentType().getName().equals(type) : "\n\nLe champ '" + name + "' n'est pas un tableau de '" + type + "'\n";
		return field;
	}
	
	public static boolean[] get_boolean_array(Dijkstra d, String name) 
			throws IllegalArgumentException, IllegalAccessException {
		Field field = test_array_field(d, name, "boolean");
		return (field == null) ? null : (boolean []) field.get(d);
	}
	
	public static void test_int_field(Dijkstra d, String name, int value) 
			throws IllegalArgumentException, IllegalAccessException {
		Field field = test_base_field(d, name, "int");
		int intField;
		try {
			intField = (int) field.get(d);
		} catch (IllegalAccessException e) {
			throw new Error ("\n\nLe champ '" + name + "' ne doit pas être déclaré comme 'private'\n");			
		}

		assert intField == value : "\n\nVous n'avez pas initialisé " + name + " correctement\n"; 
	}
	
	public static void test_int_array_field(Graph g, Dijkstra d, String name, int defaultVal, int sourceVal) 
			throws IllegalArgumentException, IllegalAccessException {
		Field field = test_array_field(d, name, "int");
		int[] intField = null;
		try {
			intField = (int[]) field.get(d);
		} catch (IllegalAccessException e) {
			throw new Error ("\n\nLe champ '" + name + "' ne doit pas être déclaré comme 'private'\n");			
		}
		
		assert intField != null : "\n\nVous n'avez pas initialisé le tableau '" + name + "'\n";		
		assert intField.length == g.nbVertices : "\n\nLa taille du tableau '" + name + "' est incorrecte\n";

		for (int i = 1; i < intField.length; i++) {
			if (i == d.source)
				continue;
			
			assert intField[i] == defaultVal : "\n\nVous n'avez pas bien initialisé les cases du tableau '" + name + "'\n";
		}
		
		assert intField[0] == defaultVal : "\n\nVous n'avez pas bien initialisé " + name + "[0]. "
				+ "Ce sommet n'est pas la source dans ce test!\n";
		assert intField[d.source] == sourceVal : "\n\nLa valeur " + name + "[source] est incorrecte\n";
	}
	
	public static void test_bool_array_field(Graph g, Dijkstra d, String name, boolean defaultVal, boolean sourceVal) 
			throws IllegalArgumentException, IllegalAccessException {
		Field field = test_array_field(d, name, "boolean");
		boolean[] boolField = null;
		try {
			boolField = (boolean[]) field.get(d);
		} catch (IllegalAccessException e) {
			throw new Error ("\n\nLe champ '" + name + "' ne doit pas être déclaré comme 'private'\n");			
		}

		assert boolField != null : "\n\nVous n'avez pas initialisé le tableau 'settled'\n";
		assert boolField.length == g.nbVertices : "\n\nLa taille du tableau 'settled' est incorrecte\n";


		for (int i = 1; i < boolField.length; i++) {
			if (i == d.source)
				continue;
			
			assert boolField[i] == defaultVal : "\n\nVous n'avez pas bien initialisé les cases du tableau '" + name + "'\n";
		}
		
		assert boolField[0] == defaultVal : "\n\nVous n'avez pas bien initialisé " + name + "[0]. "
				+ "Ce sommet n'est pas la source dans ce test!\n";
		assert boolField[d.source] == sourceVal : "\n\nLa valeur " + name + "[source] est incorrecte\n";
	}

	@SuppressWarnings("unchecked")
	public static void test_node_pri_queue(Dijkstra d, String name, Object value) 
			throws IllegalArgumentException, IllegalAccessException {
		Field field = test_util_field(d, name, "PriorityQueue", "Node");
		PriorityQueue<Node> queue = null;
		try {
			queue = (PriorityQueue<Node>) field.get(d);
		} catch (IllegalAccessException e) {
			throw new Error ("\n\nLe champ '" + name + "' ne doit pas être déclaré comme 'private'\n");			
		}

		assert queue != null : "\n\nVous n'avez pas initialisé le champ '" + name + "' de Dijkstra\n";
		assert !queue.isEmpty() : "\n\nVous n'avez pas correctement initialisé le champ '" + name + "' de Dijkstra\n";
		Node node = queue.poll();
		assert (node != null && node.equals(value)) : "\n\nLa file de priortié '" + name + "' contient un sommet différent de la source\n";
	}
}
