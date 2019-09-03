package server;

import aStar.streetMap.Node;
import aStar.streetMap.StreetMapGraph;
import kdTree.KDTree;
import kdTree.Point;
import trie.Trie;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    private KDTree kdTree;
    private HashMap<Point, Node> map;
    private Trie trie;
    private Map<Long, Node> idToNodeMap;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);

        // construct the kdTree for task 2
        // only consider vertices that have neighbors when calculating closest
        List<Node> nodes = this.getNodes(); // cuz this getNodes() will construct a new list every time.
        // get rid of nodes that don't have neighbors
        nodes.removeIf(n -> this.neighbors(n.id()).size() == 0);

        // construct the corresponding point of each node and build a map to relate them
        List<Point> points = new ArrayList<>(nodes.size());
        map = new HashMap<>(nodes.size());
        for (Node n : nodes) {
            Point cur = new Point(n.lon(), n.lat());
            points.add(cur);
            map.put(cur, n);
        }
        kdTree = new KDTree(points);


        // construct the trie for task 3
        List<Node> allNodes = this.getNodes();
        trie = new Trie();
        for(Node n : allNodes) {
            trie.insert(n);
        }

        // construct idToNodeMap
        idToNodeMap = new HashMap<>();
        for(Node n : allNodes)
            idToNodeMap.put(n.id(), n);

    }

    public Map<Long, Node> getMap() {
        return idToNodeMap;
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        // currently add nodes to this kd is not supported

        Point nearestPoint = kdTree.nearest(lon, lat);
        long ans = map.get(nearestPoint).id();

        return ans;
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {

        Set<String> set = new HashSet<>();
        List<Node> allNodes = trie.searchByPrefix(prefix);
        for(Node n : allNodes)
            set.add(n.name());

        List<String> ans = new ArrayList<>(set);

        return ans;

    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {

        List<Node> nodes = trie.searchByName(locationName);
        List<Map<String, Object>> ans = new LinkedList<>();

        if(nodes != null) {
            for (Node n : nodes) {
                Map<String, Object> item = new HashMap<>();

                item.put("lat", n.lat());
                item.put("lon", n.lon());
                item.put("name", n.name());
                item.put("id", n.id());

                ans.add(item);
            }
        }

        return ans;

    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
