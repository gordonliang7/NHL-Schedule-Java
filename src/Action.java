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

    @Override
    public int hashCode() {
        switch (representation()) {
            case "GAME":
                return attachedGame.hashCode();
            case "ADVANCE DAY WITHOUT PENALTY":
                return -1;
            case "ADVANCE DAY":
                return -7;
            default: // Not really possible but sure
                return 0;
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) ||!(o instanceof Action)) {
            return false;
        }
        return this.hashCode() == o.hashCode();
    }

    public static Action advanceDay(boolean withoutPenalty) {
        return new Action(null, withoutPenalty);
    }

    public static Action gameAction(Game game) {
        return new Action(game, false);
    }
}
