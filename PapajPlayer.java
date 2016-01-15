/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.papajplayer;

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

    private Random random=new Random(0xdeadbeef);

    @Override
    public String getName() {
        return "Sebastian Pawlak 117269 Piotr Kurzawa 117245";
    }

    @Override
    public Move nextMove(Board b) {
        List<Move> moves = b.getMovesFor(getColor());         
        
        return moves.get(0);
    }
    
    private Integer getScore(Board b){
        Integer result=0;
        for (int i=0;i<b.getSize();i++)
            for (int j=0;j<b.getSize();j++){
                if (b.getState(i, j)==getOpponent(getColor())){
                    result--;
                }
                if (b.getState(i, j)==getColor()){
                    result++;
                }
            }
        return result;
    }
}
