import java.time.LocalDate;

public class TestGenie {
    public static void main(String[] args) {
        // Get the singleton instance of Genie
        Genie genie = Genie.getInstance();

        // Test teamId method
        String team1 = "ANA";
        String team2 = "BOS";
        int team1Id = genie.teamId(team1);
        int team2Id = genie.teamId(team2);

        System.out.println("Team ID for " + team1 + ": " + team1Id);
        System.out.println("Team ID for " + team2 + ": " + team2Id);
        Game tst = new Game("BOS", "ANA");
        tst.mark(LocalDate.of(2024,3,5));
        System.out.println(tst.toString());

        // Test getDistance method
        double distance = genie.getDistance(team1, team2);
        System.out.println("Distance between " + team1 + " and " + team2 + ": " + distance);

    }
}
