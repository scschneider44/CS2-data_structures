package edu.caltech.cs2.project08.bots;

import edu.caltech.cs2.project08.game.Board;
import edu.caltech.cs2.project08.game.Evaluator;
import edu.caltech.cs2.project08.game.Move;

import java.util.concurrent.ForkJoinPool;

public class MinimaxSearcher<B extends Board> extends AbstractSearcher<B> {
    public static final ForkJoinPool POOL = new ForkJoinPool();

    @Override
    public Move getBestMove(B board, int myTime, int opTime) {
        BestMove best = minimax(this.evaluator, board, ply);
        return best.move;
    }

    private static <B extends Board> BestMove minimax(Evaluator<B> evaluator, B board, int depth) {
        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        for (Move move : board.getMoves()) {
            board.makeMove(move);
            int value = -minimaxHelper(evaluator, board, depth-1);
            board.undoMove();
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }
        return new BestMove(bestMove, bestValue);
    }
    private static <B extends Board> int minimaxHelper(Evaluator<B> evaluator, B board, int depth) {
        if (depth == 0) {
            return evaluator.eval(board);
        }

        int bestValue = Integer.MIN_VALUE;
        for (Move move : board.getMoves()) {
            board.makeMove(move);
            int value = -minimaxHelper(evaluator, board, depth-1);
            board.undoMove();
            if (value > bestValue) {
                bestValue = value;
            }
        }
        return bestValue;
    }
}