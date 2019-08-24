package edu.caltech.cs2.project08.bots;

import edu.caltech.cs2.project08.game.Board;
import edu.caltech.cs2.project08.game.Evaluator;
import edu.caltech.cs2.project08.game.Move;

public class AlphaBetaSearcher<B extends Board> extends AbstractSearcher<B> {
    @Override
    public Move getBestMove(B board, int myTime, int opTime) {
        BestMove best = alphaBeta(this.evaluator, board, ply);
        return best.move;
    }

    private static <B extends Board> BestMove alphaBeta(Evaluator<B> evaluator, B board, int depth){
        Move bestMove = null;
        int alpha = Integer.MIN_VALUE+1;
        int beta = Integer.MAX_VALUE;
        for (Move move : board.getMoves()) {
            board.makeMove(move);
            int value = -alphaBetaHelper(evaluator, board, depth-1, -beta, -alpha);
            board.undoMove();
            if (value > alpha) {
                alpha = value;
                bestMove = move;
            }
            if (alpha >= beta) {
                alpha = value;
                bestMove = move;
            }
        }
        return new BestMove(bestMove, alpha);
    }

    private static <B extends Board> int alphaBetaHelper(Evaluator<B> evaluator, B board, int depth, int alpha, int beta) {
        if (depth == 0) {
            return evaluator.eval(board);
        }

        for (Move move : board.getMoves()) {
            board.makeMove(move);
            int value = -alphaBetaHelper(evaluator, board, depth-1, -beta, -alpha);
            board.undoMove();
            if (value > alpha) {
                alpha = value;
            }
            if (alpha >= beta) {
                return alpha;
            }
        }
        return alpha;
    }
}