import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PathFinder {
    private ArrayList<Point> open = new ArrayList();
    private ArrayList<Point> closed = new ArrayList();
    private HashMap<Point, Point> parents = new HashMap();
    private HashMap<Point, Integer> totalCost = new HashMap();

    public PathFinder() {
        this.open = new ArrayList<Point>();
        this.closed = new ArrayList<Point>();
        this.parents = new HashMap<Point, Point>();
        this.totalCost = new HashMap<Point, Integer>();
    }

    private int heuristicCost(Point from, Point to) {
        return Math.max(Math.abs(from.x - to.x), Math.abs(from.y - to.y));
    }

    private int costToGetTo(Point from) {
        return this.parents.get(from) == null ? 0 : (1 + this.costToGetTo( this.parents.get(from)));
    }

    private int totalCost(Point from, Point to) {
        if (this.totalCost.containsKey(from)) {
            return this.totalCost.get(from);
        } else {
            int cost = this.costToGetTo(from) + this.heuristicCost(from, to);
            this.totalCost.put(from, cost);
            return cost;
        }
    }

    private void reParent(Point child, Point parent) {
        this.parents.put(child, parent);
        this.totalCost.remove(child);
    }

    public ArrayList<Point> findPath(Actor actor, Point start, Point end, int maxTries) {
        this.open.clear();
        this.closed.clear();
        this.parents.clear();
        this.totalCost.clear();
        this.open.add(start);

        for (int tries = 0; tries < maxTries && this.open.size() > 0; ++tries) {
            Point closest = this.getClosestPoint(end);
            this.open.remove(closest);
            this.closed.add(closest);
            if (closest.equals(end)) {
                return this.createPath(start, closest);
            }else{
                this.checkNeighbors(actor, end, closest);
            }
        }
        return null;
    }

    private Point getClosestPoint(Point end) {
        Point closest = open.get(0);
        for (Point other : open){
            if (totalCost(other, end) < totalCost(closest, end))
                closest = other;
        }
        return closest;
    }

    private void checkNeighbors(Actor actor, Point end, Point closest) {
        for (Point neighbor : closest.getNeighbors()) {
            if (closed.contains(neighbor) || !actor.canEnter(neighbor.x, neighbor.y, actor.z) && !neighbor.equals(end)) {
                continue;
            }

            if (open.contains(neighbor)) {
                reParentNeighborIfNecessary(closest, neighbor);
            } else {
                reParentNeighbor(closest, neighbor);
            }
        }
    }

    private void reParentNeighbor(Point closest, Point neighbor) {
        this.reParent(neighbor, closest);
        this.open.add(neighbor);
    }

    private void reParentNeighborIfNecessary(Point closest, Point neighbor) {
        Point originalParent = this.parents.get(neighbor);
        double currentCost = this.costToGetTo(neighbor);
        this.reParent(neighbor, closest);
        double reparentCost = this.costToGetTo(neighbor);
        if (reparentCost < currentCost) {
            this.open.remove(neighbor);
        } else {
            this.reParent(neighbor, originalParent);
        }

    }

    private ArrayList<Point> createPath(Point start, Point end) {
        ArrayList<Point> path = new ArrayList<Point>();

        while (!end.equals(start)) {
            path.add(end);
            end = parents.get(end);
        }

        Collections.reverse(path);
        return path;
    }
}