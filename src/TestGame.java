import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

import static com.google.common.truth.Truth.assertThat;

public class TestGame {
    private static Random random = new Random();
    public static Genie genie = Genie.getInstance();
    public static String[] teamList = genie.getTeams();
    final static int year = 2024;
    final static int fullSeason = 1312;
    @Test
    public void hashsetsWorkForSameUnscheduledGames() {
        ArrayList<Game> gamesStored = new ArrayList<>();
        HashSet<Game> schedule1 = new HashSet<>();
        HashSet<Game> schedule2 = new HashSet<>();

        // Test teamId method
        for (int i = 0; i < fullSeason; i++) {
            int index1 = random.nextInt(teamList.length);
            int index2;
            do {
                index2 = random.nextInt(teamList.length);
            } while (index2 == index1);
            gamesStored.add(new Game(teamList[index1], teamList[index2]));
        }
        Collections.shuffle(gamesStored);
        for (Game game: gamesStored) {
            schedule1.add(game);
        }
        Collections.shuffle(gamesStored);
        for (Game game: gamesStored) {
            schedule2.add(game);
        }
        assertThat(schedule1.equals(schedule2)).isTrue();
    }

    @Test
    public void hashsetsWorkForDiffUnscheduledGames() {
        ArrayList<Game> gamesStored = new ArrayList<>();
        ArrayList<Game> gamesStored2 = new ArrayList<>();
        HashSet<Game> schedule1 = new HashSet<>();
        HashSet<Game> schedule2 = new HashSet<>();

        // Test teamId method
        for (int i = 0; i < fullSeason; i++) {
            int index1 = random.nextInt(teamList.length);
            int index2;
            do {
                index2 = random.nextInt(teamList.length);
            } while (index2 == index1);
            gamesStored.add(new Game(teamList[index1], teamList[index2]));
            gamesStored2.add(new Game(teamList[index1], teamList[index2]));
        }
        Collections.shuffle(gamesStored);
        for (Game game: gamesStored) {
            schedule1.add(game);
        }
        Collections.shuffle(gamesStored2);
        for (Game game: gamesStored2) {
            schedule2.add(game);
        }
        assertThat(schedule1.equals(schedule2)).isTrue();
    }

    @Test
    public void hashsetsWorkForSameScheduledGames() {
        ArrayList<Game> gamesStored = new ArrayList<>();
        HashSet<Game> schedule1 = new HashSet<>();
        HashSet<Game> schedule2 = new HashSet<>();

        // Test teamId method
        for (int i = 0; i < fullSeason; i++) {
            int index1 = random.nextInt(teamList.length);
            int index2;
            do {
                index2 = random.nextInt(teamList.length);
            } while (index2 == index1);
            Game sampledGame = new Game(teamList[index1], teamList[index2]);
            int month = random.nextInt(11) + 1;
            int day = random.nextInt(28) + 1;
            sampledGame.mark(LocalDate.of(year, month, day));
            gamesStored.add(sampledGame);
        }
        Collections.shuffle(gamesStored);
        for (Game game: gamesStored) {
            schedule1.add(game);
        }
        Collections.shuffle(gamesStored);
        for (Game game: gamesStored) {
            schedule2.add(game);
        }
        assertThat(schedule1.equals(schedule2)).isTrue();
    }

