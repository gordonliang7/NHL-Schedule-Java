import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Schedule {
    private ArrayList<Game> toSchedule;
    private HashSet<Game> scheduled;
    private LocalDate endOfSeason;
    private LocalDate currentDate;
    private HashMap<String, Team> teams;
    ArrayList<LocalDate> mandatoryOffDays;
    private double totalCost = 0;

    public Schedule(ArrayList<Game> toSchedule,
                    LocalDate sznEnd, LocalDate date,
                    ArrayList<Team> teamLst, ArrayList<LocalDate> mandatory_offs) {
        this.toSchedule = toSchedule;
        endOfSeason = sznEnd;
        currentDate = date;
        teams = new HashMap<String, Team>();
        for (Team t : teamLst) {
            teams.put(t.toString(), t);
        }
        mandatoryOffDays = mandatory_offs;
    }

    public void addCost(double added_cost) {
        totalCost += added_cost;
    }

    public Schedule copy() {
        ArrayList<Game> schedule_copy = new ArrayList<Game>();
        ArrayList<Team> teams_copy = new ArrayList<Team>();
        for (Game i : toSchedule) {
            schedule_copy.add(i.copy());
        }
        for (Team t : teams.values()) {
            teams_copy.add(t.copy());
        }
        Schedule newSched = new Schedule(schedule_copy, endOfSeason, currentDate,
                teams_copy, mandatoryOffDays);
        newSched.addCost(totalCost);
        return newSched;
    }

    public double markGame(Game game) {
        assert toSchedule.remove(game); // assert this is true
        game.mark(currentDate);
        scheduled.add(game);
        double cost1 = teams.get(game.home.toString()).scheduleHome(game);
        double cost2 = teams.get(game.away.toString()).scheduleAway(game);
        return cost1 + cost2;
    }

    private int advanceDay() {
        int gamesToday = 0;
        for (Team t : teams.values()) {
            gamesToday += t.advanceDay(currentDate);
        }
        currentDate.plusDays(1);
        return gamesToday;
    }

    public Schedule getSuccessor(Action action) {
        Schedule successor = this.copy();
        double cost = 0;
        if (action.isAdvanceDay) {
            int totalGames = successor.advanceDay();
            if (!action.withoutPenalty) {
                cost += Math.pow(1500, 1 - (0.1 * totalGames));
            }
        } else {
            cost += successor.markGame(action.getGame());
        }
        successor.addCost(cost);
        return successor;
    }

    private boolean bothValid(Game game) {
        return teams.get(game.home).valid(game, currentDate) && teams.get(game.away).valid(game, currentDate);
    }

    public ArrayList<Action> getLegalActions() {
        ArrayList<Action> legalActions = new ArrayList<Action>();
        if (mandatoryOffDays.contains(currentDate)) {
            legalActions.add(Action.advanceDay(true));
            return legalActions;
        }
        int daysLeft = (int) ChronoUnit.DAYS.between(endOfSeason, currentDate);
        for (Team t : teams.values()) {
            if (daysLeft < (2 * t.getGamesRemaining() / 3)) {
                return legalActions;
            }
        }
        legalActions.add(Action.advanceDay(false));
        for (Game game : toSchedule) {
            if (bothValid(game)) {
                legalActions.add(Action.gameAction(game));
            }
        }
        return legalActions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || !(o instanceof Schedule)) {
            return false;
        }
        return this.scheduled.equals((HashSet<Game>) ((Schedule) o).scheduled);
    }

    public boolean isGoalState() {
        return toSchedule.size() == 0;
    }
}
