import java.io.*;
import java.util.*;

public class FordFulkerson {

	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph) {
		ArrayList<Integer> Stack = new ArrayList<Integer>();
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		
		nodes = getNodes(graph);
		int[] visited = new int[nodes.size()];
		
		 DFS(source, destination , graph , visited, Stack);
		
		return Stack;
	}
	public static void DFS (Integer source, Integer destination, WGraph graph, int[] visited, ArrayList<Integer> stack){
		ArrayList<Integer> adj = new ArrayList<Integer>();
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		
		nodes = getNodes(graph);
		adj = getAdj(nodes.get(source), graph);
		visited[source]++;
		stack.add(nodes.get(source));
		for (Integer node : adj){
			if (nodes.get(node) != null && visited[node] ==0){
			DFS(node, destination, graph, visited, stack);

			}
		

		}
	}


	public static ArrayList<Integer> getAdj (int node, WGraph g){
		ArrayList<Integer> adj= new ArrayList<Integer>();
		for (Edge e: g.getEdges()){
			for (int n : e.nodes)
			if (n == node)
				if (e.nodes[0]==node){
					adj.add(e.nodes[1]);
				}
				else{
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

	public static void fordfulkerson(Integer source, Integer destination, WGraph graph, String filePath) {
		String answer = "";
		String myMcGillID = "260725697"; // Please initialize this variable with
											// your McGill ID
		int maxFlow = 0;

		pathDFS(source, destination, graph);
		/*
		 * YOUR CODE GOES HERE // // // // // // //
		 */

		answer += maxFlow + "\n" + graph.toString();
		writeAnswer(filePath + myMcGillID + ".txt", answer);
		System.out.println(answer);
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
