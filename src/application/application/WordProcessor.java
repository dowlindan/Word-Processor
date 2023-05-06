package application;
	
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Font;


public class WordProcessor extends Application 
{
	private FileChooser chooser;
	private File selectedFile;
	private TextArea content;
	private Stage primaryStage;
	
	private boolean ctrlHeld;
	
	public void start(Stage primaryStage) throws FileNotFoundException 
	{
		this.primaryStage = primaryStage;
		chooser = new FileChooser();
		Button save = new Button("Save");
		save.setOnAction(this::processButtonPress);
		save.setTooltip(new Tooltip("Saves the file that is currently being modified."));
				
		content = new TextArea("Select a file");
		
		ScrollPane pane = new ScrollPane(content);
		
		SplitPane root = new SplitPane();
		
		root.getItems().addAll(save, pane);
		
		Scene scene = new Scene(root);
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			public void handle(KeyEvent event)
			{
				System.out.println(event.getCode());
				if (event.getCode().toString().equals("CONTROL"))
					ctrlHeld = true;
				else
				{
					if (ctrlHeld)
					{
						if (event.getCode().toString().equals("EQUALS"))
							content.setFont(new Font(content.getFont().getSize()+1));
						else if (event.getCode().toString().equals("MINUS"))
							content.setFont(new Font(content.getFont().getSize()-1));
					}
				}
			}
		});
		
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>()
		{
			public void handle(KeyEvent event)
			{
				if (event.getCode().toString().equals("CONTROL"))
					ctrlHeld = false;
			}
		});
		
		scene.setOnScroll((ScrollEvent event) ->
		{
			if (ctrlHeld)
			{
				double zoomFactor = 0.1*event.getDeltaY();
				content.setFont(new Font(content.getFont().getSize()+zoomFactor));
			}
		});
		
		primaryStage.setTitle("Word Processor");
        primaryStage.setScene(scene);
        primaryStage.show();
        
		selectedFile = chooser.showOpenDialog(primaryStage);
		if (selectedFile == null)
            content.setText("No file chosen.");
        else
        {
        	try 
        	{
	            Scanner scan = new Scanner(selectedFile);
	            
	            String info = "";
	            while (scan.hasNext())
	                info += scan.nextLine() + "\n";
	            
	            content.setText(info);
	            scan.close();
        	}
        	catch (Exception e) {}
        }
		
		
	}
	

	public void processButtonPress(ActionEvent event)
    {
		try 
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(chooser.showSaveDialog(primaryStage)));
			out.write(content.getText() + "\n"); 
			out.close();
		}
		catch (IOException e) {}
    }
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}
