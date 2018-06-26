import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GerenteDeMemoria {

	private String algoritmo;

	private int tamPagina, tamMemoria, tamDisco;

	private Map<String, Process> processes = new HashMap();

	private Pagina[] ram, disk;

	public GerenteDeMemoria(String algo, int tamPagina, int tamMemoria, int tamDisco) {

		this.algoritmo = algo;
		this.tamPagina = tamPagina;
		this.tamMemoria = tamMemoria;
		this.tamDisco = tamDisco;

		this.ram = new Pagina[tamMemoria / tamPagina];
		this.disk = new Pagina[tamDisco / tamPagina];

	}

	public void criar(BufferedReader br) {

		String line, buffer = "", process;
		String[] blocks;
		int parameter = 0;

		try {
			while ((line = br.readLine()) != null) {
				blocks = line.split(" ");

				// parse parameters
				process = blocks[1];

				if (blocks.length > 2)
					parameter = Integer.parseInt(blocks[2]);

				switch (blocks[0].toUpperCase()) {
				case "C":
					buffer = criarProcesso(process, parameter);
					break;

				case "A":
					buffer = acessarProcesso(process, parameter);
					break;

				case "M":
					buffer = expandirProcesso(process, parameter);
					break;

				}

				System.out.println("[" + line + "] - " + buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String expandirProcesso(String process, int parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	private String acessarProcesso(String process, int parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	private String criarProcesso(String process, int parameter) {
		// TODO Auto-generated method stub
		return null;
	}

}
