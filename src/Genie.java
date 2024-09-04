import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Genie {
    private static Genie instance;
    private String[] teams = {"ANA", "ARI", "BOS", "BUF", "CAR", "CBJ", "CGY", "CHI", "COL", "DAL",
            "DET", "EDM", "FLA", "LAK", "MIN", "MTL", "NJD", "NSH", "NYI", "NYR",
            "OTT", "PHI", "PIT", "SEA", "SJS", "STL", "TBL", "TOR", "VAN", "VGK",
            "WPG", "WSH"
    };
    private String distance_file = "Resources/team_distances.csv";
    private Map<String, Integer> team_map = new HashMap<>();
    private double[][] distance_table = new double[32][32];
    private Genie() {
        for (int i = 0; i < 32; i++) {
            team_map.put(teams[i], i);
        }
        String line = "";
        String splitBy = ",";
        // Read Distance csv
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/Resources/Team Distances.csv"));
            br.readLine(); // Skip header
            line = br.readLine();
            while (line != null) {
                String[] line_read = line.split(splitBy);    // use comma as separator
                Integer team1 = team_map.get(line_read[0]);
                Integer team2 = team_map.get(line_read[1]);
                double distance = Double.valueOf(line_read[2]);
                distance_table[team1][team2] = distance;
                distance_table[team2][team1] = distance;
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Genie getInstance() {
        if (instance == null) {
            instance = new Genie();
        }
        return instance;
    }

    public int teamId(String team) {
        return (team_map.get(team));
    }

    public int teamObjId(Team team) {
        return (team_map.get(team.toString()));
    }

    public String[] getTeams() {
        return teams;
    }

    public double getDistance(String team1, String team2) {
        return (distance_table[teamId(team1)][teamId(team2)]);
    }
}
