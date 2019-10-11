import java.io.*;
import java.util.*;
// in collab with : hassan haidar 
//					muhammad huzaifa elahi
public class FordFulkerson {

	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph) {
		ArrayList<Integer> Stack = new ArrayList<Integer>();
		ArrayList<Integer> nodes = new ArrayList<Integer>();

		nodes = getNodes(graph);
		int[] visited = new int[nodes.size()];

		DFS(source, destination, graph, visited, Stack);

		if (Stack.get(Stack.size() - 1) == destination) {
			return Stack;
		} else {
			Stack.removeAll(nodes);
			return Stack;
		}
	}

	public static void DFS(Integer source, Integer destination, WGraph graph, int[] visited, ArrayList<Integer> stack) {
		ArrayList<Integer> adj = new ArrayList<Integer>();
		ArrayList<Integer> nodes = new ArrayList<Integer>();

		nodes = getNodes(graph);
		adj = getAdj(source, graph.getEdges());
		visited[source]++;
		stack.add(source);
		for (Integer node : adj) {
			if (nodes.get(node) != null && visited[node] == 0 && (graph.getEdge(source, node)).weight > 0) {
				DFS(node, destination, graph, visited, stack);
				if (stack.get(stack.size() - 1) != destination) {
					stack.remove(stack.size() - 1);
				}
			}

		}
	}

	private static ArrayList<Integer> getAdj(Integer node, ArrayList<Edge> edges) {
		ArrayList<Integer> adjacencyList = new ArrayList<Integer>();
		for (Edge edge : edges) {
			if ((edge.nodes[0] == node)) {
				adjacencyList.add(edge.nodes[1]);
			}
		}
		return adjacencyList;
	}

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

	public static WGraph createG(WGraph g) {
		WGraph graph = new WGraph(g);
		ArrayList<Edge> edgesF = graph.getEdges();
		for (Edge e : edgesF) {
			e.weight = 0;
		}
		return graph;
	}

	public static void fordfulkerson(Integer source, Integer destination, WGraph graph, String filePath) {

		String answer = "";
		String myMcGillID = "260725697"; // Please initialize this variable with
											// your McGill ID
		int maxFlow = 0;
		// int[][] forwardEdge = new
		// int[(graph.getNbNodes())][(graph.getNbNodes())];

		WGraph graphF = createG(graph);

		// //ArrayList<Integer> path = pathDFS(source, destination, graph);
		//
		// if (pathR.isEmpty()) {
		// answer += maxFlow + "\n" + graph.toString();
		// writeAnswer(filePath + myMcGillID + ".txt", answer);
		// System.out.println(answer);
		// }
		while (true) {

			WGraph graphR = new WGraph();

			ArrayList<Edge> fEdge = new ArrayList<Edge>();
			ArrayList<Edge> bEdge = new ArrayList<Edge>();

			for (Edge e : graph.getEdges()) {
				int flow = graphF.getEdge(e.nodes[0], e.nodes[1]).weight;
				int capacity = e.weight;

				if (flow < capacity) {
					Edge edge = new Edge(e.nodes[0], e.nodes[1], capacity - flow);
					if (!isinGraph(graphR, edge)) {
						graphR.addEdge(edge);
						fEdge.add(edge);
					}
				}

				if (flow > 0) {
					Edge edge = new Edge(e.nodes[1], e.nodes[0], flow);
					if (!isinGraph(graphR, edge)) {
						graphR.addEdge(edge);
						bEdge.add(edge);
					}

				}
			}

			ArrayList<Integer> pathR = pathDFS(source, destination, graphR);
			ArrayList<Edge> edgePathR = getEdge(pathR, graphR);

			if (!pathR.isEmpty()) {
				int Beta = getBeta(edgePathR);
				for (Edge e : edgePathR) {
					if (fEdge.contains(e)) {
						graphF.getEdge(e.nodes[0], e.nodes[1]).weight += Beta;
					} else {
						graphF.getEdge(e.nodes[1], e.nodes[0]).weight -= Beta;
					}
				}
			} else {
				break;
			}

		}
		for (Edge e : graphF.getEdges()) {
			if (e.nodes[0] == source) {
				maxFlow += e.weight;
			}
		}

		answer += maxFlow + "\n" + graphF.toString();
		writeAnswer(filePath + myMcGillID + ".txt", answer);
		System.out.println(answer);

	}

	private static boolean isinGraph(WGraph graph, Edge edge) {
		for (Edge e : graph.getEdges()) {
			if ((edge.nodes[0] == e.nodes[0]) && (edge.nodes[1] == e.nodes[1])) {
				return true;
			}
		}
		return false;
	}

	private static int getBeta(ArrayList<Edge> edges) {
		// find the bottleneck among path;
		int beta = edges.get(0).weight;
		for (Edge edge : edges) {
			beta = Math.min(beta, edge.weight);
		}
		return beta;
	}

	public static void graphUpdate(WGraph g1, WGraph g2) {
		g1 = new WGraph(g2);

	}

	private static ArrayList<Edge> getEdge(ArrayList<Integer> path, WGraph graph) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (int i = 0; i < path.size() - 1; i++) {
			edges.add(graph.getEdge(path.get(i), path.get(i + 1)));
		}
		return edges;
	}

	public static int augmentPath(ArrayList<Integer> path, WGraph graph, int[][] fe, int maxflow) {
		int bottleNeck = 55555555;
		ArrayList<Edge> edgecapacity = graph.getEdges();
		for (Edge e : edgecapacity) {
			bottleNeck = Math.min(bottleNeck, e.weight);
		}

		for (int i = 0; i < path.size() - 1; i++) {
			Edge e = graph.getEdge(path.get(i), path.get(i + 1));
			if (fe[e.nodes[0]][e.nodes[1]] == 0) {
				maxflow -= bottleNeck;
			} else {
				maxflow += bottleNeck;
			}
		}
		return maxflow;
	}

	public static void writeAnswer(String path, String line) {
		BufferedReader br = null;
		File file = new File(path);
		// if file doesnt exists, then create it

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(line + "\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		String file = args[0];
		File f = new File(file);
		WGraph g = new WGraph(file);
		fordfulkerson(g.getSource(), g.getDestination(), g, f.getAbsolutePath().replace(".txt", ""));
	}
}
