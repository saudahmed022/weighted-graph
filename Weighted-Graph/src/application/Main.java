//Saud Ahmed
//05/01/2023
//The purpose of the program is to implement weighted graph, display MST, and allow users to find the shortest path

package application;

//Library Imports
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



public class Main extends Application 
{
	//Initialize Variables
    private File selectedFile;
    private TextField startVertexField = new TextField();
    private TextField endVertexField = new TextField();
    private Button shortestPathButton = new Button("Find Shortest Path");
    Label resultTextArea = new Label("Shortest Path: ");

    @Override
    public void start(Stage primaryStage) 
    {
        
    	//Create a Label and Button to attach a file
        Label prompt = new Label("Upload your .txt file: ");
        Button browse = new Button("Browse");
        Label title = new Label("Graph Validator");
        title.setStyle("-fx-font-weight: bold");
        HBox fileHbox = new HBox(10, prompt, browse);
        
        
        //SubTitle
        Label subTitle = new Label("Plot Details");
        subTitle.setStyle("-fx-font-weight: bold");
        
        
        // create non-editable text area and display the Plot Details
        TextArea output = new TextArea();
        output.setEditable(false);
        output.setMinHeight(350);

        // set up file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Graph Data File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        // set up button action - display the contents of the file (Edges and MST info)
        browse.setOnAction(event -> {
            selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
            	output.setText(null);
            	DisplayContent(selectedFile, output);
            }
        });
        
        
        //set up button action - Find the shortest path
        shortestPathButton.setOnAction(event ->{
        	if (selectedFile != null)
        	{
        		FindShortestPath(selectedFile);
        	}
        
        });
        
        
        //Format the layout for shortest Path finder prompt
        HBox shortestPathBox = new HBox(10, new Label("Start Vertex:"), startVertexField,
        new Label("End Vertex:"), endVertexField, shortestPathButton);
        shortestPathBox.setAlignment(Pos.CENTER_LEFT);
        
        
        //Set Layout
        VBox layout = new VBox();
        layout.getChildren().addAll(title, fileHbox, subTitle, output);
        layout.getChildren().addAll(shortestPathBox, resultTextArea);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setSpacing(10);
        layout.setPadding(new Insets(10));

        // create scene and show stage
        Scene scene = new Scene(layout, 600, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void DisplayContent(File file, TextArea output) 
    {
    		//Call the load file function and Display the contents of the file
            WeightedGraph<Integer> graph = LoadFile(file);
            
            output.appendText("Number of vertices: " + graph.getSize() + "\n");
            output.appendText("Number of edges: " + graph.getNumberOfEdges() + "\n");
            output.appendText("Edges: \n");
            output.appendText(graph.getWeightedEdgesAsString());
            
            WeightedGraph.MST mst = graph.getMinimumSpanningTree(0);

			output.appendText("\nMinimum Spanning Tree:");
			output.appendText("\nTotal weight: " + mst.getTotalWeight());
			output.appendText("\nEdges: ");
			output.appendText(mst.getTreeString());
    }
    
    private void FindShortestPath(File file)
    {
    		WeightedGraph<Integer> graph = LoadFile(file);
    		
            // create graph and display details
            int startVertex = Integer.parseInt(startVertexField.getText());
            int endVertex = Integer.parseInt(endVertexField.getText());
               
            WeightedGraph<Integer>.ShortestPathTree tree1 = graph.getShortestPath(startVertex);
            List<Integer> path = tree1.getPath(graph.getIndex(endVertex));
            resultTextArea.setText(resultTextArea.getText() + String.valueOf(path));
    }
    
    
    //Loads and Puts all the information in the file
    private WeightedGraph<Integer> LoadFile(File file)
    {
    	WeightedGraph<Integer> graph = new WeightedGraph<Integer>();
        try (Scanner scanner = new Scanner(file))
        {
            int numberOfVertices = scanner.nextInt();
            scanner.nextLine(); // read the end-of-line character

            List<WeightedEdge> edges = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] triplets = line.split("[\\|]");

                for (String triplet : triplets) {
                    String[] tokens = triplet.split(",");
                    int u = Integer.parseInt(tokens[0].trim());
                    int v = Integer.parseInt(tokens[1].trim());
                    double weight = Double.parseDouble(tokens[2].trim());
                    edges.add(new WeightedEdge(u, v, weight));
                }
            }
            
            // create graph and display details
            graph = new WeightedGraph<>(edges, numberOfVertices);

        }
        catch(FileNotFoundException e)
        {
        	e.printStackTrace();
        }
        
        
		return graph; 	
    }
    
	public static void main(String[] args) {
        launch(args);
    }
}