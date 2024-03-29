/*
* Class for storing and analysing map data for autonomous navigation

 Add points in a separate thread - use queue of points to add? Properly integrate with GUI.
 Compare to last 10 points found - cluster then delete points and place a new point at average position.
 Mark clusters? Draw lines between clusters? Brasenham line algorithm to mark definitely clear cells - can spill over one by one
*/
import java.awt.Point;

import java.util.List;
import java.util.ArrayList;

import java.io.*; 

public class Navigator{
    private List<Point> points = new ArrayList<Point>();
    private List<Point> recentPoints = new ArrayList<Point>();

    ArrayList<AStar.Point> currentPath;

    AStar.World world;

    Gui gui;

    int gridSize;
    int halfGrid;
    double cellSize;

    // Values read within this distance will be clustered
    private final int MIN_DIST = 3;
    private final int MIN_DIST_SQUARED = (int)Math.pow(MIN_DIST, 2);

    public Navigator(Gui gui, int gridSize, double cellSize){
        // Starts in the bottom left corner of the grid
        this.gui = gui;
        world = new AStar.World(gridSize, gridSize); 

        this.cellSize = cellSize;
        this.gridSize = gridSize;
        this.halfGrid = halfGrid;
    }

    public List<Point> getPoints(){
        return points;
    }

    public void addPoint(Point p){
        //for(point : recentPoints){
            
        //}
        int[] gridCoords = pointToGrid(p.x, p.y);
        int gridX = gridCoords[0];
        int gridY = gridCoords[1];

        if(gridX < 0 || gridY < 0 || gridX > gridSize || gridY > gridSize){
            //Don't add point
        } else{
            /*
            if(world.data[gridX][gridY] < world.SOLID_THRESHOLD){
                points.add(p);
            }*/
            world.data[gridX][gridY] += 1;
        }
    }

    public void addDestination(int x, int y){
        int[] d = pointToGrid(x, y);
        int[] s = pointToGrid((int)gui.getRobotX()+gui.getMapWidth()/2, (int)gui.getRobotY()+gui.getMapHeight()/2);
        
        currentPath = AStar.astar(new AStar.Point(s[0], s[1]), new AStar.Point(d[0], d[1]), world);
    }

    public int[] pointToGrid(int x, int y){
        int gridX = x % gridSize;
        int gridY = y % gridSize;

        int[] gridCoords = {gridX, gridY};

        return gridCoords;
    }

    public int[] gridToPoint(int x, int y){
        int[] coords = {x*gridSize, y*gridSize};

        return coords;
    }

    public void clearPoints(){
        points.clear();
    }

    void trimPoints(){
        for(Point p : recentPoints){
            
        }
    }
    void saveData(){
        try
        {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("mapData.csv"), "UTF-8"));
            for (Point p : points)
            {
                String line = Integer.toString(p.x) + "," + Integer.toString(p.y);
                bw.write(line);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }
        catch (UnsupportedEncodingException e) {}
        catch (FileNotFoundException e){}
        catch (IOException e){}
        }
    void loadData(){
        try{
            BufferedReader br = new BufferedReader(new FileReader(new File("mapData.csv")));
            String line;
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                points.add(new Point(Integer.parseInt(data[0]), Integer.parseInt(data[1])));
            }
        }
        catch(IOException e){}
    }
}