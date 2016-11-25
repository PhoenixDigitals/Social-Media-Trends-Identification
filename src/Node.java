
public class Node{
		String name;	
		int key;
		int degree;
		Node next;
		Node prev;
		Node child;
		Node parent;
		boolean childCut;
		
		/*Initializing the variables of Node*/
		public Node(String name, int key){
			this.name = name;
			this.key = key;
			degree = 0;
			next = this;
			prev = this;
			child = null;
			parent = null;
			childCut = false;
		}		
	}
