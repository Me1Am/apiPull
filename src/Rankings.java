import java.util.Map;
import java.util.List;
import java.math.BigDecimal;

/*Ranking Object for Teams*/
public class Rankings extends Ranking {
    String dq;
    List<BigDecimal> extra_stats;
    String matches_played;
    String qual_average;
    int rank;
    Map<String, BigDecimal> record;
    List<BigDecimal> sort_orders;
    String team_key;
}