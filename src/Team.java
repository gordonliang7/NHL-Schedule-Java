import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

public class Team {
    private String name;
    private LocalDate lastGameDate;
    private String lastGameLocation;
    private int homeStand = 0;
    private int roadTrip = 0;
    private int gamesRemaining = 82;
    private int gameStreak = 0;
    private Game recentGame = null;
    private LinkedList<Game> lastThree;

    private Genie genie = Genie.getInstance();
    public Team(String teamName) {
        name = teamName;
        lastGameDate = LocalDate.MIN;
        lastGameLocation = teamName;
        lastThree = new LinkedList<>();
    }

    @Override
    public String toString() {
        return (name);
    }
    private Team(String teamName, String lastloc_up, LocalDate lastdate_up,
                            int homestand_up, int roadtrip_up,
                            int gamesremaining_up, int gamestreak_up,
                            LinkedList<Game> scheduledGamesLst) {
        name = teamName;
        lastGameLocation = lastloc_up;
        lastGameDate = lastdate_up;
        homeStand = homestand_up;
        roadTrip = roadtrip_up;
        gamesRemaining = gamesremaining_up;
        gameStreak = gamestreak_up;
        lastThree = new LinkedList<>();
        for (Game game: scheduledGamesLst) {
            addGame(game);
        }
    }
    public Team copy() {
        return new Team(name, lastGameLocation, lastGameDate,
                homeStand, roadTrip, gamesRemaining, gameStreak,
                lastThree);
    }

    private void addGame(Game game) {
        lastThree.add(game);
        recentGame = game;
        while (lastThree.size() > 3) {
            lastThree.remove();
        }
    }

    private double schedule(Game game) {
        // Assert lastGameDate <= game.date
        double distance = genie.getDistance(lastGameLocation, game.home.toString());
        int daysBetween = (int) (3 - Math.min(3, ChronoUnit.DAYS.between(game.getDate(), lastGameDate)));
        double cost = Math.pow(distance, 1 + (daysBetween*.1));
        addGame(game);
        lastGameDate = game.getDate();
        lastGameLocation = game.home.toString();
        gamesRemaining--;
        return cost;
    }

    public double scheduleHome(Game game) {
        homeStand++;
        roadTrip = 0;
        return schedule(game);
    }

    public String getName(){
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Team)) {
            return false;
        }
        return ((Team) obj).getName() == name;
    }

    public double scheduleAway(Game game) {
        homeStand = 0;
        roadTrip++;
        return schedule(game);
    }

    public int advanceDay(LocalDate date) {
        if (date.isEqual(lastGameDate)) {
            gameStreak++;
            return 1;
        }
        gameStreak = 0;
        return 0;
    }

    public boolean valid(Game game, LocalDate date) {
        if (lastGameDate.isEqual(date)) { // Can't have two games in one day
            return false;
        }
        int matchupWithinWeek = 0; // Count same opponent matchups within a week
        int threeCount = 0; // Count for 3 games in 3 days
        int sevenCount = 0; // Count for more than 3 games 7 day span
        for (Game done_game: lastThree) {
            LocalDate game_date = done_game.getDate();
            if (ChronoUnit.DAYS.between(date, game_date) <= 7) {
                sevenCount++;
                if (done_game.opponent(this).equals(game.opponent(this))) {
                    matchupWithinWeek++;
                }
            }
            if (ChronoUnit.DAYS.between(date, game_date) <= 3) {
                threeCount++;
            }
        }
        if ((matchupWithinWeek > 1) || (threeCount > 1) || (sevenCount > 2)) {
            return false;
        }
        if (game.homeT(this) && (homeStand > 4)) {
            return false;
        }
        if (game.awayT(this) && (roadTrip > 4)) {
            return false;
        }
        if (recentGame == null) {
            return true;
        }
        return !(recentGame.home.equals(game.home) && recentGame.away.equals(game.away));
    }

    public int getGamesRemaining() {
        return gamesRemaining;
    }

}
