import java.util.ArrayList;
//in collab with :  hassan haidar 
//					muhammad huzaifa elahi
public class BellmanFord {

	/**
	 * Utility class. Don't use.
	 */
	public class BellmanFordException extends Exception {
		private static final long serialVersionUID = -4302041380938489291L;

		public BellmanFordException() {
			super();
		}

		public BellmanFordException(String message) {
			super(message);
		}
	}

	/**
	 * Custom exception class for BellmanFord algorithm
	 * 
	 * Use this to specify a negative cycle has been found
	 */
	public class NegativeWeightException extends BellmanFordException {
		private static final long serialVersionUID = -7144618211100573822L;

		public NegativeWeightException() {
			super();
		}

		public NegativeWeightException(String message) {
			super(message);
		}
	}

	/**
	 * Custom exception class for BellmanFord algorithm
	 *
	 * Use this to specify that a path does not exist
	 */
	public class PathDoesNotExistException extends BellmanFordException {
		private static final long serialVersionUID = 547323414762935276L;

		public PathDoesNotExistException() {
			super();
		}

		public PathDoesNotExistException(String message) {
			super(message);
		}
	}

	private int[] distances = null;
	private int[] predecessors = null;
	private int source;

	public static ArrayList<Integer> getNodes(WGraph g) {
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		for (Edge e : g.getEdges()) {
			for (int node : e.nodes) {
				if (!nodes.contains(node)) {
					nodes.add(node);

				}
			}
		}
		return nodes;

	}

	private static ArrayList<Integer> getAdjacencyList(Integer node, ArrayList<Edge> edges) {
		ArrayList<Integer> adjacencyList = new ArrayList<Integer>();
		for (Edge edge : edges) {
			if ((edge.nodes[0] == node)) {
				adjacencyList.add(edge.nodes[1]);
			}
		}
		return adjacencyList;
	}

	BellmanFord(WGraph g, int source) throws BellmanFordException {
		/*
		 * Constructor, input a graph and a source Computes the Bellman Ford
		 * algorithm to populate the attributes distances - at position "n" the
		 * distance of node "n" to the source is kept predecessors - at position
		 * "n" the predecessor of node "n" on the path to the source is kept
		 * source - the source node
		 *
		 * If the node is not reachable from the source, the distance value must
		 * be Integer.MAX_VALUE
		 * 
		 * When throwing an exception, choose an appropriate one from the ones
		 * given above
		 */
		this.distances = new int[g.getNbNodes()];
		this.predecessors = new int[g.getNbNodes()];
		
		for (Integer i : getNodes(g)) {
			distances[i] = Integer.MAX_VALUE;
			predecessors[i] = -1;
		}

		distances[source] = 0;
		predecessors[source] = 0;

		for (int i = 0; i < g.getNbNodes() - 1; i++) {
			for (Integer n : getNodes(g)) {
				ArrayList<Integer> adj = getAdjacencyList(n, g.getEdges());
				for (Integer adjnode : adj) {
					Edge e = g.getEdge(n, adjnode);

					int pDistance = this.distances[adjnode];
					if (this.distances[n] < Integer.MAX_VALUE) {
						this.distances[adjnode] = Math.min(this.distances[adjnode], this.distances[n] + e.weight);
					}

					if (adjnode != source && pDistance != this.distances[adjnode]) {
						this.predecessors[adjnode] = n;
					}
					// update(e);
				}
			}
		}
		for (Edge e : g.getEdges()) {
			int u = e.nodes[0];
			int v = e.nodes[1];
			int weight = e.weight;
			if (this.distances[u] != Integer.MAX_VALUE && (this.distances[u]) + weight < this.distances[v]) {
				throw new NegativeWeightException();
			}
		}

	}

	public void update(Edge e) {
		if (distances[e.nodes[0]] != Integer.MAX_VALUE && distances[e.nodes[0]] + e.weight < distances[e.nodes[1]]) {
			distances[e.nodes[1]] = distances[e.nodes[0]] + e.weight;
		}
	}

	public int[] shortestPath(int destination) throws BellmanFordException {
		/*
		 * Returns the list of nodes along the shortest path from the object
		 * source to the input destination If not path exists an Exception is
		 * thrown Choose appropriate Exception from the ones given
		 */
		ArrayList<Integer> path = new ArrayList<Integer>();
		path = dijkstra(path, destination, this.predecessors, this.source);

		if (path.isEmpty()) {
			throw new PathDoesNotExistException();

		} else if (path.get(0) == destination && path.get(path.size() - 1) == source) {
			int[] SP = new int[path.size()];
			for (int i = 0; i < path.size(); i++) {
				SP[i] = path.get(path.size() - 1 - i);
			}
			return SP;
		}
		return null;
	}
	public static ArrayList<Integer> dijkstra (ArrayList<Integer> path, int destination, int [] predecessors, int source){
		path.add(destination);
		if (predecessors[destination] == -1&& destination != source){
			return new ArrayList<Integer>();
		}
		else if ( destination == source){
			return path;
			
		}else {
			dijkstra(path, predecessors[destination], predecessors, source);
			
		}
		return path;
	}

	public void printPath(int destination) {
		/*
		 * Print the path in the format s->n1->n2->destination if the path
		 * exists, else catch the Error and prints it
		 */
		try {
			int[] path = this.shortestPath(destination);
			for (int i = 0; i < path.length; i++) {
				int next = path[i];
				if (next == destination) {
					System.out.println(destination);
				} else {
					System.out.print(next + "-->");
				}
			}
		} catch (BellmanFordException e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {

		String file = args[0];
		WGraph g = new WGraph(file);
		try {
			BellmanFord bf = new BellmanFord(g, g.getSource());
			bf.printPath(g.getDestination());
		} catch (BellmanFordException e) {
			System.out.println(e);
		}

	}
}
