/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.papajplayer;

import static java.lang.Math.abs;
import java.util.List;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

public class PapajPlayer extends Player {

    @Override
    public String getName() {
        return "Sebastian Pawlak 117269 Piotr Kurzawa 117245";
    }

    @Override
    public Move nextMove(Board b) {
        Board c = b.clone();
        int depth = 4;
        Node node = new Node(c, null, depth);
        Node resultNode = minMax(node, depth);
        return resultNode.getRootMove();
    }

    private Node minMax(Node node, int depth) {
        return alphaBeta(node, depth, -100, 100, true);
    }

    private Node alphaBeta(Node node, int depth, int alpha, int beta, Boolean maximizingPlayer) {
        if (depth == 0) {
            return node;
        }
        if (abs(node.getScore()) >= 50) {
            return node;
        }
        if (maximizingPlayer) {
            List<Move> moves = node.getBoard().getMovesFor(getColor());
            if (moves.isEmpty()) {
                return node;
            }
            Node maxNode = new Node(-100);
            for (Move move : moves) {
                Board c = node.getBoard().clone();
                c.doMove(move);
                Move rootMove = node.getRootMove();
                if (rootMove == null) {
                    rootMove = move;
                }
                maxNode = maxNode(maxNode, alphaBeta(new Node(c, rootMove, depth - 1), depth - 1, alpha, beta, false));
                alpha = maxInt(alpha, maxNode.getScore());
                if (beta <= alpha) {
                    break;
                }
            }
            return maxNode;
        } else {
            List<Move> moves = node.getBoard().getMovesFor(getOpponent(getColor()));
            if (moves.isEmpty()) {
                return node;
            }
            Node minNode = new Node(100);
            for (Move move : moves) {
                Board c = node.getBoard().clone();
                c.doMove(move);
                Move rootMove = node.getRootMove();
                if (rootMove == null) {
                    rootMove = move;
                }
                minNode = minNode(minNode, alphaBeta(new Node(c, rootMove, depth - 1), depth - 1, alpha, beta, true));
                beta = minInt(beta, minNode.getScore());
                if (beta <= alpha) {
                    break;
                }
            }
            return minNode;
        }
    }

    private int minInt(int v1, int v2) {
        if (v2 < v1) {
            return v2;
        }
        return v1;
    }

    private int maxInt(int v1, int v2) {
        if (v2 > v1) {
            return v2;
        }
        return v1;
    }

    private Node minNode(Node node1, Node node2) {
        if (node2.getScore() < node1.getScore()) {
            return node2;
        } else if (node2.getScore() == node1.getScore()) {
            if (node2.getDepth() < node1.getDepth()) {
                return node2;
            }
        }
        return node1;
    }

    private Node maxNode(Node node1, Node node2) {
        if (node2.getScore() > node1.getScore()) {
            return node2;
        } else if (node2.getScore() == node1.getScore()) {
            if (node2.getDepth() > node1.getDepth()) {
                return node2;
            }
        }
        return node1;
    }

    private Integer calculateScore(Board b) {
        if (getColor() == Color.PLAYER1) {
            if (b.getState(0, 0) == Color.PLAYER2) {
                return -50;
            }
            if (b.getState(7, 7) == Color.PLAYER1) {
                return 50;
            }
        } else {
            if (b.getState(7, 7) == Color.PLAYER1) {
                return -50;
            }
            if (b.getState(0, 0) == Color.PLAYER2) {
                return 50;
            }
        }
        Integer player = 0;
        Integer enemy = 0;
        for (int i = 0; i < b.getSize(); i++) {
            for (int j = 0; j < b.getSize(); j++) {
                if (b.getState(i, j) == getOpponent(getColor())) {
                    enemy++;
                    enemy++;
                }
                if (b.getState(i, j) == getColor()) {
                    player++;
                }
            }
        }
        if (enemy == 0) {
            return 50;
        }
        if (player == 0) {
            return -50;
        }
        return player - enemy;
    }

    public class Node {

        private Board board;
        private Integer score;
        private Move rootMove;
        private int depth;

        private Node() {
        }

        public Node(Integer score) {
            this.score = score;
        }

        public Node(Board b, Move rootMove, int depth) {
            board = b;
            this.depth = depth;
            score = calculateScore(b);
            this.rootMove = rootMove;
        }

        public Move getRootMove() {
            return rootMove;
        }

        Board getBoard() {
            return board;
        }

        Integer getScore() {
            return score;
        }

        int getDepth() {
            return depth;
        }
    }
}
