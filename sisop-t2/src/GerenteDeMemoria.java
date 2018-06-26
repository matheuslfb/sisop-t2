import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GerenteDeMemoria {

	private String algoritmo;

	private int tamPagina, tamMemoria, tamDisco;

	private Map<String, Processos> listaProcessos = new HashMap();

	private Pagina[] ram, disk;

	public GerenteDeMemoria(String algo, int tamPagina, int tamMemoria, int tamDisco) {

		this.algoritmo = algo;
		this.tamPagina = tamPagina;
		this.tamMemoria = tamMemoria;
		this.tamDisco = tamDisco;

		this.ram = new Pagina[tamMemoria / tamPagina];
		this.disk = new Pagina[tamDisco / tamPagina];

		this.criarPaginas();
	}

	private void criarPaginas() {
		for (int i = 0; i < ram.length; i++)
			ram[i] = new Pagina('R');
		System.out.println("Quantidade de paginas da mem�ria: " + ram.length);

		for (int i = 0; i < disk.length; i++)
			disk[i] = new Pagina('D');
		System.out.println("Quantidade de paginas do disco: "+disk.length);
	}
	
	
	/*
	 * @param nome do processo, parametro que ser� o tamanho da mem�ria
	 * necess�ria para criar o processo
	 * 
	 * @return retorna uma mensagem de ok ou falha na cria��o do processo.
	 */
	private String criarProcesso(String process, int parameter) {

		if (!listaProcessos.containsKey(process)) {
			Processos p = new Processos(process, parameter, this.tamPagina);
			listaProcessos.put(process, p);
			
			vinculaPagina(p, tamPagina);
			return "Processo criado com sucesso! \n P�ginas do processo: " ;
		} else
			return "N�o foi possivel criar o processo!";

	}
	
	public void vinculaPagina(Processos p, int tamPagina){
			p.addPagina(verificaPagina(ram));
	}
	
	public Pagina verificaPagina(Pagina[] vetor ){
		for (int i = 0; i < vetor.length; i++) {
			if(vetor[i] == null)
				return vetor[i];			
		}
		
		return null;
	}

	public void criar(BufferedReader br) {

		String linha;
		String buffer = "";
		String processo;
		String[] split;
		int parametro = 0;

		try {
			while ((linha = br.readLine()) != null) {
				split = linha.split(" ");

				// parse parameters
				processo = split[1];

				if (split.length > 2)
					parametro = Integer.parseInt(split[2]);

				switch (split[0].toUpperCase()) {
				case "C":
					buffer = criarProcesso(processo, parametro);
					break;

				case "A":
					buffer = acessarProcesso(processo, parametro);
					break;

				case "M":
					buffer = expandirProcesso(processo, parametro);
					break;

				}

				System.out.println("[" + linha + "] - " + buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * @param nome do processo, parametro que ser� o tamanho de memoria
	 * necess�ria para alocar o processo.
	 * 
	 * @return retorna uma mensagem de "ok" se conseguir alocar a mem�ria
	 * esperada, sen�o, retorna erro.
	 */
	private String expandirProcesso(String process, int parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @param nome do processo, parametro que ser� a posi��o de mem�ria em que o
	 * processo est�
	 * 
	 * @return retorna mensagem de "ok" ao conseguir acessar o processo, "erro"
	 * caso n�o consiga.
	 */
	private String acessarProcesso(String process, int parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
