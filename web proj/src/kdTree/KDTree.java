package kdTree;

import java.util.List;

public class KDTree implements PointSet{

    TreeNode root;

    public KDTree(List<Point> points) {

        root = null;
        // build the KD tree according to that point list
        for(Point p : points)
            add(p);

    }

    
    private void add(Point point) {
        root = add(point, root, true);
    }

    private TreeNode add(Point point, TreeNode root, boolean horizontal) {

        if(root == null)
            return new TreeNode(point);

        if(horizontal) {
            if(root.point.getX() > point.getX())
                root.left = add(point, root.left, false); // left
            else
                root.right = add(point, root.right, false); // right
        } else {
            if(root.point.getY() > point.getY())
                root.left = add(point, root.left, true); // down
            else
                root.right = add(point, root.right, true); // up
        }

        return root;

    }

    @Override
    public Point nearest(double x, double y) {

        return nearest(root, new Point(x, y), null, true);

    }

    private Point nearest(TreeNode root, Point target, Point best, boolean horizontal) {

        if(root == null)
            return best;

        if(best == null)
            best = root.point;
        else {
            if(Point.distance(target, root.point) < Point.distance(target, best))
                best = root.point;
        }

        TreeNode goodSide, badSide;

        double bestDistance = Point.distance(target, best);
        boolean skipBadSide = false;

        if(horizontal) {
            if(target.getX() < root.point.getX()) {
                goodSide = root.left;
                badSide = root.right;
            } else {
                goodSide = root.right;
                badSide = root.left;
            }

            if(bestDistance < Math.pow(target.getX() - root.point.getX(), 2))
                skipBadSide = true;
        } else {
            if(target.getY() < root.point.getY()) {
                goodSide = root.left;
                badSide = root.right;
            } else {
                goodSide = root.right;
                badSide = root.left;
            }

            if(bestDistance < Math.pow(target.getY() - root.point.getY(), 2))
                skipBadSide = true;
        }

        best = nearest(goodSide, target, best, !horizontal);
        if(!skipBadSide)
            best = nearest(badSide, target, best, !horizontal);

        return best;
    }

    class TreeNode {

        Point point;
        TreeNode left;
        TreeNode right;

        public TreeNode(Point p) {
            point = p;
        }

    }

}