    @Test
    public void hashsetsWorkForDiffScheduledGames() {
        ArrayList<Game> gamesStored = new ArrayList<>();
        ArrayList<Game> gamesStored2 = new ArrayList<>();
        HashSet<Game> schedule1 = new HashSet<>();
        HashSet<Game> schedule2 = new HashSet<>();

        // Test teamId method
        for (int i = 0; i < fullSeason; i++) {
            int index1 = random.nextInt(teamList.length);
            int index2;
            do {
                index2 = random.nextInt(teamList.length);
            } while (index2 == index1);
            int month = random.nextInt(11) + 1;
            int day = random.nextInt(28) + 1;
            LocalDate gameDate = LocalDate.of(year, month, day);
            Game sampledGame1 = new Game(teamList[index1], teamList[index2]);
            Game sampledGame2 = new Game(teamList[index1], teamList[index2]);
            sampledGame1.mark(gameDate);
            sampledGame2.mark(gameDate);
            gamesStored.add(sampledGame1);
            gamesStored2.add(sampledGame2);
        }
        Collections.shuffle(gamesStored);
        for (Game game: gamesStored) {
            schedule1.add(game);
        }
        Collections.shuffle(gamesStored2);
        for (Game game: gamesStored2) {
            schedule2.add(game);
        }
        assertThat(schedule1.equals(schedule2)).isTrue();
    }

    @Test
    public void testHomeAwayInvolved() {
        ArrayList<Game> gamesStored = new ArrayList<>();
        ArrayList<String> homeTeams = new ArrayList<>();
        ArrayList<String> awayTeams = new ArrayList<>();
        HashMap<String, Team> teamObjs = new HashMap<>();
        for (String team: teamList) {
            teamObjs.put(team, new Team(team));
        }
        for (int i = 0; i < fullSeason; i++) {
            int indexH = random.nextInt(teamList.length);
            int indexA;
            do {
                indexA = random.nextInt(teamList.length);
            } while (indexA == indexH);
            gamesStored.add(new Game(teamList[indexH], teamList[indexA]));
            homeTeams.add(teamList[indexH]);
            awayTeams.add(teamList[indexA]);
        }
        for (int i = 0; i < fullSeason; i++) {
            Team homeTeam = teamObjs.get(homeTeams.get(i));
            Team awayTeam = teamObjs.get(awayTeams.get(i));
            Game game = gamesStored.get(i);
            assertThat(game.homeT(homeTeam)).isTrue();
            assertThat(game.awayT(awayTeam)).isTrue();
            assertThat(game.homeT(awayTeam)).isFalse();
            assertThat(game.awayT(homeTeam)).isFalse();
            assertThat(game.involves(homeTeam)).isTrue();
            assertThat(game.involves(awayTeam)).isTrue();
        }
    }

    @Test
    public void comparisonTest() {
        for (int i = 0; i < fullSeason; i++) {
            int indexH = random.nextInt(teamList.length);
            int indexA;
            do {
                indexA = random.nextInt(teamList.length);
            } while (indexA == indexH);
            String homeTeam = teamList[indexH];
            String awayTeam = teamList[indexA];
            Game game1 = new Game(homeTeam, awayTeam);
            Game game2 = new Game(homeTeam, awayTeam);
            assertThat(game1.equals(game2)).isTrue();
            assertThat(game1.sameMatchup(game2)).isTrue();
            assertThat(game2.equals(game1)).isTrue();
            assertThat(game2.sameMatchup(game1)).isTrue();
            game1.mark(LocalDate.of(2023,4,5));
            assertThat(game1.equals(game2)).isFalse();
            assertThat(game1.sameMatchup(game2)).isTrue();
            assertThat(game2.equals(game1)).isFalse();
            assertThat(game2.sameMatchup(game1)).isTrue();
        }
    }

    @Test
    public void testInitialHeuristic() {
        ArrayList<Game> games1 = new ArrayList<>();
        ArrayList<Game> games2 = new ArrayList<>();
        for (String homeTeam: teamList) {
            for (String awayTeam: teamList) {
                if (homeTeam == awayTeam) {
                    continue;
                }
                games1.add(new Game(homeTeam, awayTeam));
                games2.add(new Game(homeTeam, awayTeam));
            }
        }
        for (Game game1: games1) {
            for (Game game2: games2) {
                if ((game1.home == game2.home) && (game1.away == game2.away)) {
                    assertThat(game1.hashCode()).isEqualTo(game2.hashCode());
                    continue;
                }
                assertThat(game1.hashCode()).isNotEqualTo(game2.hashCode());
            }
        }
    }
}
