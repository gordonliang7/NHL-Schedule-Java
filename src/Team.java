import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Team {
    private String name;
    private LocalDate lastGameDate;
    private String lastGameLocation;
    private int homeStand = 0;
    private int roadTrip = 0;
    private int gamesRemaining = 82;
    private int gameStreak = 0;
    private ArrayList<Game> scheduledGames;

    private Genie genie = Genie.getInstance();
    public Team(String teamName) {
        name = teamName;
        lastGameDate = LocalDate.MIN;
        lastGameLocation = teamName;
        scheduledGames = new ArrayList<>();
    }

    @Override
    public String toString() {
        return (name);
    }
    private void alterStats(String lastloc_up, LocalDate lastdate_up,
                            int homestand_up, int roadtrip_up,
                            int gamesremaining_up, int gamestreak_up,
                            ArrayList<Game> scheduledGamesLst) {
        lastGameLocation = lastloc_up;
        lastGameDate = lastdate_up;
        homeStand = homestand_up;
        roadTrip = roadtrip_up;
        gamesRemaining = gamesremaining_up;
        gameStreak = gamestreak_up;
        scheduledGames = scheduledGamesLst;
    }
    public Team copy() {
        Team newTeam = new Team(name);
        newTeam.alterStats(lastGameLocation, lastGameDate,
                homeStand, roadTrip, gamesRemaining, gameStreak,
                scheduledGames);
        return newTeam;
    }

    private double schedule(Game game) {
        // Assert lastGameDate <= game.date
        double distance = genie.getDistance(lastGameLocation, game.home.toString());
        int daysBetween = (int) (3 - Math.min(3, ChronoUnit.DAYS.between(game.getDate(), lastGameDate)));
        double cost = Math.pow(distance, 1 + (daysBetween*.1));
        scheduledGames.add(game);
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
        Game recentGame = null;
        for (Game done_game: scheduledGames) {
            LocalDate game_date = done_game.getDate();
            if (recentGame == null) {
                recentGame = done_game;
            }
            else if (game_date.isAfter(recentGame.getDate())) {
                recentGame = done_game;
            }
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
        if (scheduledGames.size() == 0) {
            return true;
        }
        return !(recentGame.home.equals(game.home) && recentGame.away.equals(game.away));
    }

    public int getGamesRemaining() {
        return gamesRemaining;
    }

}
