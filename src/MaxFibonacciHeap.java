import java.util.HashMap;

public class MaxFibonacciHeap {
	Node maxNode;
	HashMap<Integer, Node> degreeTable;
	int maxTreesCount;
	
	/*Initializing the variables of MaxFibonacciHeap*/ 
	public MaxFibonacciHeap(){
		maxNode = null;
		degreeTable = new HashMap<>();
		maxTreesCount = 0;
	}
	
	/*Inserting elements into the MaxFibonacciHeap*/
	Node insertNode(String name, int key){
		Node n;
		
		//If heap is empty then insert the node as maxNode
		if (maxNode == null){		
			n = new Node(name, key);
			maxNode = n;
			maxTreesCount++;
			return maxNode;
		}
		else{
			Node temp = maxNode;
			while (temp.next != maxNode ){		
					temp = temp.next;
			}
			n = new Node(name, key);
			maxTreesCount++;
			n.next = temp.next;		
			temp.next.prev = n;
			temp.next = n;
			n.prev = temp;
			updateMaxNode(n);
			return n;

		}
	}
	
	/*Updates the maxNode*/
	void updateMaxNode(Node n){
		//If node n greater than maxNode
		//Make it the maxNode
		if(n.key > maxNode.key){		
			maxNode = n;			
		}
		
	}

	/*Increases the key of a node*/
	void increaseKey(Node n, int newKey){	
		
		n.key = newKey + n.key;		
				
		//If only a single node no need of cut
		if (n.parent == null){
			updateMaxNode(n);
			return;
		}
		
		//If increased child node greater than parent
		//then perform a cut
		if (n.key > n.parent.key){	
			cut(n);
		}
	}
	
	/*Removes a node from a tree*/
	void cut(Node n){
		
		if (n.parent.parent != null){	
			if (n.parent.childCut == false){	
				n.parent.childCut = true;
				reinsertNode(n);
			}
			//If parent.childCut == true
			else{	
				Node temp = n.parent;
				reinsertNode(n);
				cut(temp);
			}
		}
		//Height 1 node
		else{	
			reinsertNode(n);
		}
		
	}
	
	/*Reinserts a node into the MaxFibonacciHeap*/
	void reinsertNode(Node n){
		
		//For reinserting children of a node in cut() or removeMax() 
		if (n.parent != null){
			removeFromChildrenList(n);
			n.parent.degree--;
			n.parent = null;	//Recently removed node
		}

		//For reinserting a node with no parent
		maxTreesCount++;	
		Node temp = maxNode;
		while (temp.next != maxNode){
			temp = temp.next;
		}
		
		n.next = maxNode;	
		maxNode.prev = n;
		temp.next = n;
		n.prev = temp;
		updateMaxNode(n);
		
	}
	
	/*To remove a child from the children doubly circular linked list of a parent*/
	void removeFromChildrenList(Node n){
		
		//Multiple children but n is the node pointed by parent
		if(n.parent.child == n && n.next != n){	
			n.parent.child = n.next;
			//Updating other children references
			n.prev.next = n.next;
			n.next.prev = n.prev;
		}
		//The only node that is pointed by parent 
		else if (n.parent.child == n && n.next == n){
			n.parent.child = null;
		}
		//Multiple children and n is not pointed by the parent
		else {
			n.prev.next = n.next;
			n.next.prev = n.prev;
		}
	}
	
	/*Removes the maxNode of MaxFibonacciHeap*/
	Node removeMax(){	
		Node prevRoot = maxNode;
		Node temp1;
		
		//If the maxNode has only 1 child
		if (maxNode.child != null && maxNode.child.next == maxNode.child ){
			reinsertNode(maxNode.child);
		}
		//If the maxNode has multiple children
		else if (maxNode.child != null){
			Node tempChild,child;
			child = maxNode.child;
			tempChild = child.next;
			
			while (tempChild != child){
				temp1 = tempChild.next;
				reinsertNode(tempChild);
				tempChild = temp1; 
			}
			reinsertNode(tempChild);	
		}
		
		//Removing previous maxNode by removing references
		prevRoot.prev.next = prevRoot.next;	
		prevRoot.next.prev = prevRoot.prev;
		
		maxTreesCount--;	
		
		//Doing the pairwise combine
		int cnt = maxTreesCount;
		Node temp = maxNode;	
		temp = temp.next;
		while (cnt != 0){
			temp1 = temp.next;
			pairwiseCombine(temp);
			temp = temp1;
			cnt--;
		}
		findMaxNode();
		clearDegreeTable();
		return prevRoot;
	}
	
	/*Doing pairwise combine on MaxFibonacciHeap*/
	void pairwiseCombine(Node n){
		//If node with same degree not present in the degreeTable
		//then insert that node in the degreeTable
		if (degreeTable.containsKey(n.degree) == false){
				degreeTable.put(n.degree, n);
		}
		else{
				mergeHeaps(degreeTable.get(n.degree), n);
		}
	}
	
	/*Merging two nodes*/
	void mergeHeaps(Node n1, Node n2){
		if (n1.key >= n2.key){	 
			n1.degree++;
			n2.parent = n1;
			
			//Adjusting the max trees
			n2.prev.next = n2.next;		
			n2.next.prev = n2.prev;
			n2.next = n2;	
			n2.prev = n2;
			
			//If n1 does not have a child
			if (n1.child == null){
				n1.child = n2;
			}
			else{
				addToChildrenList(n2);	
			}
			
			maxTreesCount--;
			degreeTable.remove(n2.degree);
			
			//Add the combined node to the degreeTable
			if (degreeTable.containsKey(n1.degree) == false){
				degreeTable.put(n1.degree, n1);
			}
			else{
				mergeHeaps(degreeTable.get(n1.degree), n1);
			}
		}
		else{
			n2.degree++;
			n1.parent = n2;
			
			//Adjusting the max trees
			n1.prev.next = n1.next;
			n1.next.prev = n1.prev;
			n1.next = n1;
			n1.prev = n1;

			//If n1 does not have a child
			if (n2.child == null){
				n2.child = n1; 
			}
			else{
				addToChildrenList(n1);
			}	
			
			maxTreesCount--;
			degreeTable.remove(n1.degree);
			
			//Add the combined node to the degreeTable 
			if (degreeTable.containsKey(n2.degree) == false){
				degreeTable.put(n2.degree, n2);
			}
			else{
				mergeHeaps(degreeTable.get(n2.degree), n2);
			}
		}
		
	}
	
	/*To add a child to children doubly circular linked list*/
	void addToChildrenList(Node n){
		Node child = n.parent.child;
		Node temp = child.next;
	
		while (temp.next != child){
			temp = temp.next;
		}
		
		n.next = temp.next;
		temp.next.prev = n;
		temp.next = n;
		n.prev = temp;
		
	}
	
	/*Finding the maxNode in MaxFibonacciHeap after pairwiseCombine*/
	void findMaxNode(){		
		int max = -1;
		int cnt = maxTreesCount;
		Node newRoot = null;
		int i = 0;
		while (degreeTable.containsKey(i) == false){
			i++;
		}
		Node temp = degreeTable.get(i);
		temp = temp.next;
		while (cnt != 0){	
			if (temp.key > max){
				max = temp.key;
				newRoot = temp;
			}
			temp = temp.next;
			cnt--;
		}
		maxNode = newRoot;
	}
	
	/*To clear the degreeTable after pairwiseCombine*/
	void clearDegreeTable(){
		degreeTable.clear();
	}
}
