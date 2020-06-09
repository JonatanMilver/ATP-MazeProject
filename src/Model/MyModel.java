package Model;


import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import algorithms.mazeGenerators.Maze;
import Server.ServerStrategyGenerateMaze;
import Client.IClientStrategy;

import java.io.*;
import java.net.InetAddress;

public class MyModel implements IModel {


    @Override
    public Maze generateMaze(int rows, int columns) {
        Server server = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        server.start();
        final Maze[] returnedMaze = new Maze[1];
        try{
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inputStream, OutputStream outputStream){
                    try{
                        ObjectOutputStream toServer = new ObjectOutputStream(outputStream);
                        ObjectInputStream fromServer = new ObjectInputStream(inputStream);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, columns};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[998013 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        Maze maze = new Maze(decompressedMaze);
                       // maze.print();
                        returnedMaze[0] = maze;
                    }
                    catch(IOException | ClassNotFoundException e){
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        server.stop();
        return returnedMaze[0];

    }



    @Override
    public void solveMaze(Maze maze) {

    }
}
