import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

import static com.google.common.truth.Truth.assertThat;

public class TestGame {
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
        Random random = new Random();
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
        Random random = new Random();
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
        Random random = new Random();
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
        Random random = new Random();
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
}
