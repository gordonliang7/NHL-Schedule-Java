public class Action {
    public boolean isGame = false;
    private Game attachedGame;
    public boolean withoutPenalty = true;
    public boolean isAdvanceDay;
    public Action(Game game, boolean withoutPenalty) {
        if (game != null) {
            isGame = true;
            attachedGame = game;
        }
        else {
            this.withoutPenalty = withoutPenalty;
        }
        isAdvanceDay = !isGame;
    }

    public String representation() {
        if (isGame) {
            return "GAME";
        }
        if (withoutPenalty) {
            return "ADVANCE DAY WITHOUT PENALTY";
        }
        return "ADVANCE DAY";
    }
    public Game getGame() {
        return attachedGame;
    }

    public static Action advanceDay(boolean withoutPenalty) {
        return new Action(null, withoutPenalty);
    }

    public static Action gameAction(Game game) {
        return new Action(game, false);
    }
}
