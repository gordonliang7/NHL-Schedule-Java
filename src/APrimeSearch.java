import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

public class APrimeSearch {
    public static void main(String[] args) {
        Genie genie = Genie.getInstance();
        PriorityQueue<Schedule> order = new PriorityQueue<>();
        HashSet<Schedule> visited = new HashSet<>();
        ArrayList<Game> game_lst = loadGames();
        ArrayList<Team> teams = new ArrayList<>();
        for (String team: genie.getTeams()) {
            teams.add(new Team(team));
        }
        order.add(new Schedule(game_lst, LocalDate.of(2023,5,8),
                LocalDate.of(2022,8,1), teams, new ArrayList<LocalDate>()));
        int iterations = 0;
        while (!order.isEmpty()) {
            if (iterations%1000 == 0) {
                System.out.println(order.size());
            }
            Schedule state = order.remove();
            if (visited.contains(state)) {
                continue;
            }
            visited.add(state);
            if (state.isGoalState()) {
                System.out.println(state.toString());
                break;
            }
            HashSet<Action> legalActions = state.getLegalActions(); //TODO: Fix legal actions
            for (Action action: legalActions) {
                order.add(state.getSuccessor(action));
            }
            iterations++;
        }
    }
    public static ArrayList<Game> loadGames() {
        ArrayList<Game> initialGameList = new ArrayList<>();
        try {
            String line = "";
            String splitBy = ",";
            String game_file = "src/Resources/2023-24 NHL Scheduled Games.csv";
            BufferedReader br = new BufferedReader(new FileReader(game_file));
            br.readLine(); // Skip header
            line = br.readLine();
            while (line != null) {
                String[] line_read = line.split(splitBy);    // use comma as separator
                String homeTeam = line_read[0];
                String awayTeam = line_read[1];
                initialGameList.add(new Game(homeTeam, awayTeam));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return initialGameList;
    }
}
