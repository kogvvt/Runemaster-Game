import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class PathFinder {
    private ArrayList<Point> open = new ArrayList();
    private ArrayList<Point> closed = new ArrayList();
    private HashMap<Point, Point> parents = new HashMap();
    private HashMap<Point, Integer> totalCost = new HashMap();

    public PathFinder() {
    }

    private int heuristicCost(Point from, Point to) {
        return Math.max(Math.abs(from.x - to.x), Math.abs(from.y - to.y));
    }

    private int costToGetTo(Point from) {
        return this.parents.get(from) == null ? 0 : 1 + this.costToGetTo((Point) this.parents.get(from));
    }

    private int totalCost(Point from, Point to) {
        if (this.totalCost.containsKey(from)) {
            return (Integer) this.totalCost.get(from);
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
            }

            this.checkNeighbors(actor, end, closest);
        }

        return null;
    }

    private Point getClosestPoint(Point end) {
        Point closest = (Point) this.open.get(0);
        Iterator var4 = this.open.iterator();

        while (var4.hasNext()) {
            Point other = (Point) var4.next();
            if (this.totalCost(other, end) < this.totalCost(closest, end)) {
                closest = other;
            }
        }

        return closest;
    }

    private void checkNeighbors(Actor actor, Point end, Point closest) {
        Iterator var5 = closest.getNeighbors().iterator();

        while (true) {
            Point neighbor;
            do {
                do {
                    if (!var5.hasNext()) {
                        return;
                    }

                    neighbor = (Point) var5.next();
                } while (this.closed.contains(neighbor));
            } while (!actor.canEnter(neighbor.x, neighbor.y, actor.z) && !neighbor.equals(end));

            if (this.open.contains(neighbor)) {
                this.reParentNeighborIfNecessary(closest, neighbor);
            } else {
                this.reParentNeighbor(closest, neighbor);
            }
        }
    }

    private void reParentNeighbor(Point closest, Point neighbor) {
        this.reParent(neighbor, closest);
        this.open.add(neighbor);
    }

    private void reParentNeighborIfNecessary(Point closest, Point neighbor) {
        Point originalParent = (Point) this.parents.get(neighbor);
        double currentCost = (double) this.costToGetTo(neighbor);
        this.reParent(neighbor, closest);
        double reparentCost = (double) this.costToGetTo(neighbor);
        if (reparentCost < currentCost) {
            this.open.remove(neighbor);
        } else {
            this.reParent(neighbor, originalParent);
        }

    }

    private ArrayList<Point> createPath(Point start, Point end) {
        ArrayList path;
        for (path = new ArrayList(); !end.equals(start); end = (Point) this.parents.get(end)) {
            path.add(end);
        }

        Collections.reverse(path);
        return path;
    }
}