import java.io.*;
import java.util.*;

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
		adj = getAdj(nodes.get(source), graph);
		visited[source]++;
		stack.add(nodes.get(source));
		for (Integer node : adj) {
			if (nodes.get(node) != null && visited[node] == 0) {
				DFS(node, destination, graph, visited, stack);

			}

		}
	}

	public static ArrayList<Integer> getAdj(int node, WGraph g) {
		ArrayList<Integer> adj = new ArrayList<Integer>();
		for (Edge e : g.getEdges()) {
			for (int n : e.nodes)
				if (n == node)
					if (e.nodes[0] == node) {
						adj.add(e.nodes[1]);
					} else {
						adj.add(e.nodes[0]);
					}
		}
		return adj;
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
		int[][] forwardEdge = new int[(graph.getNbNodes())][(graph.getNbNodes())];

		WGraph graphF = createG(graph);
		WGraph graphR = createG(graph);

		ArrayList<Integer> path = pathDFS(source, destination, graph);

		if (path.isEmpty()) {
			answer += maxFlow + "\n" + graph.toString();
			writeAnswer(filePath + myMcGillID + ".txt", answer);
			System.out.println(answer);
		}

		int bottleNeck = 55555555;
		ArrayList<Edge> edgecapacity = graph.getEdges();
		for (Edge e : edgecapacity) {
			bottleNeck = Math.min(bottleNeck, e.weight);
		}

		for (int i = 0; i < path.size() - 1; i++) {
			Edge e = graphR.getEdge(path.get(i), path.get(i + 1));
			e.weight = e.weight + bottleNeck; // increment
		}

		for (Edge e : graph.getEdges()) {
			if (bottleNeck < e.weight) {
				//graphR.addEdge(new Edge(e.nodes[0], e.nodes[1], e.weight - bottleNeck));
				graphR.setEdge(e.nodes[0], e.nodes[1], e.weight-bottleNeck);
				forwardEdge[e.nodes[0]][e.nodes[1]] = e.weight;

			}
			if (bottleNeck > 0) {
				//graphR.addEdge(new Edge(e.nodes[0], e.nodes[1], bottleNeck));
				graphR.setEdge(e.nodes[0], e.nodes[1], bottleNeck);
			}
		}
		
		while (true){
			ArrayList<Integer> pathR = pathDFS(source, destination, graphR);
			if (! pathR.isEmpty()){
				maxFlow = augmentPath( pathR, graphR, forwardEdge, maxFlow);
				
				graphUpdate(graphF,graphR);
			}else {
				break;
			}
		}

	}

	public static void graphUpdate(WGraph g1, WGraph g2){
		g1 = new WGraph(g2);
	 
	}
	public static int augmentPath(ArrayList<Integer> path, WGraph graph, int[][] fe, int maxflow) {
		int bottleNeck = 55555555;
		ArrayList<Edge> edgecapacity = graph.getEdges();
		for (Edge e : edgecapacity) {
			bottleNeck = Math.min(bottleNeck, e.weight);
		}

		for (int i = 0; i < path.size() - 1; i++) {
			Edge e = graph.getEdge(path.get(i), path.get(i+1));
			if ( fe[e.nodes[0]][e.nodes[1]] == 0){
				maxflow -= bottleNeck;
			}else {
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
