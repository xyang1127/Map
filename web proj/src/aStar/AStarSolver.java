package aStar;

import proirityQueue.ExtrinsicMinPQ;
import proirityQueue.MinPQ;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    private SolverOutcome outcome;
    private List<Vertex> path;
    private double weight;
    private int explored;
    private double time;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {

        // initialize private data structure
        outcome = null;
        path = new LinkedList<>();
        weight = 0;
        explored = 0;
        time = 0;

        HashMap<Vertex, Double> distTo = new HashMap<>();
        HashMap<Vertex, Vertex> edgeTo = new HashMap<>();

        ExtrinsicMinPQ<Vertex> pq = new MinPQ<>();
        pq.add(start, input.estimatedDistanceToGoal(start, end));
        distTo.put(start, 0.0);
        double startTime = System.currentTimeMillis();

        while(pq.size() != 0 && time <= timeout) {

            // update time
            time = (System.currentTimeMillis() - startTime)/1000;

            Vertex cur = pq.removeSmallest();
            explored++;
            if(cur.equals(end)) {
                outcome = SolverOutcome.SOLVED;

                // get the path and weight
                getPath(start, end, edgeTo);
                weight = distTo.get(end);

                return;
            }

            for(WeightedEdge<Vertex> edge : input.neighbors(cur)) {
                // relax
                Vertex to = edge.to();
                double w = edge.weight();

                if(!distTo.containsKey(to) || distTo.get(cur)+w < distTo.get(to)) {
                    distTo.put(to, distTo.get(cur)+w);
                    edgeTo.put(to, cur);

                    if(pq.contains(to))
                        pq.changePriority(to, distTo.get(to)+input.estimatedDistanceToGoal(to, end));
                    else
                        pq.add(to, distTo.get(to)+input.estimatedDistanceToGoal(to, end));
                }
            }

        }

        if(pq.size() == 0)
            outcome = SolverOutcome.UNSOLVABLE;
        else
            outcome = SolverOutcome.TIMEOUT;

        path = null;
        weight = 0;

    }

    private void getPath(Vertex start, Vertex end, HashMap<Vertex, Vertex> edgeTo) {

        Vertex iter = end;

        while(iter != start) {
            path.add(0, iter);
            iter = edgeTo.get(iter);
        }

        path.add(0, start);
    }

    @Override
    public SolverOutcome outcome() {
        return outcome;
    }

    @Override
    public List<Vertex> solution() {
        return path;
    }

    @Override
    public double solutionWeight() {
        return  weight;
    }

    @Override
    public int numStatesExplored() {
        return  explored;
    }

    @Override
    public double explorationTime() {
        return  time;
    }
}