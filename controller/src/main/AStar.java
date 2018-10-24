import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AStar
{
    public class Point
    {
        public int x;
        public int y;
        public int priority;

        public Point(int x, int y)
        {
            this.x = x;
            this.y = y;
            this.priority = 0;
        }
    }

    public class World
    {
        int[][] data;
        int width;
        int height;
        public World(int w, int h)
        {
            width = w;
            height = h;
            data = new int[width][height];
        }

        public boolean point_inside(Point point)
        {
            if((0 <= point.x < this.width) && (0 <= point.y < this.height))
            {
                return true;
            }
            return false;
        }

        // implement __getitem__ here

        public boolean is_empty(Point point)
        {
            if(data[point.y][point.x] == 0)
            {
                return true;
            }
            return false;
        }


        public ArrayList<Point> neighbours(Point point)
        {
            potentialPoints = new ArrayList<Point>(4);
            potentialPoints.set(0,new Point(point.x-1,point.y)); 
            potentialPoints.set(1,new Point(point.x+1,point.y)); 
            potentialPoints.set(2,new Point(point.x,point.y+1)); 
            potentialPoints.set(3,new Point(point.x,point.y-1)); 

            ArrayList<Point> neighbours = new ArrayList<Point>();
            for(Point neighbour : potentialPoints)
            {
                if(point_inside(neighbour))
                {
                    if(is_empty(neighbour))
                    {
                        neighbours.add(neighbour);
                    }
                }
            }
            return neighbours;
        }
    }

    public int cartesian_distance(Point pos1, Point pos2)
    {
        return Math.abs(pos1.x - pos2.x) + abs(pos1.y - pos2.y);
    }

    public void astar(Point start, Point end, World world)
    {
         Comparator<Point> pointPriorityComparator = new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                return p1.priority - p2.priority;
            }
        };
        PriorityQueue<Point> looking_at = new PriorityQueue<Point>(pointPriorityComparator);
        looking_at.add(start);

        Map<Point, Point> parent = new HashMap<Point, Point>();
        parent.put(start,null);
        Map<Point, Int> cost = new HashMap<Point, Int>();
        cost.put(start,0);

        while(looking_at.size() != 0)
        {
            Point current = looking_at.poll();
            
            // check if destination reached
            if(current.x == end.x && current.y == end.y)
            {
                break;
            }
            for(Point new_point : world.neighbours(current))
            {
                // int new_cost = cost[current] + 1;
                if(!cost.containsKey(new_point) || (new_cost < cost.get(new_point)))
                {
                    cost.put(new_point,new_cost);

                    int priority = new_cost + cartesian_distance(end,new_point);
                    new_point.priority = priority;
                    looking_at.add(new_point);
                    parent.put(new_point,current);
                }
            }
        }
        Point point = end;
        ArrayList<Point> path = new ArrayList<Point>();
        path.add(end);

        try{
            while(point != start){
                point = parent[point];
                path.add(point);
            }
        } catch(Exception e){
            System.out.println("Failed to find path!");
            return path;
        }


        Collections.reverse(path);
        return path;
    }
}