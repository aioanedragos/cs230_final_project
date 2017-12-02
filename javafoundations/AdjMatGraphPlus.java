/********************************************************************
  * KNOWN FEATURES/BUGS: 
  * It handles unweighted graphs only, but it can be extended.
  * It does not handle operations involving non-existing vertices
  ********************************************************************/
package javafoundations;
import java.util.*;
import java.io.*;

public class AdjMatGraphPlus<T> implements GraphPlus<T>
{
  private final int NOT_FOUND = -1;
  private final int DEFAULT_CAPACITY = 1; // Small so that we can test expand
  
  private int n;   // number of vertices in the graph
  private boolean[][] arcs;   // adjacency matrix of arcs
  private T[] vertices;   // values of vertices
  
  /******************************************************************
    Constructor. Creates an empty graph.
    ******************************************************************/
  public AdjMatGraphPlus()
  {
    n = 0;
    this.arcs = new boolean[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
    this.vertices = (T[])(new Object[DEFAULT_CAPACITY]);
  }
  
  
  /******************************************************************
    * Second constructor:
    * Creates a new graph using the data found in a .tgf file.
    If the file does not exist, a message is printed. 
    *****************************************************************/
  public AdjMatGraphPlus(String tgf_file_name) {
    //reset current graph
    vertices = (T[]) (new Object[DEFAULT_CAPACITY]); 
    arcs = new boolean[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
    n = 0;
    
    try{
      
      Scanner fileReader = new Scanner(new File(tgf_file_name));
      while (!fileReader.next().equals("#")){
        T line = (T) fileReader.next();
        addVertex(line);
      }
      
      while (fileReader.hasNext()){
        int arcVertex1 = fileReader.nextInt();
        int arcVertex2 = fileReader.nextInt();
        //System.out.println ("Arc Vertex 1: " + arcVertex1);
        //System.out.println ("Arc Vertex 2: " + arcVertex2);
        addArc(vertices[arcVertex1 -1], vertices[arcVertex2 -1]);
      }
      
    } catch (IOException ex) {
      System.out.println(" ***(T)ERROR*** The file was not found: " + ex);
    }
  }
  
  /******************************************************************
    Returns true if the graph is empty and false otherwise. 
    ******************************************************************/
  public boolean isEmpty()
  {
    return (n == 0);
  }
  
  /******************************************************************
    Returns the number of vertices in the graph.
    ******************************************************************/
  public int n()
  { return n; }
  
  /******************************************************************
    Returns the number of arcs in the graph by counting them.
    ******************************************************************/
  public int m()
  {
    int total = 0;
    
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
      if (arcs[i][j]) total++; 
    return total; 
  }
  
  /******************************************************************
    Returns true iff a directed edge exists from v1 to v2.
    ******************************************************************/
  public boolean isArc (T vertex1, T vertex2)
  { return arcs[getIndex(vertex1)][getIndex(vertex2)]; }
  
  
  /******************************************************************
    Helper. Returns true iff an arc exists between two given indices 
    ******************************************************************/
  private boolean isArc (int index1, int index2)
  {
    if (indexIsValid(index1) && indexIsValid(index2))
      return arcs[index1][index2] == true;
    else return false;
  }
  
  
  /******************************************************************
    Returns true iff an edge exists between two given vertices
    which means that two corresponding arcs exist in the graph
    ******************************************************************/
  public boolean isEdge (T vertex1, T vertex2){
    return (isArc(vertex1, vertex2) && isArc(vertex2, vertex1)); }
  
  
  /******************************************************************
    Returns true IFF the graph is undirected, that is, for every 
    pair of nodes i,j for which there is an arc, the opposite arc
    is also present in the graph.  
    ******************************************************************/
  public boolean isUndirected(){
    for (int i = 0; i < n(); i++)
      for (int j = 0; j < n(); j++)
      if (arcs[i][j])
      if (!arcs[j][i]) 
      return false;
    return true;
  };
  
  
  /******************************************************************
    Adds a vertex to the graph, expanding the capacity of the graph
    if necessary.  If the vertex already exists, it does not add it.
    ******************************************************************/
  public void addVertex (T vertex)
  {  if (getIndex(vertex) == NOT_FOUND) {
    if (n == vertices.length)
      expandCapacity();
    
    vertices[n] = vertex;
    for (int i = 0; i <= n; i++)
    {
      arcs[n][i] = false;
      arcs[i][n] = false;
    }      
    n++;
  }
  }
  
  /******************************************************************
    Helper. Creates new arrays to store the contents of the graph 
    withtwice the capacity.
    ******************************************************************/
  private void expandCapacity()
  {
    T[] largerVertices = (T[])(new Object[vertices.length*2]);
    boolean[][] largerAdjMatrix = 
      new boolean[vertices.length*2][vertices.length*2];
    
    for (int i = 0; i < n; i++)
    {
      for (int j = 0; j < n; j++)
      {
        largerAdjMatrix[i][j] = arcs[i][j];
      }
      largerVertices[i] = vertices[i];
    }
    
    vertices = largerVertices;
    arcs = largerAdjMatrix;
  }
  
  
  /******************************************************************
    Removes a single vertex with the given value from the graph.  
    Uses equals() for testing equality
    ******************************************************************/
  public void removeVertex (T vertex)
  {
    for (int i = 0; i < n; i++)
      if (vertex.equals(vertices[i]))
      removeVertex(i);
  }
  
  /******************************************************************
    Helper. Removes a vertex at the given index from the graph.   
    Note that this may affect the index values of other vertices.
    ******************************************************************/
  private void removeVertex (int index)
  {
    if (indexIsValid(index))
    {
      n--;
      
      for (int i = index; i < n; i++)
        vertices[i] = vertices[i+1];
      
      for (int i = index; i < n; i++)
        for (int j = 0; j <= n; j++)
        arcs[i][j] = arcs[i+1][j];
      
      for (int i = index; i < n; i++)
        for (int j = 0; j < n; j++)
        arcs[j][i] = arcs[j][i+1];
    }
  }
  
  /******************************************************************
    Inserts an edge between two vertices of the graph.
    If one or both vertices do not exist, ignores the addition.
    ******************************************************************/
  public void addEdge (T vertex1, T vertex2)
  {
    // getIndex will return NOT_FOUND if a vertex does not exist,
    // and the addArc calls will not insert it
    addArc (getIndex(vertex1), getIndex(vertex2));
    addArc (getIndex(vertex2), getIndex(vertex1));
  }
  
  /******************************************************************
    Inserts an arc from vertex1 to vertex2.
    If the vertices exist, else does not change the graph. 
    ******************************************************************/
  public void addArc (T vertex1, T vertex2){
    addArc (getIndex(vertex1), getIndex(vertex2));
  }
  
  /******************************************************************
    Helper. Inserts an edge between two vertices of the graph.
    ******************************************************************/
  private void addArc (int index1, int index2)
  {
    if (indexIsValid(index1) && indexIsValid(index2))
      arcs[index1][index2] = true;
  }
  
  
  /******************************************************************
    Removes an edge between two vertices of the graph.
    If one or both vertices do not exist, ignores the removal.
    ******************************************************************/
  public void removeEdge (T vertex1, T vertex2)
  {
    removeArc (getIndex(vertex1), getIndex(vertex2));
    removeArc (getIndex(vertex2), getIndex(vertex1));
  }
  
  
  /******************************************************************
    Removes an arc from vertex v1 to vertex v2,
    if the vertices exist, else does not change the graph. 
    ******************************************************************/
  public void removeArc (T vertex1, T vertex2){
    removeArc (getIndex(vertex1), getIndex(vertex2)); }
  
  /******************************************************************
    Helper. Removes an arc from index v1 to index v2.
    ******************************************************************/
  private void removeArc (int index1, int index2)
  {
    if (indexIsValid(index1) && indexIsValid(index2))
      arcs[index1][index2] = false;
  }
  
  
  
  /******************************************************************
    Returns the index value of the first occurrence of the vertex.
    Returns NOT_FOUND if the key is not found.
    ******************************************************************/
  private int getIndex(T vertex)
  {
    for (int i = 0; i < n; i++){
      if (vertices[i].equals(vertex)){
        return i;
      }   
    } 
    return NOT_FOUND;
  }
  
  /******************************************************************
    Returns the vertex object that is at a certain index
    ******************************************************************/
  private T getVertex(int v)
  {   return vertices[v]; 
  }
  
  /******************************************************************
    Returns true if the given index is valid. 
    ******************************************************************/
  private boolean indexIsValid(int index)
  {  return ((index < n) && (index >= 0));  
  }
  
  /******************************************************************
    Retrieve from a graph the vertices x pointing to vertex v (x->v)
    and returns them onto a linked list
    ******************************************************************/
  public LinkedList<T> getPredecessors(T vertex){
    LinkedList<T> neighbors = new LinkedList<T>();
    
    int v = getIndex(vertex); 
    
    for (int i = 0; i < n; i++)
    {
      if (arcs[i][v])  
        neighbors.add(getVertex(i)); // if T then add i to linked list
    }    
    return neighbors;    
  }
  
  /******************************************************************
    * Retrieve from a graph the vertices x following vertex v (v->x)
    and returns them onto a linked list
    ******************************************************************/
  public LinkedList<T> getSuccessors(T vertex){
    LinkedList<T> neighbors = new LinkedList<T>();
    
    int v = getIndex(vertex); 
    
    for (int i = 0; i < n; i++)
    {
      if (arcs[v][i])  
        neighbors.add(getVertex(i)); // if T then add i to linked list
    }    
    return neighbors;    
  }
  
  /******************************************************************
    Returns a string representation of the graph. 
    ******************************************************************/
  public String toString()
  {
    if (n == 0)
      return "Graph is empty";
    
    String result = new String("");
    
    result += "Arcs\n";
    result += "-----\n";
    result += "i ";
    
    for (int i = 0; i < n; i++) 
    {
      result += "" + getVertex(i);
      if (i < 10)
        result += " ";
    }
    result += "\n";
    
    for (int i = 0; i < n; i++)
    {
      result += "" + getVertex(i) + " ";
      
      for (int j = 0; j < n; j++)
      {
        if (arcs[i][j])
          result += "1 ";
        else
          result += "- "; //just empty spaaaaaace
      }
      result += "\n";
    }
    
    return result;
  }
  
  
  /******************************************************************
    * Saves the current graph into a .tgf file.
    * If it cannot save the file, a message is printed. 
    *****************************************************************/
  public void saveTGF(String tgf_file_name) {
    try {
      PrintWriter writer = new PrintWriter(new File(tgf_file_name));
      
      //prints vertices by iterating through array "vertices"
      for (int i = 0; i < n(); i++){
        if (vertices[i] == null){
          break;
        }
        else{
          writer.print((i+1) + " " + vertices[i]);
          writer.println("");
        }
      }
      writer.print("#"); // Prepare to print the edges
      writer.println("");
      
      //prints arcs by iterating through 2D array
      for (int i = 0; i < n(); i++){
        for (int j = 0; j < n(); j++){
          if (arcs[i][j] == true){
            writer.print((i+1) + " " + (j+1));
            writer.println("");
          }
        }
      }
      writer.close();
    } catch (IOException ex) {
      System.out.println("***(T)ERROR*** The file could nt be written: " + ex);
    }
  }
  
  /******************************************************************
    * Clones the graph by saving the current one on the disk 
    * as TEMP.tgf using saveTGF() and creating a new onw using the 
    * second constructor.
    * @return the new graph.
    * FEATURE: It does not try to delete the file from the disk
    *****************************************************************/
  public AdjMatGraphPlus<T> clone () 
  { 
    saveTGF("TEMP.tgf");
    AdjMatGraphPlus<T> clone = new AdjMatGraphPlus<T>("TEMP.tgf");
    return clone;
  }
  
  
  
  /******************************************************************
    * Checks if a vertex is a sink, (points to no other vertex)
    * @return true if the vertex is a sink, false if it is not.
    ******************************************************************/
  public boolean isSink (T vertex)
  {
    boolean s = true;
    LinkedList<T> neighbors = getSuccessors(vertex);//finds all of the vertices vertex points to
    s = (neighbors.size() == 0);//indicates if vertex points to nothing
    return s;
  }
  
  /******************************************************************
    * Retrieves the vertices that are sinks and 
    * @return all the sinks in a linked list
    ******************************************************************/
  public LinkedList<T> allSinks()
  {
    LinkedList<T> sinks = new LinkedList<T>();
    for(int i=0; i<n; i++){
      if(isSink(vertices[i])) sinks.add(vertices[i]);
    }
    return sinks;  
  }
  
  
  /******************************************************************
    * Checks if a vertex is a source, (no vertex points to it)
    * @return true if the vertex is a source, false if it is not
    ******************************************************************/
  public boolean isSource (T vertex)
  {
    boolean s = true;
    LinkedList<T> neighbors = getPredecessors(vertex);//gets all of the vertices that point to vertex
    s = (neighbors.size() == 0);//indicates if nothing points to vertex
    return s;
  }
  
  /******************************************************************
    * Retrieves the vertices that are sources and 
    * @return all the sources in a linked list
    ******************************************************************/
  public LinkedList<T> allSources()
  {
    LinkedList<T> sources = new LinkedList<T>();
    for(int i=0; i<n; i++){
      if(isSource(vertices[i])) sources.add(vertices[i]);
    }  
    return sources; 
  }
  
  /******************************************************************
    * Checks if a vertex is a isolated, b/c it's source and sink
    * @return true if the vertex is isolated, false if it is not
    ******************************************************************/
  public boolean isIsolated (T vertex)
  {
    return isSource(vertex) && isSink(vertex);
  }
  
  
  /******************************************************************
    * Topologically sort vertices of a directed acyclic graph
    * using one of the two algorithms presented in class. 
    * PREREQUISITE: The input graph must be a DAG, i.e., have NO CYCLES.
    * KNOWN BUG: It will get into an infinite loop if the graph has a cycle
    * @return the topologically sorted vertices in a linked list
    ******************************************************************/
  public LinkedList<T> topologicalSort()
  {
    AdjMatGraphPlus<T> graph = clone();//creates clone of graph in order to preserve the original graphs structure
    LinkedList<T> sorted = new LinkedList<T>();
    int size = graph.n();
    while(size > 0){
      T vertex = (T) new Object();
      for(int i=0; i<graph.n(); i++){//finds vertex without a successor
        if(graph.isSource(graph.getVertex(i))){
          vertex = graph.getVertex(i);
        }
      }
      sorted.add(vertex);//adds vertex to sorted list
      graph.removeVertex(vertex);  //removes vertex from cloned graph
      size = graph.n();//adjusts for smaller size
    }  
    return sorted;
    
  }
  
  /******************************************************************
    * Returns a LinkedList contining a DEPTH first search traversal 
    * starting at the given index. If the index is not valid, 
    * it returns an empty list
    * @return a linked list with the vertices in depth-first order
    *****************************************************************/
  public LinkedList<T> DFS(T vertex)
  { 
    AdjMatGraphPlus<T> graph = clone();//clone graph preserves properties of graph
    T current = vertex;
    java.util.Stack<T> stk = new java.util.Stack<T>();
    stk.push(current);
    graph.removeVertex(current);
    LinkedList<T> sorted = new LinkedList<T>();
    while(!stk.isEmpty()){
      current = stk.peek();  
      LinkedList<T> neighbors = this.getSuccessors(current);//gets all adjacent nodes
      T neighbor = null;
      for(int i=0; i<neighbors.size(); i++){
        if(graph.getIndex(neighbors.get(i)) != NOT_FOUND){
          neighbor = neighbors.get(i);
          break;
        }
      }
      if(neighbor == null){//no more paths from current node
        sorted.add(stk.pop());//add current node to sorted list; pop from stack
      }
      else{
        stk.push(neighbor);//add a neighbor to stack
        graph.removeVertex(neighbor);//remove from graph to avoid duplications
      }
    }  
    return sorted;
    
    
  }
  
  
  /******************************************************************
    * Returns a LinkedList contining a BREADTH first search traversal 
    * starting at the given index. If the index is not valid, 
    * it returns an empty list
    * @return a linked list with the vertices in breadth-first order
    *****************************************************************/
   public LinkedList<T> BFS(T vertex)
  {
    
    int currentVertex;
    LinkedQueue<T> traversalQueue = new LinkedQueue<T>();
    LinkedList<T> resultList = new LinkedList<T>();
    int startIndex = this.getIndex(vertex);
    
    if (startIndex == -1)
      return resultList;
    
    boolean[] visited = new boolean[this.n];
    for (int x = 0; x < this.n; x++)
      visited[x] = false;
    System.out.println("Setting visited to false at all nodes...");
    
    traversalQueue.enqueue(this.getVertex(startIndex));
    visited[startIndex] = true;
    
    while (!traversalQueue.isEmpty()) { 
      currentVertex = this.getIndex(traversalQueue.dequeue());
      resultList.add(vertices[currentVertex]);
      for (int i = 0; i < this.n; i++) {
        if (this.arcs[currentVertex][i] && !visited[i]) { 
          System.out.println("Enqueuing vertex...");
          traversalQueue.enqueue(this.getVertex(i)); 
          visited[i] = true;
          
        } 
      }
    }
    
    return resultList;
  }
  
  /******************************************************************
    Testing program. 
    ******************************************************************/
  
  public static void main (String args[]){
    
    // Testing for Midterm 3
    
    AdjMatGraphPlus<String> tree = new AdjMatGraphPlus<String>("tree.tgf");
    System.out.println("Sinks in tree: \n Expected: [8, 9, 10, 11, 12, 13, 14, 15] \n Result: " + tree.allSinks());
    System.out.println("Sources in tree: \n Expected: [1] \n Result: " + tree.allSources());
    System.out.println("Topological Sort of tree: \n Expected: [1, 3, 7, 15, 14, 6, 13, 12, 2, 5, 11, 10, 4, 9, 8]  ");
    System.out.println("Result: " + tree.topologicalSort()+ "\n");
    System.out.println("DFS of tree from vertex 1: \n Expected: [8, 9, 4, 10, 11, 5, 2, 12, 13, 6, 14, 15, 7, 3, 1] ");
    System.out.println("Result: " + tree.DFS("1") + "\n");
    System.out.println("BFS of tree from vertex 1: \n Expected: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15] ");
    System.out.println("Result: " + tree.BFS("1") + "\n");
    System.out.println();
    
    AdjMatGraphPlus<String> karate = new AdjMatGraphPlus<String>("karate.tgf");
    System.out.println("Sinks in karate: \n Expected: [1, 15, 16, 19, 21, 23, 24, 25, 27] \n Result: " + karate.allSinks() + "\n");
    System.out.println("Sources in karate: \n Expected: [8, 11, 12, 13, 17, 18, 22, 34] \n Result: " + karate.allSources() + "\n");
    System.out.println("Topological Sort of karate:  \n Expected:  [34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]");
    System.out.println("Result: " + karate.topologicalSort() + "\n");
    System.out.println("DFS of karate from vertex 1: \n Expected: [1]");
    System.out.println("Result: " + karate.DFS("1") + "\n");
    System.out.println("BFS of karate from vertex 1: \n Expected: [1]");
    System.out.println("Result: " + karate.BFS("1") + "\n");
    System.out.println("DFS of karate from vertex 34: \n Expected: [1, 2, 3, 9, 10, 4, 14, 15, 16, 19, 20, 21, 23, 24, 27, 25, 28, 29, 30, 31, 26, 32, 33, 34]");
    System.out.println("Result: " + karate.DFS("34") + "\n");
    System.out.println("BFS of karate from vertex 34: \n Expected: [34, 9, 10, 14, 15, 16, 19, 20, 21, 23, 24, 27, 28, 29, 30, 31, 32, 33, 1, 3, 2, 4, 25, 26]");
    System.out.println("Result: " + karate.BFS("34") + "\n");
    System.out.println("DFS of karate from vertex 15: \n Expected: [15] ");
    System.out.println("Result: " + karate.DFS("15") + "\n");
    System.out.println("BFS of karate from vertex 15: \n Expected: [15]");
    System.out.println("Result: " + karate.BFS("15") + "\n");
    System.out.println("DFS of karate from vertex 17: \n Expected: [1, 6, 5, 7, 17] ");
    System.out.println("Result: " + karate.DFS("17") + "\n");
    System.out.println("BFS of karate from vertex 17: \n Expected: [17, 6, 7, 1, 5]");
    System.out.println("Result: " + karate.BFS("17"));
    System.out.println();
    
    AdjMatGraphPlus<String> cs_courses = new AdjMatGraphPlus<String>("cs-courses.tgf");
    System.out.println("Sinks in cs_courses: \n Expected: [CS232, CS231, CS240, CS251, CS242, CS315] \n Result: " + cs_courses.allSinks()+ "\n");
    System.out.println("Sources in cs_courses: \n Expected: [MATH225, CS110, CS114, CS111] \n Result: " + cs_courses.allSources()+ "\n");
    System.out.println("Topological Sort of cs_courses:");
    System.out.println("Expected: [CS111, CS114, CS240, CS230, CS242, CS110, CS215, CS315, MATH225, CS235, CS251, CS231, CS232] " );
    System.out.println("Result: " + cs_courses.topologicalSort()+ "\n");
    System.out.println("DFS of cs_courses from vertex CS230: \n Expected: [CS232, CS231, CS251, CS235, CS242, CS315, CS230]");
    System.out.println("Result: " + cs_courses.DFS("CS230")+ "\n");
    System.out.println("BFS of cs_coursesfrom vertex CS230: \n Expected: [CS230, CS232, CS231, CS235, CS251, CS242, CS315] ");
    System.out.println("Result: " + cs_courses.BFS("CS230"));
    System.out.println();
    
    AdjMatGraphPlus<String> wheel = new AdjMatGraphPlus<String>("Wheel.tgf");
    System.out.println("Sinks in wheel: \n Expected: [] \n Result: " + wheel.allSinks()+ "\n");
    System.out.println("Sources in wheel:\n Expected: [1] \n Result: " + wheel.allSources()+ "\n");
    //System.out.println("Topological Sort of wheel: " + wheel.topologicalSort()); 
    //above line will not work because wheel is not a DAG
    System.out.println("DFS of wheel from vertex 1: ");
    System.out.println("Expected: [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]");
    System.out.println("Result: " + wheel.DFS("1") + "\n");
    System.out.println("BFS of wheel from vertex 1: ");
    System.out.println("Expected: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]");
    System.out.println("Result: " + wheel.BFS("1"));
    
    
    
    
  }
}
