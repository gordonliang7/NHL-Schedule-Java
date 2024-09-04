import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class APrimeSearch {
    public static void main(String[] args) {
        Genie genie = Genie.getInstance();
        PriorityQueue<Schedule> order = new PriorityQueue<Schedule>();
        ArrayList<Game> game_lst = new ArrayList<Game>();
        ArrayList<Team> teams = new ArrayList<Team>();
        for (String team: genie.getTeams()) {
            teams.add(new Team(team));
        }
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
                game_lst.add(new Game(homeTeam, awayTeam));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Schedule initialState = new Schedule(game_lst, LocalDate.of(2023,5,8),
                LocalDate.of(2022,8,1), teams, new ArrayList<LocalDate>());
    }
}
