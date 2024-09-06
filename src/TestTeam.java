import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static com.google.common.truth.Truth.assertThat;

public class TestTeam {
    public static Genie genie = Genie.getInstance();
    public static Random random = new Random();
    final static int totalTeams = genie.getTeams().length;
    public String randomTeam() {
        return genie.getTeams()[random.nextInt(totalTeams)];
    }

    public LocalDate randomDate() {
        int month = random.nextInt(10) + 1;
        int day = random.nextInt(27)+1;
        return LocalDate.of(2023, month, day);
    }

    public String randomTeam(String team) {
        String returnedTeam;
        do {
            returnedTeam = randomTeam();
        } while (team == returnedTeam);
        return returnedTeam;
    }

    public String randomTeam(ArrayList<String> teamsChosen) {
        String returnedTeam;
        do {
            returnedTeam = randomTeam();
        } while (teamsChosen.contains(returnedTeam));
        return returnedTeam;
    }


    public HashMap<String, Team> makeTeams() {
        HashMap<String, Team> returnedMap = new HashMap<>();
        for (String team: genie.getTeams()) {
            returnedMap.put(team, new Team(team));
        }
        return returnedMap;
    }
    @Test
    public void testHomeStandLimit() {
        HashMap<String, Team> teams = makeTeams();
        Team team = teams.get(randomTeam());
        LocalDate startDate = LocalDate.of(2023, 7, 8);
        ArrayList<String> teamsChosen = new ArrayList<>();
        teamsChosen.add(team.toString());
        for (int i = 0; i < 5; i++) {
            String teamSample = randomTeam(teamsChosen);
            Game addedHomeGame = new Game(team.toString(), teamSample);
            assertThat(team.valid(addedHomeGame, startDate)).isTrue();
            addedHomeGame.mark(startDate);
            team.scheduleHome(addedHomeGame);
            teamsChosen.add(teamSample);
            startDate = startDate.plusDays(3);
        }
        assertThat(team.getGamesRemaining()).isEqualTo(77);
        Game proposedGame = new Game(team.toString(), randomTeam(teamsChosen));
        assertThat(team.valid(proposedGame, startDate)).isFalse();
    }
    @Test
    public void testHomeStandMultipleTimes() {
        for (int j = 0; j < 1000; j++) {
            testHomeStandLimit();
        }
    }
    @Test
    public void testHomeStandTrueMultipleTimes() {
        for (int j = 0; j < 1000; j++) {
            testHomeStandLimitTrue();
        }
    }

    @Test
    public void testHomeStandLimitTrue() {
        HashMap<String, Team> teams = makeTeams();
        Team team = teams.get(randomTeam());
        LocalDate startDate = LocalDate.of(2023, 7, 8);
        ArrayList<String> teamsChosen = new ArrayList<>();
        teamsChosen.add(team.toString());
        for (int i = 0; i < 5; i++) {
            String teamSample = randomTeam(teamsChosen);
            if (i == 2) {
                Game addedHomeGame = new Game(teamSample, team.toString());
                assertThat(team.valid(addedHomeGame, startDate)).isTrue();
                addedHomeGame.mark(startDate);
                team.scheduleAway(addedHomeGame);
            }
            else {
                Game addedHomeGame = new Game(team.toString(), teamSample);
                assertThat(team.valid(addedHomeGame, startDate)).isTrue();
                addedHomeGame.mark(startDate);
                team.scheduleHome(addedHomeGame);
            }
            teamsChosen.add(teamSample);
            startDate = startDate.plusDays(3);
        }
        Game proposedGame = new Game(team.toString(), randomTeam(teamsChosen));
        assertThat(team.valid(proposedGame, startDate)).isTrue();
    }

    @Test
    public void noRepeats() {
        HashMap<String, Team> teams = makeTeams();
        Team team = teams.get(randomTeam());
        String opponent = randomTeam(team.toString());
        LocalDate gameDate = randomDate();
        Game game1 = new Game(team.toString(), opponent);
        game1.mark(gameDate);
        gameDate = gameDate.plusDays(10);
        team.scheduleHome(game1);
        assertThat(team.valid(new Game(team.toString(), opponent), gameDate)).isFalse();
    }

    @Test
    public void noThreepeats() {
        HashMap<String, Team> teams = makeTeams();
        Team team = teams.get(randomTeam());
        LocalDate gameDate = randomDate();
        ArrayList<String> teamsChosen = new ArrayList<>();
        teamsChosen.add(team.toString());
        int i = 0;
        do {
            String opponent = randomTeam(teamsChosen);
            Game game = new Game(team.toString(), opponent);
            game.mark(gameDate);
            teamsChosen.add(opponent);
            assertThat(team.valid(game, gameDate)).isTrue();
            team.scheduleHome(game);
            gameDate = gameDate.plusDays(1);
            i++;
        } while(i < 2);
        assertThat(team.valid(new Game(randomTeam(teamsChosen), team.toString()), gameDate)).isFalse();
    }
    @Test
    public void allowThreeInFour() {
        HashMap<String, Team> teams = makeTeams();
        Team team = teams.get(randomTeam());
        LocalDate gameDate = randomDate();
        ArrayList<String> teamsChosen = new ArrayList<>();
        teamsChosen.add(team.toString());
        int i = 0;
        do {
            String opponent = randomTeam(teamsChosen);
            Game game = new Game(team.toString(), opponent);
            game.mark(gameDate);
            teamsChosen.add(opponent);
            assertThat(team.valid(game, gameDate)).isTrue();
            team.scheduleHome(game);
            gameDate = gameDate.plusDays(1);
            i++;
        } while(i < 2);
        gameDate = gameDate.plusDays(1);
        assertThat(team.valid(new Game(randomTeam(teamsChosen), team.toString()), gameDate)).isTrue();
    }

    @Test
    public void testRepeatsAndThreepeatsMultipleTimes() {
        for (int i = 0; i < 1000; i++) {
            noThreepeats();
            noRepeats();
        }
    }
    @Test
    public void noreepeats() {
        assertThat(ChronoUnit.DAYS.between(LocalDate.of(2024,8,5), LocalDate.of(2024,8,3))).isEqualTo(-2);
    }

}
