import java.time.LocalDate;

public class Game implements Comparable<Game>{
    public String home;
    public String away;
    private boolean marked;
    private int hash;

    private LocalDate date;
    private Genie genie = Genie.getInstance();
    public Game(String homeTeam, String awayTeam) {
        home = homeTeam;
        away = awayTeam;
        marked = false;
        hash = 32 * genie.teamId(homeTeam) + genie.teamId(awayTeam);
    }

    public void mark(LocalDate sched_date) {
        date = sched_date;
        hash += ((date.getDayOfMonth()*992)+(31*992*date.getMonthValue()));
        marked = true;
    }

    public boolean homeT(Team team) {
        return (home == team.toString());
    }

    public boolean awayT(Team team) {
        return (away == team.toString());
    }

    public boolean involves(Team team) {
        return (homeT(team) || awayT(team));
    }

    @Override
    public String toString() {
        return (away + " @ " + home);
    }


    @Override
    public int compareTo(Game o) {
        if ((hash % 100000) == (o.hash % 100000)) {
            return 0;
        }
        return Integer.compare(hash, o.hash);
    }

    public Game copy() {
        Game newGame = new Game(home, away);
        if (marked) {
            newGame.mark(date);
        }
        return newGame;
    }
    public String opponent(Team team) {
        if (homeT(team)) {
            return away;
        }
        if (awayT(team)) {
            return home;
        }
        return null;
    }
    public LocalDate getDate(){
        return date;
    }
}
