/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.papajplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;


public class PapajPlayer extends Player {

    private Random random = new Random(0xdeadbeef);

    @Override
    public String getName() {
        return "Sebastian Pawlak 117269 Piotr Kurzawa 117245";
    }

    @Override
    public Move nextMove(Board b) {
        long startTime = System.currentTimeMillis();        
        Board c = b.clone();
        LinkedList<TreeNode> nextNodes = new LinkedList<>();
        TreeNode tree = new TreeNode(new Node(c, true, null));
        nextNodes.addLast(tree);
        return getBestMove(nextNodes, startTime);
    }

    private Move getBestMove(LinkedList<TreeNode> nextNodes, long startTime) {
        Integer max = -1000;

        Move bestMove = ((Node) nextNodes.get(0).getUserObject()).board.getMovesFor(getColor()).get(0);        
        while (!nextNodes.isEmpty() && ((System.currentTimeMillis() - startTime)<4000)) { //(getTime() - startTime) < 200)
//            System.out.println(getTime() - startTime);            
            if (((Node) nextNodes.getFirst().getUserObject()).player) {                
                //--------------------------------------- player move --------------------------------------------
                if ((((Node) nextNodes.getFirst().getUserObject()).score > max)
                        && (((Node) nextNodes.getFirst().getUserObject()).rootMove != null)) {
                    bestMove = ((Node) nextNodes.getFirst().getUserObject()).rootMove;
                    max = ((Node) nextNodes.getFirst().getUserObject()).score;
                }

                nextChild(nextNodes, getColor(),false,  new Comparator<Node>() {
                    @Override
                    public int compare(Node node2, Node node1) {
                        return node1.score.compareTo(node2.score);
                    }
                });
            } else {                
                //--------------------------------------- enemy move --------------------------------------------
                nextChild(nextNodes, getOpponent(getColor()),true , new Comparator<Node>() {
                    @Override
                    public int compare(Node node1, Node node2) {
                        return node1.score.compareTo(node2.score);
                    }
                });
            }
        }

        return bestMove;
    }

    private void nextChild(LinkedList<TreeNode> nextNodes, Color color, Boolean isPlayer, Comparator comp ) {
        List<Move> moves = ((Node) nextNodes.getFirst().getUserObject()).board.getMovesFor(color);
        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < moves.size(); i++) {
            Board boardTemp = ((Node) nextNodes.getFirst().getUserObject()).board.clone();
            boardTemp.doMove(moves.get(i));
            if (((Node) nextNodes.getFirst().getUserObject()).rootMove == null) {
                nodes.add(new Node(boardTemp, isPlayer, moves.get(i)));
            } else {
                nodes.add(new Node(boardTemp, isPlayer, ((Node) nextNodes.getFirst().getUserObject()).rootMove));
            }
        }       
        Collections.sort(nodes,comp);
//        nodes.sort(comp);
        int first = nodes.get(0).score;        
        for (Node x : nodes) {
            if (x.score != first) {
                break;
            }            
            nextNodes.getFirst().addChildren(x);
            nextNodes.addLast(new TreeNode(x, nextNodes.getFirst()));
        }
        nextNodes.removeFirst();
    }

    private Integer calculateScore(Board b) {
        Integer result = 0;
        for (int i = 0; i < b.getSize(); i++) {
            for (int j = 0; j < b.getSize(); j++) {
                if (b.getState(i, j) == getOpponent(getColor())) {
                    result--;
                }
                if (b.getState(i, j) == getColor()) {
                    result++;
                }
            }
        }
        return result;
    }

    public class Node implements Comparable<Node> {

        private Board board;
        private Integer score;
        private Boolean player;
        private Move rootMove;

        private Node() {
        }

        public Node(Board b, Boolean player, Move rootMove) {
            board = b;
            score = calculateScore(b);
            this.player = player;
            this.rootMove = rootMove;
        }

        @Override
        public int compareTo(Node n) {
            return score.compareTo(n.score);
        }

        Board getBoard() {
            return board;
        }

        Integer getScore() {
            return score;
        }
    }

    public class TreeNode {

        private TreeNode parent;
        private ArrayList<TreeNode> children = new ArrayList<>();
        private Node m_userData;

        public TreeNode() {
        }

        public TreeNode(Node userObject) {
            m_userData = userObject;
        }

        public TreeNode(Node userObject, TreeNode parent) {
            m_userData = userObject;
            this.parent = parent;
        }

        public TreeNode getParent() {
            return parent;
        }

        public void setParrent(TreeNode node) {
            parent = node;
        }

        public boolean isRoot() {
            if (parent == null) {
                return true;
            } else {
                return false;
            }
        }

        public ArrayList<TreeNode> getChildren() {
            return children;
        }

        public boolean hasChildren() {
            return !children.isEmpty();
        }

        public int depth() {
            int depth = recurseDepth(parent, 0);
            return depth;
        }

        private int recurseDepth(TreeNode node, int depth) {
            if (node == null) {
                return depth;
            } else {
                return recurseDepth(node.parent, depth + 1);
            }
        }

        public void setUserObject(Node userObject) {
            m_userData = userObject;
        }

        public Object getUserObject() {
            return m_userData;
        }

        public void addChildren(TreeNode node) {
            children.add(node);
        }

        public void addChildren(Node userObject) {
            children.add(new TreeNode(userObject, this));
        }
    }
}
