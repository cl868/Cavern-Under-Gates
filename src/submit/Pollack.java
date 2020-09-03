package submit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import game.FindState;
import game.Finder;
import game.Node;
import game.NodeStatus;
import game.ScramState;

/** Student solution for two methods. */
public class Pollack extends Finder {

    /** Get to the orb in as few steps as possible. <br>
     * Once you get there, you must return from the function in order to pick it up. <br>
     * If you continue to move after finding the orb rather than returning, it will not count.<br>
     * If you return from this function while not standing on top of the orb, it will count as <br>
     * a failure.
     *
     * There is no limit to how many steps you can take, but you will receive<br>
     * a score bonus multiplier for finding the orb in fewer steps.
     *
     * At every step, you know only your current tile's ID and the ID of all<br>
     * open neighbor tiles, as well as the distance to the orb at each of <br>
     * these tiles (ignoring walls and obstacles).
     *
     * In order to get information about the current state, use functions<br>
     * state.currentLoc(), state.neighbors(), and state.distanceToOrb() in FindState.<br>
     * You know you are standing on the orb when distanceToOrb() is 0.
     *
     * Use function state.moveTo(long id) in FindState to move to a neighboring<br>
     * tile by its ID. Doing this will change state to reflect your new position.
     *
     * A suggested first implementation that will always find the orb, but <br>
     * likely won't receive a large bonus multiplier, is a depth-first walk. <br>
     * Some modification is necessary to make the search better, in general. */
    @Override
    public void findOrb(FindState state) {
        // TODO 1: Get the orb

        ArrayList<Long> w= new ArrayList<>();
        dfsWalk(state, w);
    }

    /** Dfswalk helper function for findOrb. */
    public void dfsWalk(FindState s, ArrayList<Long> w) {
        if (s.distanceToOrb() == 0) {
            return;
        } else {
            long slong= s.currentLoc();
            w.add(slong);
            List<NodeStatus> neighbors= new ArrayList<>(s.neighbors());
            Collections.sort(neighbors);
            for (game.NodeStatus d : neighbors) {
                long id= d.getId();
                if (w.contains(id) == false) {
                    s.moveTo(id);
                    dfsWalk(s, w);
                    if (s.distanceToOrb() == 0) { return; }
                    s.moveTo(slong);
                }
            }
        }
    }

    /** Pres Pollack is standing at a node given by parameter state.<br>
     *
     * Get out of the cavern before the ceiling collapses, trying to collect as <br>
     * much gold as possible along the way. Your solution must ALWAYS get out <br>
     * before time runs out, and this should be prioritized above collecting gold.
     *
     * You now have access to the entire underlying graph, which can be accessed <br>
     * through parameter state. <br>
     * state.currentNode() and state.getExit() will return Node objects of interest, and <br>
     * state.allNodes() will return a collection of all nodes on the graph.
     *
     * The cavern will collapse in the number of steps given by <br>
     * state.stepsLeft(), and for each step this number is decremented by the <br>
     * weight of the edge taken. <br>
     * Use state.stepsLeft() to get the time still remaining, <br>
     * Use state.moveTo() to move to a destination node adjacent to your current node.<br>
     * Do not call state.grabGold(). Gold on a node is automatically picked up <br>
     * when the node is reached.<br>
     *
     * The method must return from this function while standing at the exit. <br>
     * Failing to do so before time runs out or returning from the wrong <br>
     * location will be considered a failed run.
     *
     * You will always have enough time to scram using the shortest path from the <br>
     * starting position to the exit, although this will not collect much gold. <br>
     * For this reason, using the shortest path method to calculate the shortest <br>
     * path to the exit is a good starting solution */

    @Override
    /** This function sorts all the given nodes in a map from the highest to lowest ratio (the
     * amount of gold to the distance it takes to move to the exit). It then moves to the tile with
     * the highest ratio using the helper function moveAlong. If it encounters a tile with no gold,
     * it moves to the exit. */
    public void scram(ScramState s) {
        List<Node> AllNodes= new LinkedList<>(s.allNodes());
        while (true) {
            Node current= s.currentNode();
            Node exit= s.getExit();
            Collections.sort(AllNodes,
                (a, b) -> ratio(b, current) - ratio(a, current));
            for (Node n : AllNodes) {
                if (n.getTile().gold() == 0) {
                    moveAlong(current, exit, s);
                    return;
                } else {
                    if (Path.pathSum(Path.shortest(current, n)) +
                        Path.pathSum(Path.shortest(n, exit)) < s.stepsLeft()) {
                        moveAlong(current, n, s);
                        break;
                    }
                }
            }
        }
    }

    /** Returns the ratio of the amount of gold to its distance from the node to the exit for each
     * tile. */
    public int ratio(Node n, Node current) {
        int gold= n.getTile().gold();
        if (gold == 0) return 0;
        int dist= Path.pathSum(Path.shortest(current, n));
        if (dist == 0) return 0;
        return 1000 * gold / dist;
    }

    /** Helper function for scram where it moves to the required node. */
    public void moveAlong(Node a, Node b, ScramState s) {
        List<Node> shortest= Path.shortest(a, b);
        for (int i= 1; i < shortest.size(); i++ ) {
            s.moveTo(shortest.get(i));
        }
    }
}