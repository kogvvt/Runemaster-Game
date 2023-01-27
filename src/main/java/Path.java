import java.util.List;

public class Path {
    private static PathFinder pf = new PathFinder();
    private List<Point> points;

    public List<Point> points() {
        return this.points;
    }

    public Path(Actor actor, int x, int y) {
        this.points = pf.findPath(actor, new Point(actor.x, actor.y, actor.z), new Point(x, y, actor.z), 300);
    }
}