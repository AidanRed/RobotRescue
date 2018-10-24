import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AStar
{
    public static class Point
    {
        public int x;
        public int y;

        public Point(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }

    public static class PointPriority
    {
        public Point point;
        public int priority;

        public PointPriority(Point point,int priority)
        {
            this.point = point;
            this.priority = priority;
        }
    }

    public static class World
    {
        int[][] data;
        int width;
        int height;

        // The number of points in a cell for it to be solid
        static final int SOLID_THRESHOLD = 2;

        public World(int w, int h)
        {
            width = w;
            height = h;
            data = new int[width][height];
        }

        public boolean point_inside(Point point)
        {
            if((0 <= point.x && point.x < this.width) && (0 <= point.y && point.y < this.height))
            {
                return true;
            }
            return false;
        }

        // implement __getitem__ here

        public boolean is_empty(Point point)
        {
            if(data[point.y][point.x] < SOLID_THRESHOLD)
            {
                return true;
            }
            return false;
        }


        public ArrayList<Point> neighbours(Point point)
        {
            ArrayList<Point> potentialPoints = new ArrayList<Point>(4);
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

    public static int cartesian_distance(Point pos1, Point pos2)
    {
        return Math.abs(pos1.x - pos2.x) + Math.abs(pos1.y - pos2.y);
    }

    public static ArrayList<Point> astar(Point start, Point end, World world)
    {
         Comparator<PointPriority> pointPriorityComparator = new Comparator<PointPriority>() {
            @Override
            public int compare(PointPriority p1, PointPriority p2) {
                return p1.priority - p2.priority;
            }
        };
        PriorityQueue<PointPriority> looking_at = new PriorityQueue<PointPriority>(pointPriorityComparator);
        looking_at.add(new PointPriority(start,0));

        Map<Point, Point> parent = new HashMap<Point, Point>();
        parent.put(start,null);
        Map<Point, Integer> cost = new HashMap<Point, Integer>();
        cost.put(start,0);

        while(looking_at.size() != 0)
        {
            Point current = looking_at.poll().point;
            
            // check if destination reached
            if(current.x == end.x && current.y == end.y)
            {
                break;
            }
            for(Point new_point : world.neighbours(current))
            {
                int new_cost = cost.get(current) + 1;
                if(!cost.containsKey(new_point) || (new_cost < cost.get(new_point)))
                {
                    cost.put(new_point,new_cost);

                    int priority = new_cost + cartesian_distance(end,new_point);
                    looking_at.add(new PointPriority(new_point,priority));
                    parent.put(new_point,current);
                }
            }
        }
        Point point = end;
        ArrayList<Point> path = new ArrayList<Point>();
        path.add(end);

        //try part here
        boolean pathFound = true;
        while(point != start)
        {
            if(!parent.containsKey(point))
            {   
                pathFound = false;
                break;
            }
            else
            {
                point = parent.get(point);
                path.add(point);
            }
        }
        pathFound = true;

        Collections.reverse(path);
        return path;
    }
}