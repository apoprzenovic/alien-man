import java.io.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;
import java.net.*;
import java.util.*;

// just coppied some starter code from ChatApp from class, to not reduce redundancy

public class GameMultiplayerServer extends Application implements EventHandler<ActionEvent> {

   private static Stage stage;
   private Scene scene;
   private VBox root = null;

   // GUI components
   private TextArea taList = new TextArea();

   // socket
   private static final int SERVER_PORT = 54654;
   private List<ObjectOutputStream> nameOfWriters = new ArrayList<>();

   int clientIDCounter = 0;

   /** Main program */
   public static void main(String[] args) {
      launch(args);
   }

   /** start the server */
   @Override
   public void start(Stage _stage) {
      stage = _stage;
      stage.setTitle("Pacman Server - Arnes & Tia");
      final int WIDTH = 400;
      final int HEIGHT = 350;
      final int X = 1520;
      final int Y = 80;

      stage.setX(X);
      stage.setY(Y);
      stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
         public void handle(WindowEvent evt) {
            System.exit(0);
         }
      });

      // Set up root
      root = new VBox();

      // Put clear button in North
      HBox hbNorth = new HBox();
      hbNorth.setAlignment(Pos.CENTER);

      // Set up rootis
      taList.setDisable(true);
      root.getChildren().addAll(hbNorth, taList);
      for (Node n : root.getChildren()) {
         VBox.setMargin(n, new Insets(10));
      }

      // Set the scene and show the stage
      scene = new Scene(root, WIDTH, HEIGHT);
      stage.setScene(scene);
      stage.show();

      // Adjust size of TextArea
      taList.setPrefHeight(HEIGHT - hbNorth.getPrefHeight());

      // do Server Stuff
      Thread t = new Thread(new Runnable() {

         @Override
         public void run() {
            doServerStuff();
         }

      });
      t.start();
   }

   /** Server action */
   private void doServerStuff() {
      ServerThread st = new ServerThread();
      st.start();
   }

   // ServerThread
   class ServerThread extends Thread {

      private ServerSocket sSocket = null;
      private Boolean runServer = true;

      @Override
      public void run() {
         try {
            System.out.println("Openning SOCKET PORT");
            sSocket = new ServerSocket(SERVER_PORT);

            while (runServer) {
               System.out.println("Waiting client to connect...");
               Socket cSocket = sSocket.accept();

               ClientThread cT = new ClientThread(cSocket);
               cT.start();
            }

         } catch (IOException e) {
            showAlert(AlertType.ERROR, e.getMessage());
            runServer = false;
         } finally {
            try {
               if (sSocket != null)
                  sSocket.close();
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      }
   }

   // ClientThread
   class ClientThread extends Thread {

      private Socket cSocket;
      private ObjectOutputStream oos = null;
      private ObjectInputStream ois = null;
      private Boolean allowClientReading = true;

      public ClientThread(Socket cSocket) {
         this.cSocket = cSocket;
      }

      @Override
      public void run() {
         try {

            this.ois = new ObjectInputStream(this.cSocket.getInputStream());
            this.oos = new ObjectOutputStream(this.cSocket.getOutputStream());

            nameOfWriters.add(this.oos);
            System.out.println(nameOfWriters.size() + "helo");

            while (allowClientReading) {
               Object obj = this.ois.readObject();
               // messages in the format
               // REGISTER@NAME - register command, STRING
               // CHAT@MESSAGE - chat message, STRING
               // Status package, Status class
               if (obj instanceof String) {
                  String message = (String) obj;
                  System.out.println(message);
                  String[] arrayOfMessage = message.split("@");
                  if (arrayOfMessage.length == 2) {
                     switch (arrayOfMessage[0]) {
                        case "REGISTER":
                           System.out.println("REGISTER received");
                           oos.writeObject(clientIDCounter);
                           oos.flush();
                           clientIDCounter++;
                           taList.appendText("Client " + clientIDCounter + " CONNECTED " + "\n");
                           break;
                        case "CHAT":
                           String chatMessage = arrayOfMessage[1];
                           System.out.println(chatMessage);
                           // send to all connected clients
                           for (int i = 0; i < nameOfWriters.size(); i++) {
                              nameOfWriters.get(i).writeObject(chatMessage);
                              nameOfWriters.get(i).flush();
                           }
                           break;
                     }
                  }
               } else if (obj instanceof Status) {
                  Status newStatus = (Status) obj;
                  for (int i = 0; i < nameOfWriters.size(); i++) {
                     // send to the others, and not back to me
                     if (nameOfWriters.get(i) != this.oos) {
                        nameOfWriters.get(i).writeObject(newStatus);
                        nameOfWriters.get(i).flush();
                     }
                  }
               } else if (obj instanceof GhostStatus) {
                  GhostStatus newStatus = (GhostStatus) obj;
                  for (int i = 0; i < nameOfWriters.size(); i++) {
                     // send to the others, and not back to me
                     if (nameOfWriters.get(i) != this.oos) {
                        nameOfWriters.get(i).writeObject(newStatus);
                        nameOfWriters.get(i).flush();
                     }
                  }
               }

            }
         } catch (SocketException se) {
            System.out.println("A client disconnect from Server");
            return;
         } catch (EOFException eofe) {
            System.out.println("Client Disconnected");
            allowClientReading = false;
            return;
         } catch (ClassNotFoundException cnfe) {
            allowClientReading = false;
            cnfe.printStackTrace();
         } catch (IOException e) {
            allowClientReading = false;
            e.printStackTrace();
         } finally {
            allowClientReading = false;
            for (int i = 0; i < nameOfWriters.size(); i++) {
               try {
                  nameOfWriters.get(i).close();
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
            try {
               ois.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         } // finally end

      }

   }

   /** Button handler */
   public void handle(ActionEvent ae) {
   }

   public void showAlert(AlertType type, String message) {
      Platform.runLater(new Runnable() {
         public void run() {
            Alert alert = new Alert(type, message);
            alert.showAndWait();
         }
      });
   }

   public static Stage getServerStage() {
      return stage;
   }

}
