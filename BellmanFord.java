import java.util.*;

public class BellmanFord{

    private int[] distances = null;
    private int[] predecessors = null;
    private int source;

    class BellmanFordException extends Exception{
        public BellmanFordException(String str){
            super(str);
        }
    }

    class NegativeWeightException extends BellmanFordException{
        public NegativeWeightException(String str){
            super(str);
        }
    }

    class PathDoesNotExistException extends BellmanFordException{
        public PathDoesNotExistException(String str){
            super(str);
        }
    }

    BellmanFord(WGraph g, int source) throws NegativeWeightException{
        /* Constructor, input a graph and a source
         * Computes the Bellman Ford algorithm to populate the
         * attributes 
         *  distances - at position "n" the distance of node "n" to the source is kept
         *  predecessors - at position "n" the predecessor of node "n" on the path
         *                 to the source is kept
         *  source - the source node
         *
         *  If the node is not reachable from the source, the
         *  distance value must be Integer.MAX_VALUE
         */
    	
    	
    	this.distances = new int [g.getNbNodes()];
    	this.predecessors = new int[g.getNbNodes()];
    	java.util.Arrays.fill(this.distances, Integer.MAX_VALUE);
    	java.util.Arrays.fill(this.predecessors, Integer.MAX_VALUE);
    	this.distances[source] = 0;
    	
    	for (int i = 0; i < g.getNbNodes() - 1; i++) {//getNbNodes() - 1 iterations
    		for (Edge edge:g.getEdges()) {
    			if (this.distances[edge.nodes[0]] + edge.weight < this.distances[edge.nodes[1]]) {
    				//update distance
    				this.distances[edge.nodes[1]] = this.distances[edge.nodes[0]] + edge.weight;
    				//update predecessor
    				this.predecessors[edge.nodes[1]] = edge.nodes[0];
    			}
    		}
    	}
    	
    	
    	//check neg cycles
    	for (int i = 0; i < g.getNbNodes() - 1; i++) {//getNbNodes() - 1 iterations
    		for (Edge edge:g.getEdges()) {
    			if (this.distances[edge.nodes[0]] + edge.weight < this.distances[edge.nodes[1]]) {
    				//negative cycle
    				throw new NegativeWeightException("There is a negative cycle in g");
    			}
    		}
    	}
    }

    public int[] shortestPath(int destination) throws PathDoesNotExistException{
        /* Returns the list of nodes along the shortest path from 
         * the object source to the input destination
         * If no path exists an Error is thrown
         */
    	
    	if (this.distances[destination] == Integer.MAX_VALUE) {
    		throw new PathDoesNotExistException("The path does not exist");
    	}
    	
    	int node = destination;
    	ArrayList<Integer> path = new ArrayList<Integer>();
    	
    	while (node != source) {
    		path.add(0,node);
    		node = this.predecessors[node];
    	}
    	
    	path.add(0,source);
    	
    	int [] p = new int[path.size()];
    	
    	for (int i = 0; i < path.size(); i++) {
    		p[i] = path.get(i);
    	}

        return p;
    }

    public void printPath(int destination){
        /* Print the path in the format s->n1->n2->destination
         * if the path exists, else catch the Error and 
         * prints it
         */
        try {
            int[] path = this.shortestPath(destination);
            for (int i = 0; i < path.length; i++){
                int next = path[i];
                if (next == destination){
                    System.out.println(destination);
                }
                else {
                    System.out.print(next + "-->");
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){
        String file = args[0];
        //String file = "C:\\Eclipse\\hw3_basecode.zip_expanded\\HW3_2021\\bf1.txt";
        WGraph g = new WGraph(file);
        try{
            BellmanFord bf = new BellmanFord(g, g.getSource());
            bf.printPath(g.getDestination());
        }
        catch (Exception e){
            System.out.println(e);
        }
   } 
}

