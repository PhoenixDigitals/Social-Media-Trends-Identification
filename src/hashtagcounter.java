import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class hashtagcounter {
	
	/*Taking the input file from command line and writing the output
	 * to output_file.txt
	 */
	public void matchPattern(String inputFilename) throws IOException{
		
		//Creating the object of MaxFibonacciHeap
		MaxFibonacciHeap fh = new MaxFibonacciHeap();
		
		//For reading the input file
		FileReader fr = new FileReader(inputFilename);	
		BufferedReader br = new BufferedReader(fr);
		
		//Creating the output file
		File file = new File("output_file.txt");
		file.createNewFile();
		
		//For writing into the output file
		FileWriter fw = new FileWriter(file.getAbsolutePath());
		BufferedWriter  bw = new BufferedWriter(fw);
		
		Map<String, Node> tagMap = new HashMap<String, Node>();
		ArrayList<Node> topTags = new ArrayList<>();
		
		String line = null;
		//(.+) For any sequence
		Pattern tag = Pattern.compile("(#)(.+)(\\s)([0-9]+)");	
		//[0-9]+ For numbers
		Pattern query = Pattern.compile("[0-9]+");	
		
		while((line = br.readLine()) != null){
			Matcher tagMatch = tag.matcher(line);
			Matcher queryMatch = query.matcher(line);
			
			//For tags
			if (tagMatch.find()){	
					String tagName = tagMatch.group(2);
					int frequency = Integer.parseInt(tagMatch.group(4));
					
					//Check if same tag used earlier, if yes then use increaseKey()
					if (tagMap.containsKey(tagName)){
						fh.increaseKey(tagMap.get(tagName), frequency);	
					}
					//Else insert a new Node and add it to the HashTable
					else{
						Node n = fh.insertNode(tagName, frequency);
						tagMap.put(tagName, n);
					} 
					
			}
			//For queries
			else if (queryMatch.find()){
					int count = Integer.parseInt(queryMatch.group(0));

					//Removing max nodes
					while(count > 0){
						
						//Adding maxNodes to an ArrayList
						topTags.add(fh.removeMax());								
						count--;
					}
					
					//Reinserting max nodes
					for (Node n : topTags){
						if (topTags.indexOf(n) == topTags.size() - 1){
							bw.write(n.name + "\n");
						}
						else{
							bw.write(n.name + ",");
						}
						fh.reinsertNode(n);
					}	

					//Clearing the ArrayList after removeMax
					topTags.clear();
			}
			//For ending the processing
			else if (line.toLowerCase().equals("stop")){
				break;
			}
		}

		br.close();
		fr.close();
		bw.close();
		fw.close();
	}

	public static void main(String[] args) throws IOException {
		//Initializing the object of hashtagcounter
		hashtagcounter ht = new hashtagcounter();
		ht.matchPattern(args[0]);

	}

}
