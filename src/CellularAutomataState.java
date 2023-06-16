import java.util.List;

public interface CellularAutomataState {
	public abstract int getValue();

	public abstract String getDescription();

	public abstract CellularAutomataState applyRule(List<CellularAutomataState> neighborhood);

	public abstract Double getPercentage();
}
