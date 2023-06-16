import java.util.ArrayList;
import java.util.List;


public class MooreNeighborhood implements CellularAutomataNeighborhood {

	private int radius;

	public MooreNeighborhood(int radius) {
		this.radius = radius;
	}

	public CellularAutomataState[][] goToNextGeneration(CellularAutomataState[][] geracaoAtual) {

		int colunas = geracaoAtual.length;
		int linhas = geracaoAtual[0].length;

		CellularAutomataState[][] proxGen = new CellularAutomataState[colunas][linhas];

		for (int xi = 0; xi < colunas; xi++) {
			for (int yi = 0; yi < linhas; yi++) {

				List<CellularAutomataState> neighborhood = new ArrayList<>();
				for (int i = -radius; i <= radius; i++) {
					for (int j = -radius; j <= radius; j++) {
						
						int xNeighborhood = colunas+ xi+i;
						int yNeighborhood =linhas+yi+ j;

						if (xi != xNeighborhood && yNeighborhood != yi) {
							neighborhood.add(geracaoAtual[xNeighborhood][yNeighborhood]);
						}
					}
					
				}

				CellularAutomataState cell = geracaoAtual[xi][yi];
				proxGen[xi][yi] = cell.applyRule(neighborhood);

			}
		}

		return proxGen;
	}

}
