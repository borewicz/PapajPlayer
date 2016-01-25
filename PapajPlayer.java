/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.papajplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.moves.*;
import put.ai.games.game.moves.ShiftMove.Direction;
import put.ai.games.game.moves.impl.MoveMoveImpl;
import put.ai.games.game.moves.impl.ShiftMoveImpl;

public class PapajPlayer extends Player {

    private Random random = new Random(0xdeadbeef);

    @Override
    public String getName() {
        return "Sebastian Pawlak 117269 Piotr Kurzawa 117245";
    }

    @Override
    public Move nextMove(Board b) {
        List<Move> moves = b.getMovesFor(getColor());
        Board c = b.clone();
        TreeNode tree = new TreeNode(new Node(c));

        return moves.get(0);
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

        private Node() {
        }

        public Node(Board b) {
            board = b;
            score = calculateScore(b);
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
        private Object m_userData;

        public TreeNode() {
        }

        public TreeNode(Object userObject) {
            m_userData = userObject;
        }

        public TreeNode(Object userObject, TreeNode parent) {
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

        public void setUserObject(Object userObject) {
            m_userData = userObject;
        }

        public Object getUserObject() {
            return m_userData;
        }

        public void addChildren(Object userObject) {
            children.add(new TreeNode(userObject,this));            
        }
    }
}
