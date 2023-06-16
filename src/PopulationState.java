import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public enum PopulationState implements CellularAutomataState {

	SUSCEPTIBLE(0, "Suscetível", 0.99), INFECTIOUS(1, "Infectado", 0.01), RECOVERY(2, "Recuperado", 0d);

	private final Map<PopulationState, Map<PopulationState, Collection<Function<Double, Function<Double, Double>>>>> graph = new HashMap<>();

	private final int value;
	private final String description;
	private final Double populationPercentage;


	private PopulationState(int value, String description, Double populationPercentage) {

		this.value = value;
		this.description = description;
		this.populationPercentage = populationPercentage;
	}

	private void initGraph() {

		if (graph.isEmpty()) {

			Collection<Function<Double, Function<Double, Double>>> susceptibleRecoveryRules = new ArrayList<>();
			susceptibleRecoveryRules.add(v -> k -> 0.1);

			Collection<Function<Double, Function<Double, Double>>> susceptibleInfectiousRules = new ArrayList<>();
			susceptibleInfectiousRules.add(v -> k -> 0.1);
			susceptibleInfectiousRules.add(v -> k -> 1 - Math.pow(Math.E, -1 * k * v));

			Map<PopulationState, Collection<Function<Double, Function<Double, Double>>>> susceptibleVertex = new LinkedHashMap<>();
			susceptibleVertex.put(RECOVERY, susceptibleRecoveryRules);
			susceptibleVertex.put(INFECTIOUS, susceptibleInfectiousRules);

			Collection<Function<Double, Function<Double, Double>>> infectiousRecoveryRules = new ArrayList<>();
			infectiousRecoveryRules.add(v -> k -> 0.6);

			Collection<Function<Double, Function<Double, Double>>> infectiousSusceptibleRules = new ArrayList<>();
			infectiousSusceptibleRules.add(v -> k -> 0.3);

			Map<PopulationState, Collection<Function<Double, Function<Double, Double>>>> infectiousVertex = new LinkedHashMap<>();
			infectiousVertex.put(RECOVERY, infectiousRecoveryRules);
			infectiousVertex.put(SUSCEPTIBLE, infectiousSusceptibleRules);

			Collection<Function<Double, Function<Double, Double>>> recoverySusceptibleRules = new ArrayList<>();
			recoverySusceptibleRules.add(v -> k -> 0.1);

			Map<PopulationState, Collection<Function<Double, Function<Double, Double>>>> recoveryVertex = new LinkedHashMap<>();
			recoveryVertex.put(SUSCEPTIBLE, recoverySusceptibleRules);

			graph.put(SUSCEPTIBLE, susceptibleVertex);
			graph.put(INFECTIOUS, infectiousVertex);
			graph.put(RECOVERY, recoveryVertex);
		}

	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public String getDescription() {
		return description;
	}


	@Override
	public CellularAutomataState applyRule(List<CellularAutomataState> neighborhood) {

		double v = neighborhood.stream().filter(n -> this.equals(n)).count();

		initGraph();
		Map<PopulationState, Collection<Function<Double, Function<Double, Double>>>> vertexMap = this.graph.get(this);

		Random random = new Random();
		for (PopulationState state : vertexMap.keySet()) {

			Collection<Function<Double, Function<Double, Double>>> rules = vertexMap.get(state);
			for (Function<Double, Function<Double, Double>> rule : rules) {

				double probability = rule.apply(v).apply(1d);
				if (random.nextDouble() < probability) {
					return state;
				}
			}
		}

		return this;
	}

	@Override
	public Double getPercentage() {
		return populationPercentage;
	}

	public static CellularAutomataState get(int value) {
		for (PopulationState populationState : values()) {
			if (populationState.value == value) {
				return populationState;
			}
		}
		return null;
	}
}
