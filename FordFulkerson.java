import java.util.*;

public class FordFulkerson {
	/*
	public static ArrayList<Integer> helper (Integer v, WGraph graph, boolean visited [], ArrayList<Integer> path){
		
		
		
		visited[v] = true;
		path.add(v);
		
		if (v == graph.getDestination()) {
			return path;
		}
		
		ArrayList <Integer> choices = new ArrayList<Integer>();
		
		for (Edge edge:graph.getEdges()) {
			if (edge.nodes[0] == v) {
				//System.out.print("HELLO \n");
				//if (edge.weight > 0) { //has remaining capacity
				choices.add(edge.nodes[1]);
				//}
			}
		}
		
		Iterator<Integer> iterator = choices.listIterator();
		while(iterator.hasNext()) {
			int vertex = iterator.next();
			if (!visited[vertex]) {
				//System.out.print("HELLO2 \n");
				
				helper(vertex, graph, visited, path);
				
			}
		}
		return path;

	}
	
	
	public static ArrayList<Integer> DFS(Integer source, Integer destination, WGraph graph) {
		
		boolean visited [] = new boolean[graph.getNbNodes()];
		ArrayList <Integer> path = new ArrayList<Integer>();
		return helper(graph.getSource(), graph, visited, path);
	}
	*/
	
		
	/*
	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph){
		boolean visited [] = new boolean[graph.getNbNodes()];
		ArrayList <Integer> path = new ArrayList<Integer>();
		path = helper(graph.getSource(), graph, visited, path);
		//System.out.print(path.toString());
		if (path.get((path.size() - 1)) != graph.getDestination()) {//if last element is not dest
			return null;
		}
		
		return path;

	}*/
	
	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph){
		ArrayList <Integer> path = new ArrayList<Integer>();
		ArrayList <Integer> visited = new ArrayList<Integer>();
		
		//add source
		path.add(source);
		visited.add(source);

		while(path.size()>0) {
			int last = path.size() - 1;
			if (path.get(last) == destination) {
				break; //get out of loop when reaching a sink
			}
			
			ArrayList<Edge> edges = graph.getEdges();
			for (Edge edge:edges) {
				if (edge.nodes[0] == path.get(last) && edge.weight > 0 && !visited.contains(edge.nodes[1])) {
					path.add(edge.nodes[1]);
					visited.add(edge.nodes[1]);
					break;
				}
				
				//if we're at the last edge and it didnt get added
				if (edge == edges.get(edges.size()-1)) {
					path.remove(path.size()-1); //remove last element of path
				}
			}	
		}
		return path;
	}
	
	public static WGraph residual(WGraph g, int flow, ArrayList<Edge> edges) { 
		WGraph rg = new WGraph(g);
		
		for (Edge edge:edges) {
			//if (flow <= edge.weight) {//forward edge
			rg.setEdge(edge.nodes[0], edge.nodes[1], edge.weight - flow);
			//}
			
			if (flow > 0) { //backward edge
				
				if (rg.getEdge(edge.nodes[1], edge.nodes[0]) == null) {//no such edge. add edge
					Edge e = new Edge(edge.nodes[1], edge.nodes[0], flow);
					rg.addEdge(e);	
					
				}
				else {//edge exists
					rg.setEdge(edge.nodes[1], edge.nodes[0], rg.getEdge(edge.nodes[1], edge.nodes[0]).weight + flow);
				}
			}
		}
		
		return rg;
	}
	
	
	public static int bottleNeck(ArrayList<Integer> path, WGraph graph) {
		
		if (path.isEmpty()) {
			return 0;
		}
		
		ArrayList<Edge> edges = getPathEdges(path, graph);
		//initiate bottleNeck value
		int bottleNeck = Integer.MAX_VALUE - 1; 
		
		//loop through each edge of path
		for (Edge edge:edges) {
			if (edge.weight < bottleNeck) {
				bottleNeck = edge.weight; //update bottleNeck
			}
		}
		
		return bottleNeck;
		
	}
	
	public static ArrayList <Edge> getPathEdges(ArrayList<Integer> path, WGraph graph){
		
		ArrayList <Edge> edges = new ArrayList <Edge>();
		
		if (path.isEmpty()) {
			return edges;
		}
	
		//get list of edges from path
		for (int i = 0; i < path.size() - 1; i++) {
			edges.add(graph.getEdge(path.get(i), path.get(i+1)));
		}
		return edges;
	}
	
	/*
	public static WGraph augment(ArrayList<Integer> path, WGraph graph, WGraph residual, int bottleNeck) {
	
		if (!path.isEmpty()) {
			ArrayList<Edge> edges = getPathEdges(path, residual);
			
			//traverse each edge of path
			for (Edge edge:edges) {
				if (graph.getEdge(edge.nodes[0], edge.nodes[1]) != null) { //forward edge
					graph.getEdge(edge.nodes[0], edge.nodes[1]).weight -= bottleNeck;
					//System.out.print("WEIGHT DECREASE\n");
				}
				else {//backward edge
					graph.getEdge(edge.nodes[1], edge.nodes[0]).weight += bottleNeck;						
					//System.out.print("WEIGHT INCREASE\n");
				}
			}
		}
		//System.out.print("IN AUGMENT");
		return graph;
		
	}*/
	
	public static WGraph update(WGraph graph, WGraph residual) {
		
		for (Edge e:graph.getEdges()) {
			e.weight -= residual.getEdge(e.nodes[0], e.nodes[1]).weight;
		}
		return graph;
	}
	
	
	

	public static String fordfulkerson(WGraph graph){
		String answer = "";
		int maxFlow = 0;
		int bottleNeck;
		
		WGraph residual = new WGraph(graph); //initiate residual graph
		
		
		while (!pathDFS(residual.getSource(), residual.getDestination(), residual).isEmpty()) {
			ArrayList<Integer> path = pathDFS(residual.getSource(), residual.getDestination(), residual);
			bottleNeck = bottleNeck(path, residual);
			ArrayList <Edge> edges = getPathEdges(path, residual);
			
			//update residual
			residual = residual(residual, bottleNeck, edges);
			maxFlow += bottleNeck; //AUGMENT

		}
		
		update(graph, residual);
		answer += maxFlow + "\n" + graph.toString();	
		return answer;
	}
	

	 public static void main(String[] args){
		 
		 String file = args[0];
		 //String file = "C:\\Eclipse\\hw3_basecode.zip_expanded\\HW3_2021\\ff2.txt";
		 WGraph g = new WGraph(file);
		 System.out.println(fordfulkerson(g));

	 }
}

